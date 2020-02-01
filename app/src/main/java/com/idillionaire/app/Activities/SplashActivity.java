package com.idillionaire.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.util.BillingHelper;
import com.android.vending.billing.IInAppBillingService;
import com.android.volley.Request;
import com.crashlytics.android.Crashlytics;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.BillingManager.BillingManager;
import com.idillionaire.app.GoogleAnalytics.AnalyticsTrackers;
import com.idillionaire.app.Models.AppPrefModel;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import io.fabric.sdk.android.Fabric;

public class SplashActivity extends AppCompatActivity implements PurchasesUpdatedListener {

    SharedPreferences sp;
    List<AppPrefModel> AppPrefs_Array;
    public static String termstoUse;
    public static String privacyPolicy;
    public static String bookclub;
    public static String fblink;
    public static String twitterlink;
    public static String instalink;
    public static String homebg;
    public static String p_getstarted_bg;
    public static String p_signup_bg;
    public static String p_login_bg;

    BillingClient billingClient;

    ImageView splash_image;

    String purchaseToken;

    static final Logger log = Logger.getLogger(SplashActivity.class.getName());

    private static String refreshToken;
    private static String accessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_activity);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        splash_image = findViewById(R.id.splash_image);

        apiAppPreferences();

//        SharedPreferences sharedPreferences = getSharedPreferences("SPLASH", MODE_PRIVATE);
//        String splash_img = sharedPreferences.getString("splash", "");

//        Picasso.with(SplashActivity.this)
//                .load(Constants.URL.BASE_URL + splash_img)
//                .into(splash_image);

        splash_image.setImageResource(R.drawable.splash);

        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(6000);

                    sp = getSharedPreferences("login", MODE_PRIVATE);
                    sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                    String token = sp.getString("USER_TOKEN", "");
                    boolean tf = sp.getBoolean("logged", false);
                    if (!token.isEmpty()) {
                        Intent intent = new Intent(SplashActivity.this, HomePage.class);
                        intent.putExtra("fromNotification", false);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, GetStarted.class);
                        startActivity(intent);
                        finish();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();

        sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchaseToken = sp.getString("PURCHASETOKEN", "");

        billingClient = BillingClient.newBuilder(this).setListener(this).build();

        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(int responseCode) {
                //TODO: use this for stuff
                com.android.billingclient.api.Purchase.PurchasesResult result;
                result = billingClient.queryPurchases(BillingClient.SkuType.SUBS);
                List<Purchase> resultList = result.getPurchasesList();

                if (resultList.isEmpty()){
                    sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                    sp.edit().putString("PURCHASETOKEN", "").apply();
                    sp = getSharedPreferences("USER_DATA", MODE_PRIVATE);
                    String email = sp.getString("USER_EMAIL", "");
                    if (email.equals("admin@idil.com")){
                        sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                        sp.edit().putString("PURCHASETOKEN", "subscribed user").apply();
                    }

                }else {
                    String token = resultList.get(0).getPurchaseToken();
                    sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                    sp.edit().putString("PURCHASETOKEN", token).apply();
                }

            }

            @Override
            public void onBillingServiceDisconnected() {
                //TODO: use this for stuff
//                Timber.d("something went wrong ");
                Toast.makeText(SplashActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void apiAppPreferences() {
//        SharedPreferences mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
//        String USER_TOKEN = mprefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GETAPPPREFERENCES, SplashActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        JSONArray app_pref = jsonObject.getJSONArray("app_pref");
                        AppPrefs_Array = new ArrayList<>();
                        for (int i = 0; i < app_pref.length(); i++) {
                            JSONObject app_prefObj = app_pref.getJSONObject(i);
                            AppPrefModel appPrefModel = new AppPrefModel();

                            String _id = app_prefObj.getString("_id");
                            String text = app_prefObj.getString("text");
                            String color_name = app_prefObj.getString("color_name");
                            String privacy_policy = app_prefObj.getString("privacy_policy");
                            String terms_cons = app_prefObj.getString("terms_cons");
                            String fb_link = app_prefObj.getString("fb_link");
                            String twitter_link = app_prefObj.getString("twitter_link");
                            String instagram_link = app_prefObj.getString("instagram_link");
                            String login_bg = app_prefObj.getString("login_bg");
                            String signup_bg = app_prefObj.getString("signup_bg");
                            String splash_bg = app_prefObj.getString("splash_bg");
                            String home_bg = app_prefObj.getString("home_bg");
                            String getStarted_bg = app_prefObj.getString("getStarted_bg");
                            String bookClub_bg = app_prefObj.getString("bookClub_bg");

                            appPrefModel.set_id(_id);
                            appPrefModel.setText(text);
                            appPrefModel.setColor_name(color_name);
                            appPrefModel.setPrivacy_policy(privacy_policy);
                            appPrefModel.setTerms_cons(terms_cons);
                            appPrefModel.setFb_link(fb_link);
                            appPrefModel.setTwitter_link(twitter_link);
                            appPrefModel.setInstagram_link(instagram_link);
                            appPrefModel.setLogin_bg(login_bg);
                            appPrefModel.setSignup_bg(signup_bg);
                            appPrefModel.setSplash_bg(splash_bg);
                            appPrefModel.setHome_bg(home_bg);
                            appPrefModel.setGetStarted_bg(getStarted_bg);
                            appPrefModel.setBookClub_bg(bookClub_bg);

//                            SharedPreferences sp = getSharedPreferences("SPLASH", MODE_PRIVATE);
//                            sp.edit().putString("splash", splash_bg).apply();

                            AppPrefs_Array.add(appPrefModel);
                            termstoUse = appPrefModel.getTerms_cons();
                            privacyPolicy = appPrefModel.getPrivacy_policy();
                            bookclub = appPrefModel.getBookClub_bg();
                            fblink = appPrefModel.getFb_link();
                            twitterlink = appPrefModel.getTwitter_link();
                            instalink = appPrefModel.getInstagram_link();
                            homebg = appPrefModel.getHome_bg();
                            p_getstarted_bg = appPrefModel.getGetStarted_bg();
                            p_signup_bg = appPrefModel.getSignup_bg();
                            p_login_bg = appPrefModel.getLogin_bg();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
//                    Toast.makeText(SplashActivity.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
    }
}
