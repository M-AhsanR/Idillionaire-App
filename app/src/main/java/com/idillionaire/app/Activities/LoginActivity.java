package com.idillionaire.app.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idillionaire.app.AlertDialogManager;
import com.idillionaire.app.ConnectionDetector;
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
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.R;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class LoginActivity extends AppCompatActivity {
    private static Button LogInBtn;

    // Google

    ImageView google_login;
    FacebookBroadcastReceiver FB;

    String accessTokenFB;

    private static final String TAG = "FB Activity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private GoogleSignInClient mGoogleSignInClient;


    // Google

    // TwitterActivity

    ImageView twitter_login;

    // TwitterActivity

    TextView btn;
    private EditText email_login;
    private EditText pass_login;
    TextView forgot_txt;
    TextView or_login;
    TextView dnt_have;
    TextView signup_change;
    Gson gson = new Gson();
    private TextView signup;
    SharedPreferences sp;
    String time_zone;
    ProgressDialog progressDialog;

    ImageView fb_login, login_bg_img;

    AccessTokenTracker accessTokenTracker;
    CallbackManager callbackManager;

    //    public static String favorites_id;
    private ArrayList<Login.Favourite_images> favouriteImages_arr;

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        AppEventsLogger.activateApp(this);

        email_login = (EditText) findViewById(R.id.email_login);
        pass_login = (EditText) findViewById(R.id.pass_login);
        forgot_txt = (TextView) findViewById(R.id.forgot_txt);
        or_login = (TextView) findViewById(R.id.or_login);
        dnt_have = (TextView) findViewById(R.id.dnt_have);
        btn = (TextView) findViewById(R.id.LogInBtn);
        login_bg_img = (ImageView) findViewById(R.id.login_bg_img);
        signup_change = (TextView) findViewById(R.id.signup_change);

        Picasso.with(this)
                .load(Constants.URL.BASE_URL + SplashActivity.p_login_bg)
                .into(login_bg_img);

        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        email_login.setTypeface(font);
        pass_login.setTypeface(font);
        forgot_txt.setTypeface(font);
        or_login.setTypeface(font);
        dnt_have.setTypeface(font);
        btn.setTypeface(font);
        signup_change.setTypeface(font);


        com.facebook.AccessToken accessToken = com.facebook.AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();


        // Google

        google_login = findViewById(R.id.google_login);
        google_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GoogleSignInActivity.class);
                intent.putExtra("google", "login");
                startActivity(intent);
                finish();
            }
        });

        // Google

        // TwitterActivity

        twitter_login = findViewById(R.id.twitter_login);
        twitter_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, TwitterActivity.class);
                intent.putExtra("google", "login");
                startActivity(intent);
                finish();
            }
        });

        // TwitterActivity

        // FaceBook

        CallbackManager callbackManager = CallbackManager.Factory.create();

        fb_login = findViewById(R.id.fb_login);
        fb_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile"));
            }
        });

        FacebookSdk.setIsDebugEnabled(true);
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        // FaceBook

        sp = getSharedPreferences("login", MODE_PRIVATE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                apiLogin();
            }
        });

        //for hide keyboard
        setupUI(findViewById(R.id.main_parent));

        // time zone
        time_zone = TimeZone.getDefault().getID();

        // Forgot Password

        forgot_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                LayoutInflater inflater = LoginActivity.this.getLayoutInflater();
                View dailogview = inflater.inflate(R.layout.forgot_password, null);
                builder.setView(dailogview);

                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                final EditText forgot_pass_email = dailogview.findViewById(R.id.forgot_pass_email);
                TextView forgot_send = dailogview.findViewById(R.id.forgot_send);
                TextView forgot_cancle = dailogview.findViewById(R.id.forgot_cancle);
                alertDialog.show();

                forgot_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String email = forgot_pass_email.getText().toString();

                        Map<String, String> postParam = new HashMap<String, String>();
                        postParam.put("email", forgot_pass_email.getText().toString());

                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json");

                        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.FORGOTPASS, LoginActivity.this, postParam, headers, new ServerCallback() {


                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {
                                if (ERROR.isEmpty()) {
                                    try {
                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                        int code = jsonObject.getInt("code");
                                        String message = jsonObject.getString("message");

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Toast.makeText(getApplicationContext(), "Email has been sent", Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                    AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
                                    LayoutInflater inflater1 = LoginActivity.this.getLayoutInflater();
                                    View dailogview1 = inflater1.inflate(R.layout.reset_password_pin, null);
                                    builder1.setView(dailogview1);

                                    final AlertDialog alertDialog1 = builder1.create();
                                    alertDialog1.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                                    final EditText reset_pass_email = dailogview1.findViewById(R.id.reset_pass_email);
                                    final EditText reset_pass_pin = dailogview1.findViewById(R.id.reset_pass_pin);
                                    final EditText reset_pass = dailogview1.findViewById(R.id.reset_pass);
                                    final EditText reset_confirmpass = dailogview1.findViewById(R.id.reset_confirmpass);
                                    TextView reset_ok = dailogview1.findViewById(R.id.reset_ok);
                                    TextView reset_cancel = dailogview1.findViewById(R.id.reset_cancel);
                                    alertDialog1.show();

                                    reset_pass_email.setText(email);

                                    reset_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            alertDialog1.dismiss();
                                        }
                                    });

                                    reset_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (reset_pass.getText().toString().equals(reset_confirmpass.getText().toString())) {

                                                SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE);
                                                String refresh_token = mPrefs_token.getString("FIREBASE_TOKEN", "");

                                                Map<String, String> postParam = new HashMap<String, String>();
                                                postParam.put("email", email);
                                                postParam.put("password", reset_pass.getText().toString());
                                                postParam.put("pin", reset_pass_pin.getText().toString());
                                                postParam.put("device_token", refresh_token);
                                                postParam.put("platform", "android");
                                                postParam.put("time_zone", String.valueOf(time_zone));

                                                HashMap<String, String> headers = new HashMap<String, String>();
                                                headers.put("Content-Type", "application/json");

                                                ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.RESETPASS, LoginActivity.this, postParam, headers, new ServerCallback() {
                                                    @Override
                                                    public void onSuccess(JSONObject result, String ERROR) {

                                                        if (ERROR.isEmpty()) {

                                                            try {
                                                                JSONObject jsonObject = new JSONObject(String.valueOf(result));
                                                                Login login_object = new Login();
                                                                int code = jsonObject.getInt("code");
                                                                String session_id = jsonObject.getString("session_id");
                                                                String message = jsonObject.getString("message");
                                                                String token = jsonObject.getString("token");
                                                                JSONObject user = jsonObject.getJSONObject("user");

                                                                String _id = user.getString("_id");
                                                                Boolean confirmed = user.getBoolean("confirmed");
                                                                String full_name = user.getString("full_name");
                                                                String gender = user.getString("gender");
                                                                Boolean admin_access = user.getBoolean("admin_access");
                                                                int gratitude_count = user.getInt("gratitude_count");
                                                                int manifestation_count = user.getInt("manifestation_count");
                                                                boolean subscribed_from_android = user.getBoolean("subscribed_from_android");
                                                                boolean subscribed_from_ios = user.getBoolean("subscribed_from_ios");
                                                                String login_type = user.getString("login_type");
                                                                JSONArray favourite_images = user.getJSONArray("favourite_images");
                                                                ArrayList<Login.Favourite_images> reset_fvrt_images = new ArrayList<>();
                                                                for (int i = 0; i < favourite_images.length(); i++) {
                                                                    Login.Favourite_images fvrt_images = new Login.Favourite_images();
                                                                    JSONObject fvrtObj = favourite_images.getJSONObject(i);
                                                                    fvrt_images.set_id(fvrtObj.getString("_id"));
                                                                    fvrt_images.setTitle(fvrtObj.getString("title"));
                                                                    fvrt_images.setImage_id(fvrtObj.getString("image_id"));
                                                                    fvrt_images.setDetailed_img_name(fvrtObj.getString("Detailed_img_name"));
                                                                    fvrt_images.setList_img(fvrtObj.getString("list_img"));
                                                                    fvrt_images.setOrder_number(fvrtObj.getInt("order_number"));
                                                                    reset_fvrt_images.add(fvrt_images);
                                                                }
                                                                login_object.set_id(_id);
                                                                login_object.setConfirmed(confirmed);
                                                                login_object.setFull_name(full_name);
                                                                login_object.setGender(gender);
                                                                login_object.setAdmin_access(admin_access);
                                                                login_object.setFavourite_images(reset_fvrt_images);
                                                                login_object.setManifestation_count(manifestation_count);
                                                                login_object.setGratitude_count(gratitude_count);
                                                                login_object.setSubscribed_from_android(subscribed_from_android);
                                                                login_object.setSubscribed_from_ios(subscribed_from_ios);
                                                                login_object.setLogin_type(login_type);

                                                                SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                                                mEditor.putString("USER_EMAIL", email_login.getText().toString());
                                                                mEditor.putString("USER_PASSWORD", pass_login.getText().toString());
                                                                mEditor.putString("USER_TOKEN", token);
                                                                mEditor.putString("USER_ID", _id);
                                                                mEditor.putString("NAME", full_name);
                                                                mEditor.putBoolean("SUBSCRIBED", subscribed_from_android);
                                                                mEditor.putString("LOGIN_TYPE", login_type);
                                                                mEditor.putString("LOGIN", "normal");
                                                                mEditor.putString("SESSION_ID", session_id);

                                                                String favoriteList = gson.toJson(favouriteImages_arr);
                                                                mEditor.putString("FAVORITELIST", favoriteList);
                                                                mEditor.apply();

                                                                sp.edit().putBoolean("logged", true).apply();

                                                                // saving email and password in shared pref......

                                                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                                                                Intent intt = new Intent(LoginActivity.this, HomePage.class);
                                                                intt.putExtra("fromNotification", false);
                                                                startActivity(intt);
                                                                finish();

                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        } else {
                                                            Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } else {
                                                Toast.makeText(getApplicationContext(), "Passwords are not same", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                } else {
                                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                forgot_cancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });

        // Forgot Password

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        /** This block obtains Facebook UserID and Token */
        com.facebook.AccessToken token = com.facebook.AccessToken.getCurrentAccessToken();
        if (token != null) {
            Toast.makeText(this, token.getToken(), Toast.LENGTH_SHORT).show();
            accessTokenFB = token.getToken();

            apiFBlogin();


            Log.e(TAG, "Token: " + token.getToken());
            Log.e(TAG, "UserID: " + token.getUserId());
        }
        /***/


    }

    private void apiFBlogin() {

        progressDialog = new ProgressDialog(LoginActivity.this);
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
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGINFB + accessTokenFB, LoginActivity.this, postParam, headers, new ServerCallback() {
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

                        Intent intt = new Intent(LoginActivity.this, HomePage.class);
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


    private void apiLogin() {
        // hit api............

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE);
        String refresh_token = mPrefs_token.getString("FIREBASE_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("email", email_login.getText().toString());
        postParam.put("password", pass_login.getText().toString());
        postParam.put("device_token", refresh_token);
        postParam.put("platform", "android");
        postParam.put("time_zone", String.valueOf(time_zone));

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.LOGIN, LoginActivity.this, postParam, headers, new ServerCallback() {
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
                        mEditor.putString("USER_EMAIL", email_login.getText().toString());
                        mEditor.putString("USER_PASSWORD", pass_login.getText().toString());
                        mEditor.putString("USER_TOKEN", token);
                        mEditor.putString("USER_ID", _id);
                        mEditor.putString("NAME", full_name);
                        mEditor.putBoolean("SUBSCRIBED", subscribed_from_android);
                        mEditor.putString("LOGIN_TYPE", login_type);
                        mEditor.putString("LOGIN", "normal");
                        mEditor.putString("SESSION_ID", session_id);

                        String favoriteList = gson.toJson(favouriteImages_arr);
                        mEditor.putString("FAVORITELIST", favoriteList);
                        mEditor.apply();

                        sp.edit().putBoolean("logged", true).apply();

                        // For Admin User

                        if (email_login.getText().toString().equals("admin@idil.com")){
                            sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                            sp.edit().putString("PURCHASETOKEN", "subscribed user").apply();
                        }

                        // For Admin User

                        // saving email and password in shared pref......

                        Intent intt = new Intent(LoginActivity.this, HomePage.class);
                        intt.putExtra("fromNotification", false);
                        startActivity(intt);
                        finish();
//                        ringProgressDialog.dismiss();
                    } catch (JSONException e) {
                        progressDialog.dismiss();
                        e.printStackTrace();
                    }
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                    pass_login.setText("");
                }
            }
        });
    }

    public void OnsignupButtonClick(View view) {

        Intent intentSignUp = new Intent(getApplicationContext(), SignupActivity.class);
        startActivity(intentSignUp);
        finish();
    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(LoginActivity.this);
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
