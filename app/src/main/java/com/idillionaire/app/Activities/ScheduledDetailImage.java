package com.idillionaire.app.Activities;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.FavoriteList;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.R;
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

public class ScheduledDetailImage extends AppCompatActivity {

    RelativeLayout sche_detailed_image_back, sche_detailed_like, sche_detailed_schedual, sche_detailed_share, sche_detailed_download;
    ImageView sche_detailed_image, like;

    SharedPreferences mPrefs;
    private List<FavoriteList> favourite_images_arr;
    Gson gson = new Gson();
    Type type;
    String favoriteListPost;

    ArrayList<Login.Favourite_images> favorite_list_from_login;
    String favoriteListget;
    boolean tf;

    public static String detailed_image, listimage, galleryname, schedule_detail_image_id;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intt = new Intent(ScheduledDetailImage.this, HomePage.class);
        intt.putExtra("fromNotification", false);
        startActivity(intt);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduled_detail_image);

        initialization();

        schedule_detail_image_id = getIntent().getStringExtra("image_id");
        detailed_image = getIntent().getStringExtra("detailedimage");
        listimage = getIntent().getStringExtra("listimage");
        galleryname = getIntent().getStringExtra("gallery_name");


        Picasso.with(this)
                .load(Constants.URL.BASE_URL + detailed_image)
                .into(sche_detailed_image);

        // Like Button Loop

        mPrefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        favoriteListget = mPrefs.getString("FAVORITELIST", "");
        type = new TypeToken<List<Login.Favourite_images>>() {
        }.getType();
        favorite_list_from_login = gson.fromJson(favoriteListget, type);

        if (favorite_list_from_login.isEmpty()) {
            like.setImageResource(R.drawable.story_like);
            tf = false;
        } else {

            // Loop for matching ids
            for (int i = 0; i < favorite_list_from_login.size(); i++) {
                String image_id_arr = favorite_list_from_login.get(i).getImage_id();
                if (schedule_detail_image_id.equals(image_id_arr)) {
                    like.setImageResource(R.drawable.story_like_filled);
                    favorite_list_from_login.get(i).setTf(true);
                    tf = true;
                    break;
                } else {
                    like.setImageResource(R.drawable.story_like);
                    favorite_list_from_login.get(i).setTf(false);
                    tf = false;
                }
            }
            // Loop for matching ids
        }

        // Like Button Loop

        sche_detailed_image_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(ScheduledDetailImage.this, HomePage.class);
                intt.putExtra("fromNotification", false);
                startActivity(intt);
                finish();
            }
        });

        sche_detailed_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (tf){
                    sche_detailed_like.setEnabled(false);
                    apiRemoveFavorite();
                }else {
                    sche_detailed_like.setEnabled(false);
                    apiFavorite();
                }
            }
        });

        sche_detailed_schedual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(ScheduledDetailImage.this, ScheduleActivity.class);
                intent.putExtra("activity_name", "ScheduleDetailImage");
                startActivity(intent);

            }
        });

        sche_detailed_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String img = Constants.URL.BASE_URL + detailed_image;
                new AsyncTaskLoadImage().execute(img);
            }
        });

        sche_detailed_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndRequestPermissions();
                DownLoadTask();
            }
        });




    }


    private void apiRemoveFavorite(){
        mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.RMVFVRT + schedule_detail_image_id, ScheduledDetailImage.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        like.setImageResource(R.drawable.story_like);
                        tf = false;
                        sche_detailed_like.setEnabled(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void apiFavorite(){
        mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVRT + schedule_detail_image_id, ScheduledDetailImage.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        like.setImageResource(R.drawable.story_like_filled);
                        tf = true;
                        sche_detailed_like.setEnabled(true);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


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


    private void DownLoadTask(){

        if(checkAndRequestPermissions()) {
            String filename = schedule_detail_image_id + ".jpg";
            String downloadUrlOfImage = Constants.URL.BASE_URL + detailed_image;
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


    private  boolean checkAndRequestPermissions() {
        int storage = ContextCompat.checkSelfPermission(ScheduledDetailImage.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(ScheduledDetailImage.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(ScheduledDetailImage.this,listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }



    private void initialization(){
        sche_detailed_image_back = findViewById(R.id.sche_detailed_image_back);
        sche_detailed_like = findViewById(R.id.sche_detailed_like);
        sche_detailed_schedual = findViewById(R.id.sche_detailed_schedual);
        sche_detailed_share = findViewById(R.id.sche_detailed_share);
        sche_detailed_download = findViewById(R.id.sche_detailed_download);
        sche_detailed_image = findViewById(R.id.sche_detailed_image);
        like = findViewById(R.id.like);
    }

}
