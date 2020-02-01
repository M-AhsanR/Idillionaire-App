package com.idillionaire.app;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.TwitterAuthProvider;
import com.google.gson.Gson;
import com.idillionaire.app.Activities.GoogleSignInActivity;
import com.idillionaire.app.Activities.HomePage;
import com.idillionaire.app.Activities.LoginActivity;
import com.idillionaire.app.Activities.SignupActivity;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Login;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class TwitterActivity extends AppCompatActivity {

    TwitterLoginButton loginButton;

    ProgressDialog progressDialog;

    String time_zone;
    SharedPreferences sp;
    Gson gson = new Gson();
    String token_twitter;
    String token_secret;
    String consumer_key;
    String consumer_secret;
    Bundle bundle;
    String intent_extra;

    private ArrayList<Login.Favourite_images> favouriteImages_arr;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (intent_extra.equals("login")) {
            Intent intent = new Intent(TwitterActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(TwitterActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_twitter);
        Twitter.initialize(this);
        // Inflate layout (must be done after TwitterActivity is configured)
        setContentView(R.layout.activity_twitter);

        time_zone = TimeZone.getDefault().getID();
        sp = getSharedPreferences("login", MODE_PRIVATE);

        bundle = getIntent().getExtras();
        intent_extra = bundle.getString("google");


        loginButton = (TwitterLoginButton) findViewById(R.id.login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(TwitterActivity.this, "Authenticating Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void login(TwitterSession session) {
        String username = session.getUserName();
        token_twitter = session.getAuthToken().token;
        token_secret = session.getAuthToken().secret;
        consumer_key = "FuipP0lP3pQAOiPR50VAqmRr1";
        consumer_secret = "eeH8XNQNj3l1yRDlCmvFr37SBTOe0WJ7IshPEFzRs34OyNqxoE";

        apiTwitterlogin();

        String sdcvs = "dc";

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    private void apiTwitterlogin() {

        progressDialog = new ProgressDialog(TwitterActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refresh_token = mPrefs_token.getString("FIREBASE_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("device_token", refresh_token);
        postParam.put("platform", "android");
        postParam.put("time_zone", String.valueOf(time_zone));

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.TWITTERLOGIN + token_twitter + Constants.URL.SECRET + token_secret + Constants.URL.CONSUMERKEY + consumer_key + Constants.URL.CONSUMERSECRET + consumer_secret, TwitterActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                progressDialog.dismiss();
                if (ERROR.isEmpty()) {
                    try {
                        Login login_obj = new Login();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String session_id = jsonObject.getString("session_id");
                        String token = jsonObject.getString("token");

                        JSONObject user_object = jsonObject.getJSONObject("user");

                        String _id = user_object.getString("_id");
                        boolean confirmed = user_object.getBoolean("confirmed");
                        String full_name = user_object.getString("full_name");
                        String gender = user_object.getString("gender");
                        boolean admin_access = user_object.getBoolean("admin_access");
                        JSONArray favouriteImages = user_object.getJSONArray("favourite_images");
                        int gratitude_count = user_object.getInt("gratitude_count");
                        int manifestation_count = user_object.getInt("manifestation_count");
                        boolean subscribed_from_android = user_object.getBoolean("subscribed_from_android");
                        boolean subscribed_from_ios = user_object.getBoolean("subscribed_from_ios");
                        String login_type = user_object.getString("login_type");


                        favouriteImages_arr = new ArrayList<>();

                        for (int a = 0; a < favouriteImages.length(); a++) {
                            Login.Favourite_images favourite_images = new Login.Favourite_images();
                            JSONObject favourite_obj = favouriteImages.getJSONObject(a);
                            favourite_images.set_id(favourite_obj.getString("_id"));
                            favourite_images.setTitle(favourite_obj.getString("title"));
                            favourite_images.setImage_id(favourite_obj.getString("image_id"));
                            favourite_images.setDetailed_img_name(favourite_obj.getString("Detailed_img_name"));
                            favourite_images.setList_img(favourite_obj.getString("list_img"));
                            favourite_images.setOrder_number(favourite_obj.getInt("order_number"));
                            favourite_images.setTf(true);
                            favouriteImages_arr.add(favourite_images);
//                            favorites_id = favouriteImages_arr.get(a).get_id();
                        }

                        login_obj.set_id(_id);
                        login_obj.setConfirmed(confirmed);
                        login_obj.setFull_name(full_name);
                        login_obj.setGender(gender);
                        login_obj.setAdmin_access(admin_access);
                        login_obj.setFavourite_images(favouriteImages_arr);
                        login_obj.setManifestation_count(manifestation_count);
                        login_obj.setGratitude_count(gratitude_count);
                        login_obj.setSubscribed_from_android(subscribed_from_android);
                        login_obj.setSubscribed_from_ios(subscribed_from_ios);
                        login_obj.setLogin_type(login_type);

                        // saving email and password in shared pref......

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_TOKEN", token);
                        mEditor.putString("USER_ID", _id);
                        mEditor.putString("NAME", full_name);
                        mEditor.putBoolean("SUBSCRIBED", subscribed_from_android);
                        mEditor.putString("LOGIN_TYPE", login_type);
                        mEditor.putString("LOGIN", "social");
                        mEditor.putString("SESSION_ID", session_id);

                        String favoriteList = gson.toJson(favouriteImages_arr);
                        mEditor.putString("FAVORITELIST", favoriteList);
                        mEditor.apply();

                        sp.edit().putBoolean("logged", true).apply();

                        // saving email and password in shared pref......

                        Intent intt = new Intent(TwitterActivity.this, HomePage.class);
                        intt.putExtra("fromNotification", false);
                        startActivity(intt);
                        finish();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "error in api", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }


}
