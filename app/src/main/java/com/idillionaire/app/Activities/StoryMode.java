package com.idillionaire.app.Activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RemoteController;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.ads.MobileAds;
import com.idillionaire.app.Adapters.FavrtGrideMAdapter;
import com.idillionaire.app.Adapters.MagAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.StoryMediaImages;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.shts.android.storiesprogressview.StoriesProgressView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class StoryMode extends AppCompatActivity implements StoriesProgressView.StoriesListener {

    StoriesProgressView storiesProgressView;
    ImageView imageview;
    RelativeLayout back;
    TextView story_title;
    ImageView title_image;
    public static String title;
    public static String media_file_name;
    private ArrayList<Login.Favourite_images> liked_items_list;
    private ArrayList<Login.Favourite_images> removed_items_list;
    RelativeLayout like;
    RelativeLayout schedule;
    ImageView like_icon;
    Type type;
    SharedPreferences mprefs;
    Gson gson = new Gson();
    String favoriteListpost;
    String favoriteListget;
    boolean tf;
    ArrayList<Login.Favourite_images> favorite_list_from_login;
    String resources_id;
    RelativeLayout bookmark;
    RelativeLayout share;
    String activity_name;
    String premium_name;

    String id_of_resources;

    String purchase_token;

    private ArrayList<MediaFiles> media_files_arr;
    Bundle bundle;
    String fromNotification;
    String from_notification_title;
    String from_notification_gallery_img;

    public static String schedule_detail_image;
    public static String schedule_list_image;
    public static String schedule_image_id;
    public static String schedule_gallery_name;
    ArrayList<MediaFiles> mediaimagelist;

    private static InterstitialAd interstitialAd;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;


    int main_counter = 0;
    ArrayList <StoryMediaImages> storyMediaImagesList;
    int counter = 0;
    ArrayList<MediaFiles> resources;


    private final long[] durations = new long[]{
            500L, 1000L, 1500L, 4000L, 5000L, 1000
    };
    long pressTime = 0L;
    long limit = 500L;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    pressTime = System.currentTimeMillis();
                    storiesProgressView.pause();
                    return false;
                case MotionEvent.ACTION_UP:
                    long now = System.currentTimeMillis();
                    storiesProgressView.resume();
                    return limit < now - pressTime;
            }
            return false;
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (fromNotification.equals("true")){
            Intent intt = new Intent(StoryMode.this, HomePage.class);
            intt.putExtra("fromNotification", false);
            startActivity(intt);
            finish();
        }else {
            if (purchase_token.isEmpty()) {
                interstitialAd.show();
            }
            finish();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
//        bundle = getIntent().getExtras();
//        fromNotification = bundle.getString("Notification");
//
//        if (fromNotification.equals("true")){
//            String gallery_id_from_notification = bundle.getString("Gallery_id");
//            String gallery_id_extracted = gallery_id_from_notification;
//            ApiGetGallery(gallery_id_extracted);
//            resources = media_files_arr;
//        }else {
//            storyMediaImagesList = MagAdapter.story_media_images;
//            resources = storyMediaImagesList.get(main_counter).getMediaData();
//        }

    }

    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_mode);

        SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");

        if (purchase_token.isEmpty()) {
            MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.add_app_id));

            interstitialAd = new InterstitialAd(getApplicationContext());
            interstitialAd.setAdUnitId(String.valueOf(R.string.add_add_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }

        like_icon = findViewById(R.id.like);
        imageview = (ImageView) findViewById(R.id.image);

        activity_name = "StoryMode";

        bundle = getIntent().getExtras();
        fromNotification = bundle.getString("Notification");

        if (fromNotification.equals("true")){
            ApiGetGallery();
        }else {
            storyMediaImagesList = MagAdapter.story_media_images;
            resources = storyMediaImagesList.get(main_counter).getMediaData();

            // Loop for checking gallery name
            mprefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mprefs.edit();
            gson = new Gson();
            String galleryList = mprefs.getString("GALLERYLIST", "");
            Type type = new TypeToken<List<MediaFiles>>(){}.getType();
            mediaimagelist = gson.fromJson(galleryList, type);

            String main_id = resources.get(counter).get_id();
            for (int i = 0; i < mediaimagelist.size(); i++){
                String main_image_id = mediaimagelist.get(i).get_id();
                if (main_id.equals(main_image_id)){
                    schedule_gallery_name = mediaimagelist.get(i).getGallery_title();
                    break;
                }
            }
            // Loop for checking gallery name
            mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
            favoriteListget = mprefs.getString("FAVORITELIST", "");
            type = new TypeToken<List<Login.Favourite_images>>(){}.getType();
            favorite_list_from_login = gson.fromJson(favoriteListget, type);

            if (favorite_list_from_login.isEmpty()){

                tf = false;
                like_icon.setImageResource(R.drawable.story_like);

            }else {

                // Loop for matching ids

                resources_id = resources.get(counter).get_id();
                for (int i = 0; i < favorite_list_from_login.size(); i++) {
                    String image_id = favorite_list_from_login.get(i).getImage_id();
                    if (resources_id.equals(image_id)) {
                        like_icon.setImageResource(R.drawable.story_like_filled);
                        favorite_list_from_login.get(i).setTf(true);
                        tf = favorite_list_from_login.get(i).isTf();
                        resources.get(counter).setTf(true);
                        break;
                    } else {
                        like_icon.setImageResource(R.drawable.story_like);
                        favorite_list_from_login.get(i).setTf(false);
                        tf = favorite_list_from_login.get(i).isTf();
                        resources.get(counter).setTf(false);
                    }
                }

                // Loop for matching ids

            }
//            ApiGetGallery();
        }


        // full screen

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // full screen



        // Schedule

        schedule = findViewById(R.id.btn2);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                String purchase_token = sp.getString("PURCHASETOKEN", "");

                if(!purchase_token.isEmpty()){

                    schedule_detail_image = resources.get(counter).getDetailed_img_name();
                    schedule_list_image = resources.get(counter).getList_image();
                    schedule_image_id = resources.get(counter).get_id();
//                schedule_gallery_name = storyMediaImagesList.get(main_counter).getTitle();
                    Intent intent = new Intent(StoryMode.this, ScheduleActivity.class);
                    intent.putExtra("activity_name", activity_name);
                    startActivity(intent);

                }else {
                    Intent intent = new Intent(StoryMode.this, PremiumActivity.class);
                    intent.putExtra("premium", activity_name);
                    intent.putExtra("schedule_detail_image", resources.get(counter).getDetailed_img_name());
                    intent.putExtra("schedule_list_image", resources.get(counter).getList_image());
                    intent.putExtra("gallery_name", resources.get(counter).getGallery_title());
                    intent.putExtra("main_id", resources.get(counter).get_id());
                    startActivity(intent);

                }
            }
        });

        // Schedule

        // Bookmark

//        final String image_url = Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name();
        bookmark = (RelativeLayout) findViewById(R.id.btn4);
        bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkAndRequestPermissions();
                DownLoadTask();

            }
        });

        // Bookmark
        // Share

        share = (RelativeLayout) findViewById(R.id.btn3);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img = Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name();
                new AsyncTaskLoadImage().execute(img);
            }
        });

        // Share
        // back

        back = (RelativeLayout) findViewById(R.id.back_story);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fromNotification.equals("true")){
                    Intent intt = new Intent(StoryMode.this, HomePage.class);
                    intt.putExtra("fromNotification", false);
                    startActivity(intt);
                    finish();
                }else {
                    if (purchase_token.isEmpty()) {
                        interstitialAd.show();
                    }
                    finish();
                }
            }
        });

        // back

        // like




        like = (RelativeLayout) findViewById(R.id.btn1);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                like.setEnabled(false);

                if (!tf){
                    like_icon.setImageResource(R.drawable.story_like_filled);
                    tf = true;
                    apiLike();

                    resources.get(counter).setTf(true);

                    String getLikedItems = mprefs.getString("FAVORITELIST", "");
                    type = new TypeToken<List<Login.Favourite_images>>(){}.getType();
                    favorite_list_from_login = gson.fromJson(getLikedItems, type);
//                    favorite_list_from_login.get(counter).setTf(true);
//                    tf = favorite_list_from_login.get(counter).isTf();
                    like.setEnabled(true);
                }
                  else{
                    like_icon.setImageResource(R.drawable.story_like);
                    tf = false;
                    apiRemoveFvrt();

                    resources.get(counter).setTf(false);

                    String getRemovedItems = mprefs.getString("FAVORITELIST", "");
                    type = new TypeToken<List<Login.Favourite_images>>(){}.getType();
                    favorite_list_from_login = gson.fromJson(getRemovedItems, type);
//                    favorite_list_from_login.get(counter).setTf(false);
//                    tf = favorite_list_from_login.get(counter).isTf();
                    like.setEnabled(true);
                }
            }
        });


        story_title = (TextView) findViewById(R.id.story_title);
        title_image = (ImageView) findViewById(R.id.title_image);

        if (fromNotification.equals("false")){
            title = storyMediaImagesList.get(main_counter).getTitle();
            story_title.setText(title);

            media_file_name = storyMediaImagesList.get(main_counter).getMedia_file_name();
            Picasso.with(StoryMode.this)
                    .load(Constants.URL.BASE_URL + media_file_name)
                    .transform(new CropCircleTransformation())
                    .into(title_image);
        }

        if (fromNotification.equals("false")){
            storiesProgressView = (StoriesProgressView) findViewById(R.id.stories_bar);
            storiesProgressView.setStoriesCount(resources.size());
            storiesProgressView.setStoryDuration(4000000000000000L);
            storiesProgressView.setStoriesListener(this);
            storiesProgressView.startStories();

            Picasso.with(this)
                    .load(Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name())
                    .into(imageview);
        }


//        image.setImageResource(resources[counter]);


        View reverse = findViewById(R.id.reverse);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (main_counter == 0 && counter == 0){
                    if (fromNotification.equals("true")){
                        Intent intt = new Intent(StoryMode.this, HomePage.class);
                        intt.putExtra("fromNotification", false);
                        startActivity(intt);
                        finish();
                    }else {
                        finish();
                    }

                } else {
                    if (counter == 0) {
                        if (purchase_token.isEmpty()) {
                            interstitialAd.show();
                        }

                            if (favorite_list_from_login.isEmpty()){
                                tf = false;
                                like_icon.setImageResource(R.drawable.story_like);
                            }else {
                                resources_id = resources.get(counter).get_id();
                                for (int i = 0; i < favorite_list_from_login.size(); i++) {
                                    String image_id = favorite_list_from_login.get(i).getImage_id();
                                    if (resources_id.equals(image_id)) {
                                        like_icon.setImageResource(R.drawable.story_like_filled);
                                        favorite_list_from_login.get(i).setTf(true);
                                        tf = favorite_list_from_login.get(i).isTf();
                                        resources.get(counter).setTf(true);
                                        break;
                                    } else {
                                        like_icon.setImageResource(R.drawable.story_like);
                                        favorite_list_from_login.get(i).setTf(false);
                                        tf = favorite_list_from_login.get(i).isTf();
                                        resources.get(counter).setTf(false);
                                    }
                                }
                            }
                            x = 0;
                            counter = 0;
                            int y = main_counter;
                            resources = storyMediaImagesList.get(--main_counter).getMediaData();

                            title = storyMediaImagesList.get(main_counter).getTitle();
                            story_title.setText(title);

                            media_file_name = storyMediaImagesList.get(main_counter).getMedia_file_name();
                            Picasso.with(StoryMode.this)
                                    .load(Constants.URL.BASE_URL + media_file_name)
                                    .transform(new CropCircleTransformation())
                                    .into(title_image);

                            storiesProgressView = (StoriesProgressView) findViewById(R.id.stories_bar);
                            storiesProgressView.setStoriesCount(resources.size());
                            storiesProgressView.setStoryDuration(4000000000000000L);
                            storiesProgressView.setStoriesListener(StoryMode.this);
                            storiesProgressView.startStories();

                        imageview = (ImageView) findViewById(R.id.image);
//        image.setImageResource(resources[counter]);
                            Picasso.with(StoryMode.this)
                                    .load(Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name())
                                    .into(imageview);
                    }else{
                        if (favorite_list_from_login.isEmpty()){
                            tf = false;
                            like_icon.setImageResource(R.drawable.story_like);
                        }else {
                            resources_id = resources.get(counter).get_id();
                            for (int i = 0; i < favorite_list_from_login.size(); i++) {
                                String image_id = favorite_list_from_login.get(i).getImage_id();
                                if (resources_id.equals(image_id)) {
                                    like_icon.setImageResource(R.drawable.story_like_filled);
                                    favorite_list_from_login.get(i).setTf(true);
                                    tf = favorite_list_from_login.get(i).isTf();
                                    resources.get(counter).setTf(true);
                                    break;
                                } else {
                                    like_icon.setImageResource(R.drawable.story_like);
                                    favorite_list_from_login.get(i).setTf(false);
                                    tf = favorite_list_from_login.get(i).isTf();
                                    resources.get(counter).setTf(false);
                                }
                            }
                        }

                            storiesProgressView.reverse();

                        }

                }
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        View skip = findViewById(R.id.skip);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                incrementActions();

                int g =resources.size();
                g=g+1;

                    if (counter + 1 == g - 1) {
                        if (fromNotification.equals("false")){

                        if (main_counter == storyMediaImagesList.size() - 1) {
                            if (fromNotification.equals("true")){
                                Intent intt = new Intent(StoryMode.this, HomePage.class);
                                intt.putExtra("fromNotification", false);
                                startActivity(intt);
                                finish();
                            }else {
                                finish();
                            }

                        } else {

                            if (purchase_token.isEmpty()) {
                                interstitialAd.show();
                            }

                            x = 0;

                            if (purchase_token.isEmpty()) {
                                interstitialAd.show();
                            }

                            counter = 0;
                            int y = main_counter;
//            int u=main_counter+1;
                            resources = storyMediaImagesList.get(++main_counter).getMediaData();
//                    MediaFiles newempty = new MediaFiles();
//                    resources.add(newempty);
                            title = storyMediaImagesList.get(main_counter).getTitle();
                            story_title.setText(title);

                            media_file_name = storyMediaImagesList.get(main_counter).getMedia_file_name();
                            Picasso.with(StoryMode.this)
                                    .load(Constants.URL.BASE_URL + media_file_name)
                                    .transform(new CropCircleTransformation())
                                    .into(title_image);

                            storiesProgressView = (StoriesProgressView) findViewById(R.id.stories_bar);
                            storiesProgressView.setStoriesCount(resources.size());
                            storiesProgressView.setStoryDuration(4000000000000000L);
                            storiesProgressView.setStoriesListener(StoryMode.this);
                            storiesProgressView.startStories();

                            imageview = (ImageView) findViewById(R.id.image);
                            Picasso.with(StoryMode.this)
                                    .load(Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name())
                                    .into(imageview);
                            if (favorite_list_from_login.isEmpty()) {
                                tf = false;
                                like_icon.setImageResource(R.drawable.story_like);
                            } else {
                                resources_id = resources.get(counter).get_id();
                                for (int i = 0; i < favorite_list_from_login.size(); i++) {
                                    String image_id = favorite_list_from_login.get(i).getImage_id();
                                    if (resources_id.equals(image_id)) {
                                        like_icon.setImageResource(R.drawable.story_like_filled);
                                        favorite_list_from_login.get(i).setTf(true);
                                        tf = favorite_list_from_login.get(i).isTf();
                                        break;
                                    } else {
                                        like_icon.setImageResource(R.drawable.story_like);
                                        favorite_list_from_login.get(i).setTf(false);
                                        tf = favorite_list_from_login.get(i).isTf();
                                    }
                                }
                            }
                        }}else {
                            if (fromNotification.equals("true")){
                                Intent intt = new Intent(StoryMode.this, HomePage.class);
                                intt.putExtra("fromNotification", false);
                                startActivity(intt);
                                finish();
                            }else {
                                finish();
                            }

                        }
                    } else {
                        storiesProgressView.skip();
                        if (favorite_list_from_login.isEmpty()) {
                            tf = false;
                            like_icon.setImageResource(R.drawable.story_like);
                        } else {
                            resources_id = resources.get(counter).get_id();
                            for (int i = 0; i < favorite_list_from_login.size(); i++) {
                                String image_id = favorite_list_from_login.get(i).getImage_id();
                                if (resources_id.equals(image_id)) {
                                    like_icon.setImageResource(R.drawable.story_like_filled);
                                    favorite_list_from_login.get(i).setTf(true);
                                    tf = favorite_list_from_login.get(i).isTf();
                                    break;
                                } else {
                                    like_icon.setImageResource(R.drawable.story_like);
                                    favorite_list_from_login.get(i).setTf(false);
                                    tf = favorite_list_from_login.get(i).isTf();
                                }
                            }
                        }

                    }

            }
        });
        skip.setOnTouchListener(onTouchListener);
    }

    @Override
    public void onNext() {

            Picasso.with(this)
                    .load(Constants.URL.BASE_URL + resources.get(++counter).getDetailed_img_name())
                    .into(imageview);
    }

    @Override
    public void onPrev() {
        Picasso.with(this)
                .load(Constants.URL.BASE_URL + resources.get(--counter).getDetailed_img_name())
                .into(imageview);
    }

    @Override
    public void onComplete() {
        if (fromNotification.equals("true")){
            Intent intt = new Intent(StoryMode.this, HomePage.class);
            intt.putExtra("fromNotification", false);
            startActivity(intt);
            finish();
        }else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        storiesProgressView.destroy();
        super.onDestroy();
    }
    private static int x = 0;

    public void incrementActions() {
        x++;
        if(x >= 10) {
            x = 0;

            if (purchase_token.isEmpty()) {
                interstitialAd.show();
            }
        }
    }

    private void apiLike(){

        id_of_resources = resources.get(counter).get_id();

        mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN=mprefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVRT + id_of_resources, getApplicationContext(),postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
                        liked_items_list = new ArrayList<>();
                            for (int i = 0; i < favourite_images.length(); i++){
                                JSONObject a = favourite_images.getJSONObject(i);
                                Login.Favourite_images addFavoriteobj = new Login.Favourite_images();

                                String _id = a.getString("_id");
                                String title = a.getString("title");
                                String image_id = a.getString("image_id");
                                String Detailed_img_name = a.getString("Detailed_img_name");
                                String list_img = a.getString("list_img");
                                int order_number = a.getInt("order_number");

                                addFavoriteobj.set_id(_id);
                                addFavoriteobj.setTitle(title);
                                addFavoriteobj.setImage_id(image_id);
                                addFavoriteobj.setDetailed_img_name(Detailed_img_name);
                                addFavoriteobj.setList_img(list_img);
                                addFavoriteobj.setOrder_number(order_number);
                                addFavoriteobj.setTf(true);
                                liked_items_list.add(addFavoriteobj);
                            }

//                        tf = favorite_list_from_login.get(counter).isTf();
                        favoriteListpost = gson.toJson(liked_items_list);
                        mprefs.edit().putString("FAVORITELIST", favoriteListpost).apply();

                        Toast.makeText(getApplicationContext(), "Successfully added to favorites", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void  apiRemoveFvrt(){

        id_of_resources = resources.get(counter).get_id();

        String USER_TOKEN=mprefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.RMVFVRT + id_of_resources, StoryMode.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
                        removed_items_list = new ArrayList<>();
                        for (int i = 0; i < favourite_images.length(); i++) {
                            JSONObject a = favourite_images.getJSONObject(i);
                            Login.Favourite_images favoriteListObj = new Login.Favourite_images();

                            String _id = a.getString("_id");
                            String title = a.getString("title");
                            String image_id = a.getString("image_id");
                            String Detailed_img_name = a.getString("Detailed_img_name");
                            String list_img = a.getString("list_img");
                            int order_number = a.getInt("order_number");

                            favoriteListObj.set_id(_id);
                            favoriteListObj.setTitle(title);
                            favoriteListObj.setImage_id(image_id);
                            favoriteListObj.setDetailed_img_name(Detailed_img_name);
                            favoriteListObj.setList_img(list_img);
                            favoriteListObj.setOrder_number(order_number);
                            favoriteListObj.setTf(false);

                            removed_items_list.add(favoriteListObj);
                        }
//                        tf = favorite_list_from_login.get(counter).isTf();
                        favoriteListpost = gson.toJson(removed_items_list);
                        mprefs.edit().putString("FAVORITELIST", favoriteListpost).apply();

                        Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(StoryMode.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(StoryMode.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(StoryMode.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(StoryMode.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    // Download Class

    private void DownLoadTask(){

        if (checkAndRequestPermissions()) {
            String filename = resources.get(counter).get_id() + ".jpg";
            String downloadUrlOfImage = Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name();
            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "Idillionaire" + "/");


            if (!direct.exists()) {
                direct.mkdir();
                Log.d("First time Msg.", "dir created for first time");
            }

            DownloadManager dm = (DownloadManager) getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            File.separator + "Idillionaire" + File.separator + filename);

            dm.enqueue(request);
            Toast.makeText(getApplicationContext(), "Download Started...", Toast.LENGTH_SHORT).show();
        }
    }

    // Download Class
    // Share Class

    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;
        public AsyncTaskLoadImage() {
//            this.imageView = imageView;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap b) {

            if(checkAndRequestPermissions()) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContentResolver(),
                        b, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
//                share.putExtra(Intent.EXTRA_SUBJECT, "hellooo the image");
                share.putExtra(Intent.EXTRA_TEXT, "Download the Idillionaire App for more inspiration! Now available on iOS and Android.");
                startActivity(Intent.createChooser(share, "Share with"));

            }
        }
    }

    // Share Class

    private void ApiGetGallery(){

        String gallery_id_from_notification = bundle.getString("Gallery_id");
        String gallery_id_extracted = gallery_id_from_notification;

//        String id = "5b2b538bd1ef647d09929fd1";

        mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN=mprefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GETGALLERY + gallery_id_extracted, StoryMode.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        JSONObject gallery = jsonObject.getJSONObject("gallery");
                        String _id = gallery.getString("_id");
                        String category = gallery.getString("category");
                        String title = gallery.getString("title");
                        String image = gallery.getString("image");
                        boolean is_active = gallery.getBoolean("is_active");
                        JSONArray media_files = gallery.getJSONArray("media_files");

                        media_files_arr = new ArrayList<>();
                        for (int x = 0; x < media_files.length(); x++ ){
                            JSONObject s = media_files.getJSONObject(x);
                            MediaFiles mediaFiles_obj = new MediaFiles();

                            int order_number_media = s.getInt("order_number");
                            String _id_media = s.getString("_id");
                            String title_media = s.getString("title");
                            String list_image_media = s.getString("list_image");
                            String Detailed_img_name = s.getString("Detailed_img_name");

                            mediaFiles_obj.set_id(_id_media);
                            mediaFiles_obj.setDetailed_img_name(Detailed_img_name);
                            mediaFiles_obj.setList_image(list_image_media);
                            mediaFiles_obj.setTitle(title_media);
                            mediaFiles_obj.setOrder_number(order_number_media);
                            media_files_arr.add(mediaFiles_obj);
                        }

                        resources = media_files_arr;

                        from_notification_title = title;
                        from_notification_gallery_img = image;

                        story_title.setText(from_notification_title);
                        Picasso.with(StoryMode.this)
                                .load(Constants.URL.BASE_URL + from_notification_gallery_img)
                                .transform(new CropCircleTransformation())
                                .into(title_image);

                        // Loop for checking gallery name
                        mprefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mprefs.edit();
                        gson = new Gson();
                        String galleryList = mprefs.getString("GALLERYLIST", "");
                        Type type = new TypeToken<List<MediaFiles>>(){}.getType();
                        mediaimagelist = gson.fromJson(galleryList, type);

                        String main_id = resources.get(counter).get_id();
                        for (int i = 0; i < mediaimagelist.size(); i++){
                            String main_image_id = mediaimagelist.get(i).get_id();
                            if (main_id.equals(main_image_id)){
                                schedule_gallery_name = mediaimagelist.get(i).getGallery_title();
                                break;
                            }
                        }
                        // Loop for checking gallery name

                        mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        favoriteListget = mprefs.getString("FAVORITELIST", "");
                        type = new TypeToken<List<Login.Favourite_images>>(){}.getType();
                        favorite_list_from_login = gson.fromJson(favoriteListget, type);

                        if (favorite_list_from_login.isEmpty()){
                            tf = false;
                            like_icon.setImageResource(R.drawable.story_like);
                        }else {

                            // Loop for matching ids
                            resources_id = resources.get(counter).get_id();
                            for (int i = 0; i < favorite_list_from_login.size(); i++) {
                                String image_id = favorite_list_from_login.get(i).getImage_id();
                                if (resources_id.equals(image_id)) {
                                    like_icon.setImageResource(R.drawable.story_like_filled);
                                    favorite_list_from_login.get(i).setTf(true);
                                    tf = favorite_list_from_login.get(i).isTf();
                                    resources.get(counter).setTf(true);
                                    break;
                                } else {
                                    like_icon.setImageResource(R.drawable.story_like);
                                    favorite_list_from_login.get(i).setTf(false);
                                    tf = favorite_list_from_login.get(i).isTf();
                                    resources.get(counter).setTf(false);
                                }
                            }
                            // Loop for matching ids
                        }


                        storiesProgressView = (StoriesProgressView) findViewById(R.id.stories_bar);
                        storiesProgressView.setStoriesCount(resources.size());
                        storiesProgressView.setStoryDuration(4000000000000000L);
                        storiesProgressView.setStoriesListener(StoryMode.this);
                        storiesProgressView.startStories();

                        Picasso.with(StoryMode.this)
                                .load(Constants.URL.BASE_URL + resources.get(counter).getDetailed_img_name())
                                .into(imageview);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

}
