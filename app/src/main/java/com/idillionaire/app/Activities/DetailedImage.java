package com.idillionaire.app.Activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.FavrtGrideMAdapter;
import com.idillionaire.app.Adapters.MagAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.FavoriteList;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.StoryMediaImages;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
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

import static com.idillionaire.app.Activities.StoryMode.REQUEST_ID_MULTIPLE_PERMISSIONS;

public class DetailedImage extends AppCompatActivity {

    ImageView detailed_image;
    RelativeLayout back_btn;
    RelativeLayout remove_like;
    public static String image_id;
    public static String main_id;
    ImageView like_logo;
    RelativeLayout download;
    String detailed_image_from_arr;
    private List<FavoriteList> favourite_images_arr;
    SharedPreferences mPrefs;
    RelativeLayout share;
    RelativeLayout schedule;
    InterstitialAd interstitialAd;
    Gson gson;
    Type type;
    String favoriteListPost;
    String purchase_token;

    ArrayList <MediaFiles> storyMediaImagesList;

    public static String schedule_detail_image;
    public static String schedule_list_image;
    public static String schedule_gallery_name;
    public static String schedule_image_id;
    String activity_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_image);

//        madView = (AdView)findViewById(R.id.adView);
        SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");

        if (purchase_token.isEmpty()) {
            MobileAds.initialize(getApplicationContext(), String.valueOf(R.string.add_app_id));
            interstitialAd = new InterstitialAd(this);
            interstitialAd.setAdUnitId(String.valueOf(R.string.add_add_id));
            AdRequest adRequest = new AdRequest.Builder().build();
            interstitialAd.loadAd(adRequest);
        }


        activity_name = "DetailedImage";

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        back_btn = (RelativeLayout) findViewById(R.id.detailed_image_back);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Schedule Activity

        schedule = findViewById(R.id.btn2);
        schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                String purchase_token = sp.getString("PURCHASETOKEN", "");

                if(!purchase_token.isEmpty()){
                    // Loop for checking gallery name
                    SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    SharedPreferences.Editor mEditor = mPrefs.edit();
                    gson = new Gson();
                    String galleryList = mPrefs.getString("GALLERYLIST", "");
                    Type type = new TypeToken<List<MediaFiles>>(){}.getType();
                    storyMediaImagesList = gson.fromJson(galleryList, type);

                    main_id = FavrtGrideMAdapter.image_id;
                    for (int i = 0; i < storyMediaImagesList.size(); i++){
                        String main_image_id = storyMediaImagesList.get(i).get_id();
                        if (main_id.equals(main_image_id)){
                            schedule_gallery_name = storyMediaImagesList.get(i).getGallery_title();
                            break;
                        }
                    }
                    // Loop for checking gallery name
                    schedule_detail_image = FavrtGrideMAdapter.detailed_image_name;
                    schedule_list_image = FavrtGrideMAdapter.schedule_list_image;
                    schedule_image_id = FavrtGrideMAdapter._id;
                    Intent intent = new Intent(DetailedImage.this, ScheduleActivity.class);
                    intent.putExtra("activity_name", activity_name);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(DetailedImage.this, PremiumActivity.class);
                    intent.putExtra("premium", "DetailedImage");
                    intent.putExtra("schedule_detail_image", FavrtGrideMAdapter.detailed_image_name);
                    intent.putExtra("schedule_list_image", FavrtGrideMAdapter.schedule_list_image);
                    intent.putExtra("main_id", FavrtGrideMAdapter._id);
                    startActivity(intent);
                }
            }
        });

        // Schedule Activity

        detailed_image_from_arr = FavrtGrideMAdapter.detailed_image_name;
        detailed_image = (ImageView) findViewById(R.id.detailed_image);
        Picasso.with(DetailedImage.this)
                .load(Constants.URL.BASE_URL + detailed_image_from_arr)
                .into(detailed_image);

        like_logo = (ImageView) findViewById(R.id.like);

        download = findViewById(R.id.btn4);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
                DownLoadTask();
            }
        });

        remove_like = (RelativeLayout) findViewById(R.id.btn1);
        remove_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_id = FavrtGrideMAdapter.image_id;
                apiRemoveFavorite();
            }
        });

        share = findViewById(R.id.btn3);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Log.d("TAG", "Was not loaded yet");
                    String img = Constants.URL.BASE_URL + detailed_image_from_arr;
                    new AsyncTaskLoadImage().execute(img);
            }
        });
    }

    // Download Class

    private void DownLoadTask() {

        if(checkAndRequestPermissions()) {

        String filename = image_id + ".jpg";
        String downloadUrlOfImage = Constants.URL.BASE_URL + detailed_image_from_arr;
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

    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(DetailedImage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(DetailedImage.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(DetailedImage.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    private void apiRemoveFavorite(){
        mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.RMVFVRT + image_id, DetailedImage.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
//                                ArrayList<FavoriteList> favourite_images_arr = new ArrayList<>();
                        favourite_images_arr = new ArrayList<>();
                        for (int i = 0; i < favourite_images.length(); i++) {
                            JSONObject a = favourite_images.getJSONObject(i);
                            FavoriteList favoriteListObj = new FavoriteList();

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
                            favourite_images_arr.add(favoriteListObj);
                        }

                            gson = new Gson();
                            favoriteListPost = gson.toJson(favourite_images_arr);
                            mPrefs.edit().putString("FAVORITELIST", favoriteListPost).apply();
                            Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                            finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
