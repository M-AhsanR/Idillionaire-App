package com.idillionaire.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.idillionaire.app.Adapters.FavrtGrideMAdapter;
import com.idillionaire.app.Adapters.SlidingImage_Adapter;
import com.idillionaire.app.BillingManager.BillingManager;
import com.idillionaire.app.Classes.ImageModel;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.R;
import com.viewpagerindicator.CirclePageIndicator;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static io.fabric.sdk.android.services.concurrency.AsyncTask.init;

public class PremiumActivity extends AppCompatActivity {

    TextView subscription_btn;
    BillingClient mBillingClient;
    BillingManager billingManager;
    RelativeLayout back_home;
    Bundle bundle;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.premium_1st, R.drawable.premium_2nd,
            R.drawable.premium_3rd,R.drawable.premium_4th, R.drawable.premium_5th};

    String activity_name = "";

    Gson gson;
    ArrayList<MediaFiles> storyMediaImagesList;
    public static String main_id;
    public static String schedule_gallery_name;
    public static String schedule_detail_image;
    public static String schedule_list_image;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        activity_name = "";
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // Initializing

        bundle = getIntent().getExtras();
        activity_name = bundle.getString("premium");

        subscription_btn = findViewById(R.id.subscription_btn);
        billingManager = new BillingManager(PremiumActivity.this, new MyBillingUpdateListener());
        back_home = findViewById(R.id.rel_back);

        // Initializing

        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        subscription_btn.setVisibility(View.GONE);

        init();

        //  On click

        back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity_name = "";
                finish();
            }
        });
        subscription_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                billingManager.initiatePurchaseFlow("idillionaire_purchases", null, BillingClient.SkuType.SUBS);
            }
        });




    }

    private ArrayList<ImageModel> populateList(){

        ArrayList<ImageModel> list = new ArrayList<>();

        for(int i = 0; i < 5; i++){
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);

        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(PremiumActivity.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == NUM_PAGES) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 3000, 3000);
        // Auto start of viewpager

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                if (position == myImageList.length - 1){
                    subscription_btn.setVisibility(View.VISIBLE);
                }else {
                    subscription_btn.setVisibility(View.GONE);
                }

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }

    class MyBillingUpdateListener implements BillingManager.BillingUpdatesListener {
        @Override
        public void onBillingClientSetupFinished() {
//            BillingClass();
        }

        @Override
        public void onConsumeFinished(String token, int result) {

            if (result == BillingClient.BillingResponse.OK) {
                Toast.makeText(PremiumActivity.this, "if part on Consume Finished", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(PremiumActivity.this, "else part on Consume Finished", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onPurchasesUpdated(List<Purchase> purchases) {

            for (Purchase p : purchases) {

                String token = p.getPurchaseToken();
                String orderId = p.getOrderId();
                String packagename = p.getPackageName();
                String signature = p.getSignature();
                String sku = p.getSku();
                boolean autoRenewing = p.isAutoRenewing();
                String purchasetime = String.valueOf(p.getPurchaseTime());
                String json = p.getOriginalJson();


                Log.e("TOKEN", token);
                Log.e("ORDERID", orderId);
                Log.e("PACKAGENAME", packagename);
                Log.e("signature", signature);
                Log.e("sku", sku);
                Log.e("autoRenewing", String.valueOf(autoRenewing));
                Log.e("purchasetime", purchasetime);
                Log.e("json", json);

                SharedPreferences mprefs = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mprefs.edit();
                mEditor.putString("PURCHASETOKEN", token);
//                mEditor.putString("ORDERID", orderId);
//                mEditor.putString("SKU", sku);
//                mEditor.putBoolean("AUTORENEW", autoRenewing);
//                mEditor.putString("PURCHASETIME", purchasetime);
                mEditor.apply();

                //update ui
                updateUI();

            }

        }
    }



    public void updateUI(){

        if (activity_name.equals("StoryMode")) {

            // Loop for checking gallery name
            SharedPreferences mprefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            gson = new Gson();
            String galleryList = mprefs.getString("GALLERYLIST", "");
            Type type = new TypeToken<List<MediaFiles>>() {
            }.getType();
            storyMediaImagesList = gson.fromJson(galleryList, type);

//            Bundle bundle = getIntent().getExtras();
            String main_id = bundle.getString("main_id");

            for (int i = 0; i < storyMediaImagesList.size(); i++) {
                String main_image_id = storyMediaImagesList.get(i).get_id();
                if (main_id.equals(main_image_id)) {
                    schedule_gallery_name = storyMediaImagesList.get(i).getGallery_title();
                    break;
                }
            }
            // Loop for checking gallery name

            schedule_detail_image = bundle.getString("schedule_detail_image");
            schedule_list_image = bundle.getString("schedule_list_image");
            schedule_gallery_name = bundle.getString("gallery_name");
            Intent intent = new Intent(PremiumActivity.this, ScheduleActivity.class);
            intent.putExtra("activity_name", "PremiumActivity");
            intent.putExtra("main_id", main_id);
            startActivity(intent);
            activity_name = "";
            finish();

        }
        if (activity_name.equals("DetailedImage")) {
            // Loop for checking gallery name
            SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            gson = new Gson();
            String galleryList = mPrefs.getString("GALLERYLIST", "");
            Type type = new TypeToken<List<MediaFiles>>() {
            }.getType();
            storyMediaImagesList = gson.fromJson(galleryList, type);

            main_id = bundle.getString("main_id");
            for (int i = 0; i < storyMediaImagesList.size(); i++) {
                String main_image_id = storyMediaImagesList.get(i).get_id();
                if (main_id.equals(main_image_id)) {
                    schedule_gallery_name = storyMediaImagesList.get(i).getGallery_title();
                    break;
                }
            }
            // Loop for checking gallery name
            schedule_detail_image = bundle.getString("schedule_detail_image");
            schedule_list_image = bundle.getString("schedule_list_image");

            Intent intent = new Intent(PremiumActivity.this, ScheduleActivity.class);
            intent.putExtra("activity_name", "PremiumActivity");
            intent.putExtra("main_id", main_id);
            startActivity(intent);
            activity_name = "";
            finish();
        }
        if (activity_name.equals("OneFragment")) {
            Intent intent = new Intent(PremiumActivity.this, BookClub.class);
            startActivity(intent);
            activity_name = "";
            finish();
        }
        if (activity_name.equals("Homepage")) {
            activity_name = "";
            finish();
        }
    }



    /**
     * To purchase an Subscription
     */

//    private void toPurchaseSubscription() {
//        BillingFlowParams.Builder builder = BillingFlowParams.newBuilder()
//                .setSku("idillionaire_purchases").setType(BillingClient.SkuType.SUBS);
//        int responseCode = mBillingClient.launchBillingFlow(this, builder.build());
//        Toast.makeText(PremiumActivity.this, "toPurchaseSubscription Class", Toast.LENGTH_SHORT).show();
//    }

//    private void BillingClass() {
//        mBillingClient = BillingClient.newBuilder(this).setListener(new PurchasesUpdatedListener() {
//            @Override
//            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
//
//                String sdc = "sadc";
//
//                if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
//
//                    for (Purchase purchase : purchases) {
//                        //When every a new purchase is made
//
//                        Intent intent = new Intent(PremiumActivity.this, BookClub.class);
//                        startActivity(intent);
//                        finish();
//
//
//                    }
//
//                } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
//                    // Handle an error caused by a user cancelling the purchase flow.
//                    Toast.makeText(PremiumActivity.this, "App Canceled", Toast.LENGTH_SHORT).show();
//                } else {
//                    // Handle any other error codes.
//                    Toast.makeText(PremiumActivity.this, "Other Error Occured", Toast.LENGTH_SHORT).show();
//                }
//
//
//            }
//        }).build();
//
//        mBillingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
//                if (billingResponseCode == BillingClient.BillingResponse.OK) {
//                    // The billing client is ready. You can query purchases here.
//                    Toast.makeText(PremiumActivity.this, "onBillingSetupFinished override", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//                Toast.makeText(PremiumActivity.this, "onBillingServiceDisconnected Class", Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        toPurchaseSubscription();
//    }


//    private void ListSKU() {
//        List<String> skuList = new ArrayList<>();
//        skuList.add("idillionaire_purchases");
//        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//        params.setSkusList(skuList).setType(BillingClient.SkuType.SUBS);
//        mBillingClient.querySkuDetailsAsync(params.build(),
//                new SkuDetailsResponseListener() {
//                    @Override
//                    public void onSkuDetailsResponse(int responseCode, List<SkuDetails> skuDetailsList) {
//                        if (responseCode == BillingClient.BillingResponse.OK
//                                && skuDetailsList != null) {
//                            for (SkuDetails skuDetails : skuDetailsList) {
//                                String sku = skuDetails.getSku();
//                                String price = skuDetails.getPrice();
//                                Toast.makeText(PremiumActivity.this, "Idillionaire Purchases", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    }
//                });
//        BillingClass();
//    }


}
