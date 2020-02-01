package com.idillionaire.app.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.R;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
import java.util.jar.Attributes;

import static com.facebook.internal.CallbackManagerImpl.RequestCodeOffset.Login;

public class GoogleSignInActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "IdTokenActivity";
    private static final int RC_GET_TOKEN = 9002;

    private GoogleSignInClient mGoogleSignInClient;
    private TextView mIdTokenTextView;
    private Button mRefreshButton;
    String idToken;
    String name;
    String fname;
    String lname;
    Bundle bundle;
    String intent_extra;

    String time_zone;
    ProgressDialog progressDialog;

    Gson gson = new Gson();
    SharedPreferences sp;
    private ArrayList<Login.Favourite_images> favouriteImages_arr;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (intent_extra.equals("login")) {
            Intent intent = new Intent(GoogleSignInActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(GoogleSignInActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);

        // Views
        mIdTokenTextView = findViewById(R.id.detail);
        mRefreshButton = findViewById(R.id.button_optional_action);
        mRefreshButton.setText(R.string.refresh_token);

        time_zone = TimeZone.getDefault().getID();

        // Shared Preferences
        sp = getSharedPreferences("login", MODE_PRIVATE);


        // Button click listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.disconnect_button).setOnClickListener(this);
        mRefreshButton.setOnClickListener(this);

        // For sample only: make sure there is a valid server client ID.
        validateServerClientID();

        bundle = getIntent().getExtras();
        intent_extra = bundle.getString("google");


        // [START configure_signin]
        // Request only the user's ID token, which can be used to identify the
        // user securely to your backend. This will contain the user's basic
        // profile (name, profile picture URL, etc) so you should not need to
        // make an additional call to personalize your application.
        String client_id = getString(R.string.server_client_id);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(client_id)
                .requestEmail()
                .build();
        // [END configure_signin]

        // Build GoogleAPIClient with the Google Sign-In API and the above options.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    private void getIdToken() {
        // Show an account picker to let the user choose a Google account from the device.
        // If the GoogleSignInOptions only asks for IDToken and/or profile and/or email then no
        // consent screen will be shown here.
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_GET_TOKEN);
    }

    private void refreshIdToken() {
        // Attempt to silently refresh the GoogleSignInAccount. If the GoogleSignInAccount
        // already has a valid token this method may complete immediately.
        //
        // If the user has not previously signed in on this device or the sign-in has expired,
        // this asynchronous branch will attempt to sign in the user silently and get a valid
        // ID token. Cross-device single sign on will occur in this branch.
        mGoogleSignInClient.silentSignIn()
                .addOnCompleteListener(this, new OnCompleteListener<GoogleSignInAccount>() {
                    @Override
                    public void onComplete(@NonNull Task<GoogleSignInAccount> task) {
                        handleSignInResult(task);
                    }
                });
    }

    // [START handle_sign_in_result]
    private void handleSignInResult(@NonNull Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            idToken = account.getIdToken();
            fname = account.getGivenName();
            lname = account.getFamilyName();

            Log.e("fname", fname);
            Log.e("lname", lname);
            Log.e("Token", idToken);


            apiGoogleLogin();
            // TODO(developer): send ID Token to server and validate

            updateUI(account);
        } catch (ApiException e) {
            Log.w(TAG, "handleSignInResult:error", e);

            updateUI(null);
        }
    }
// [END handle_sign_in_result]

    private void signOut() {
        mGoogleSignInClient.signOut().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateUI(null);
            }
        });
    }

    private void revokeAccess() {
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_GET_TOKEN) {
            // [START get_id_token]
            // This task is always completed immediately, there is no need to attach an
            // asynchronous listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);


            // [END get_id_token]
        }
    }

    private void updateUI(@Nullable GoogleSignInAccount account) {
        if (account != null) {
            ((TextView) findViewById(R.id.status)).setText(R.string.signed_in);

            String idToken = account.getIdToken();
            mIdTokenTextView.setText(getString(R.string.id_token_fmt, idToken));

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            mRefreshButton.setVisibility(View.GONE);
        } else {
            ((TextView) findViewById(R.id.status)).setText(R.string.signed_out);
            mIdTokenTextView.setText(getString(R.string.id_token_fmt, "null"));

            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
            mRefreshButton.setVisibility(View.GONE);
        }
    }

    /**
     * Validates that there is a reasonable server client ID in strings.xml, this is only needed
     * to make sure users of this sample follow the README.
     */
    private void validateServerClientID() {
        String serverClientId = getString(R.string.server_client_id);
        String suffix = ".apps.googleusercontent.com";
        if (!serverClientId.trim().endsWith(suffix)) {
            String message = "Invalid server client ID in strings.xml, must end with " + suffix;

            Log.w(TAG, message);
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_in_button:
                getIdToken();
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.disconnect_button:
                revokeAccess();
                break;
            case R.id.button_optional_action:
                refreshIdToken();
                break;
        }
    }


    private void apiGoogleLogin() {

        progressDialog = new ProgressDialog(GoogleSignInActivity.this);
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
        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.GOOGLELOGIN + idToken + Constants.URL.FNAME + fname + Constants.URL.LNAME + lname, GoogleSignInActivity.this, postParam, headers, new ServerCallback() {
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
                        mEditor.putString("LOGIN", "social");
                        mEditor.putBoolean("SUBSCRIBED", subscribed_from_android);
                        mEditor.putString("LOGIN_TYPE", login_type);
                        mEditor.putString("SESSION_ID", session_id);

                        String favoriteList = gson.toJson(favouriteImages_arr);
                        mEditor.putString("FAVORITELIST", favoriteList);
                        mEditor.apply();

                        sp.edit().putBoolean("logged", true).apply();

                        // saving email and password in shared pref......

                        Intent intt = new Intent(GoogleSignInActivity.this, HomePage.class);
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

