package com.idillionaire.app.Activities;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idillionaire.app.Models.Login;
import com.idillionaire.app.TwitterActivity;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookBroadcastReceiver;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.facebook.appevents.AppEventsLogger;

import com.android.volley.Request;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Fragments.OneFragment;
import com.idillionaire.app.Models.Signup;
import com.idillionaire.app.R;
import com.facebook.login.LoginManager;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private ArrayList<Login.Favourite_images> favouriteImages_arr;


    String time_zone;
    SharedPreferences sp;

    ImageView signup_bg_img;

    FacebookBroadcastReceiver FB;

    String accessTokenFB;

    AccessTokenTracker accessTokenTracker;
    CallbackManager callbackManager;
    ProgressDialog progressDialog;

    private static final String TAG = "FB Activity";

    Gson gson = new Gson();


    EditText fname_signup, pass_signup, email_signup;
    TextView btn;
    TextView or_login, already_have, loginback;

    ImageView googlesignin, twittersignin, fbsignin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();


        setContentView(R.layout.activity_sign_up);

        btn = (TextView) findViewById(R.id.signupbtn);
        fname_signup = (EditText) findViewById(R.id.fname_signup);
        pass_signup = (EditText) findViewById(R.id.pass_signup);
        email_signup = (EditText) findViewById(R.id.email_signup);
        or_login = (TextView) findViewById(R.id.or_login);
        already_have = (TextView) findViewById(R.id.already_have);
        loginback = (TextView) findViewById(R.id.loginback);
        signup_bg_img = (ImageView) findViewById(R.id.signup_bg_img);

        googlesignin = findViewById(R.id.google_signin);
        twittersignin = findViewById(R.id.twitter_signin);
        fbsignin = findViewById(R.id.fb_signin);

        Picasso.with(this)
                .load(Constants.URL.BASE_URL + SplashActivity.p_signup_bg)
                .into(signup_bg_img);

        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        btn.setTypeface(font);
        fname_signup.setTypeface(font);
        pass_signup.setTypeface(font);
        email_signup.setTypeface(font);
        or_login.setTypeface(font);
        already_have.setTypeface(font);
        loginback.setTypeface(font);

        setupUI(findViewById(R.id.signup_main_parent));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiSignup();
            }
        });

        time_zone = TimeZone.getDefault().getID();

        sp = getSharedPreferences("login", MODE_PRIVATE);

        googlesignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, GoogleSignInActivity.class);
                intent.putExtra("google", "signup");
                startActivity(intent);
                finish();
            }
        });

        twittersignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, TwitterActivity.class);
                intent.putExtra("google", "signup");
                startActivity(intent);
                finish();
            }
        });

        // fb

        CallbackManager callbackManager = CallbackManager.Factory.create();

        fbsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(SignupActivity.this, Arrays.asList("public_profile"));

            }
        });

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        // fb


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        /** This block obtains Facebook UserID and Token */
        com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();
        if (token != null) {
//            Toast.makeText(this, token.getToken(), Toast.LENGTH_SHORT).show();
            accessTokenFB = token.getToken();

            apiFBlogin();
//            apiFBlogin();


            Log.e(TAG, "Token: " + token.getToken());
            Log.e(TAG, "UserID: " + token.getUserId());
        }
        /***/


    }

    private void apiFBlogin() {

        progressDialog = new ProgressDialog(SignupActivity.this);
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
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGINFB + accessTokenFB, SignupActivity.this, postParam, headers, new ServerCallback() {
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

                        Intent intt = new Intent(SignupActivity.this, HomePage.class);
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


    private void apiSignup() {

        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refresh_token = mPrefs_token.getString("FIREBASE_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("email", email_signup.getText().toString());
        postParam.put("full_name", fname_signup.getText().toString());
        postParam.put("password", pass_signup.getText().toString());
        postParam.put("device_token", refresh_token);
        postParam.put("platform", "android");
        postParam.put("time_zone", time_zone);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.REGISTER, SignupActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                progressDialog.dismiss();
                if (ERROR.isEmpty()) {
                    try {
                        Login signup_obj = new Login();
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String session_id = jsonObject.getString("session_id");
                        String token = jsonObject.getString("token");

                        JSONObject user_object = jsonObject.getJSONObject("user");

                        String _id = user_object.getString("_id");
                        Boolean confirmed = user_object.getBoolean("confirmed");
                        String full_name = user_object.getString("full_name");
                        String gender = user_object.getString("gender");
                        Boolean admin_access = user_object.getBoolean("admin_access");
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
                        }

                        signup_obj.set_id(_id);
                        signup_obj.setConfirmed(confirmed);
                        signup_obj.setFull_name(full_name);
                        signup_obj.setGender(gender);
                        signup_obj.setAdmin_access(admin_access);
                        signup_obj.setFavourite_images(favouriteImages_arr);
                        signup_obj.setManifestation_count(manifestation_count);
                        signup_obj.setGratitude_count(gratitude_count);
                        signup_obj.setSubscribed_from_android(subscribed_from_android);
                        signup_obj.setSubscribed_from_ios(subscribed_from_ios);
                        signup_obj.setLogin_type(login_type);

                        SharedPreferences mprefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mprefs.edit();
                        mEditor.putString("USER_EMAIL", email_signup.getText().toString());
                        mEditor.putString("NAME", full_name);
                        mEditor.putString("USER_PASSWORD", pass_signup.getText().toString());
                        mEditor.putString("USER_TOKEN", token);
                        mEditor.putString("USER_ID", _id);
                        mEditor.putBoolean("SUBSCRIBED", subscribed_from_android);
                        mEditor.putString("LOGIN_TYPE", login_type);
                        mEditor.putString("LOGIN", "normal");
                        mEditor.putString("SESSION_ID", session_id);

                        String favoriteList = gson.toJson(favouriteImages_arr);
                        mEditor.putString("FAVORITELIST", favoriteList);
                        mEditor.apply();

                        sp.edit().putBoolean("logged", true).apply();

                        Intent intt = new Intent(SignupActivity.this, HomePage.class);
                        intt.putExtra("fromNotification", false);
                        startActivity(intt);
                        finish();


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {

                    progressDialog.dismiss();

                    if (fname_signup.getText().toString().isEmpty() || pass_signup.getText().toString().isEmpty() || email_signup.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Empty Fields", Toast.LENGTH_SHORT).show();
                        pass_signup.setText("");
                    } else if (!isEmailValid(email_signup.getText().toString())) {
                        Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
                        pass_signup.setText("");
                    } else if (pass_signup.getText().toString().length() < 6) {
                        Toast.makeText(getApplicationContext(), "Password " + "(" + pass_signup.getText().toString() + ")" + " is shorter than the minimum allowed length (6)", Toast.LENGTH_LONG).show();
                        pass_signup.setText("");
                    } else {
                        Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                        pass_signup.setText("");
                    }
                }
            }
        });
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public void onLoginbackClick(View view) {

        Intent intentloginback = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intentloginback);
        finish();
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SignupActivity.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }
}
