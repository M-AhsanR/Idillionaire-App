package com.idillionaire.app.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.vending.billing.IInAppBillingService;
import com.android.volley.Request;
import com.google.android.gms.common.api.ResultCallbacks;
import com.idillionaire.app.Activities.BookClub;
import com.idillionaire.app.Activities.GetStarted;
import com.idillionaire.app.Activities.LoginActivity;
import com.idillionaire.app.Activities.PremiumActivity;
import com.idillionaire.app.Activities.SplashActivity;
import com.idillionaire.app.Adapters.HomeViewAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.BillingManager.BillingManager;
import com.idillionaire.app.Models.Categories;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLTransactionRollbackException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class OneFragment extends Fragment{

    private RecyclerView recyclerview;
    ScrollView scrollView;

    BillingClient mBillingClient;
    BillingManager billingManager;

    private HomeViewAdapter homeViewAdapter;
    private ArrayList<Galleries> galleriesList;
    private ArrayList<MediaFiles> media_files_arr;
    private ArrayList<MediaFiles> array_for_shared;
    private ArrayList<Categories> homeItemsList;

    public static String home_bg;

    String activity_name;

    BillingClient billingClient;
    IInAppBillingService service;

    ImageView booklet_image;
    TextView booklet_text;
    RelativeLayout book_club_rel, home_progress_bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_one, container, false);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        activity_name = "OneFragment";

        booklet_image = view.findViewById(R.id.booklet_image);
        booklet_text = view.findViewById(R.id.booklet_text);
        scrollView = view.findViewById(R.id.scrollbar);
        recyclerview = view.findViewById(R.id.home_recyclerview);
        book_club_rel = view.findViewById(R.id.book_club_rel);
        home_progress_bar = view.findViewById(R.id.home_progress_bar);

        book_club_rel.setVisibility(View.GONE);
        home_progress_bar.setVisibility(View.VISIBLE);

        if (SplashActivity.bookclub != null){
            Picasso.with(getContext())
                    .load(Constants.URL.BASE_URL + SplashActivity.bookclub)
                    .into(booklet_image);
        }


        booklet_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                String purchase_token = sp.getString("PURCHASETOKEN", "");

                if(!purchase_token.isEmpty()){
                    Intent intent = new Intent(getContext(), BookClub.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(getContext(), PremiumActivity.class);
                    intent.putExtra("premium", activity_name);
                    startActivity(intent);
                }
            }
        });

        recyclerview.setNestedScrollingEnabled(false);
//        scrollView.setSmoothScrollingEnabled(true);

        apiHome();

        return view;

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                apiHome();
                getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            catch (Exception e){

            }
        }
    }

    private void apiHome() {

        final SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.HOME, getContext(),postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()){
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");

                        JSONArray categories = jsonObject.getJSONArray("categories");

                        homeItemsList = new ArrayList<>();
                        array_for_shared = new ArrayList<>();
                        for (int i = 0; i < categories.length(); i++){
                            JSONObject a = categories.getJSONObject(i);
                            Categories categories_obj = new Categories();

                            String _id = a.getString("_id");
                            String title = a.getString("title");
                            Boolean is_active = a.getBoolean("is_active");
                            int order_number = a.getInt("order_number");
                            JSONArray galleries = a.getJSONArray("galleries");
                            String Detail_image =a.getString("Detail_image");
                            String list_image =a.getString("list_image");

                            galleriesList = new ArrayList<>();

                        for (int b = 0; b < galleries.length(); b++){
                            Galleries galleries_obj = new Galleries();

                            JSONObject o = galleries.getJSONObject(b);

                            int order_number_gall = o.getInt("order_number");
                            String updated_at = o.getString("updated_at");
                            String _id_galleries = o.getString("_id");
                            JSONObject gallery = o.getJSONObject("gallery");

                            JSONArray media_files = gallery.getJSONArray("media_files");
                            String _id_gallery = gallery.getString("_id");
                            String title_gallery = gallery.getString("title");
                            String media_file_name = gallery.getString("media_file_name");

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
                            mediaFiles_obj.setGallery_title(title_gallery);
                            media_files_arr.add(mediaFiles_obj);
                            array_for_shared.add(mediaFiles_obj);

                            }

                            galleries_obj.setOrder_number(order_number_gall);
                            galleries_obj.setUpdated_at(updated_at);
                            galleries_obj.set_id_galleries(_id_galleries);
//                            galleries_obj.setGallery(gallery);


                            galleries_obj.set_id_gallery(_id_gallery);
                            galleries_obj.setTitle_gallery(title_gallery);
                            galleries_obj.setMedia_file_name(media_file_name);
                            galleries_obj.setMedia_files(media_files_arr);

                            galleriesList.add(galleries_obj);

                        }

                        categories_obj.set_id(_id);
                        categories_obj.setTitle(title);
                        categories_obj.setIs_active(is_active);
                        categories_obj.setOrder_number(order_number);
                        categories_obj.setGalleries(galleriesList);
                        categories_obj.setDetail_image(Detail_image);
                        categories_obj.setList_image(list_image);

                        homeItemsList.add(categories_obj);

                        }

                        String bookClub_bg = jsonObject.getString("bookClub_bg");
                        home_bg = jsonObject.getString("home_bg");

                        if (SplashActivity.bookclub == null){
                            Picasso.with(getContext())
                                    .load(Constants.URL.BASE_URL + bookClub_bg)
                                    .into(booklet_image);
                        }

                        setuprecyclerview(homeItemsList, galleriesList);

                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        Gson gson = new Gson();
                        String galleryList = gson.toJson(array_for_shared);
                        mEditor.putString("GALLERYLIST", galleryList);
                        mEditor.apply();



                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                else {
                    if (ERROR.equals("401")) {
                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.putString("USER_ID", "");
                        mEditor.apply();

                        Intent intentturn = new Intent(getContext(), GetStarted.class);
                        startActivity(intentturn);
                        getActivity().finish();
                    } else {
                        Toast.makeText(getContext(), ERROR, Toast.LENGTH_LONG).show();
                    }
                }

            }
        });

    }

    private void setuprecyclerview(ArrayList<Categories> homeItemsList, ArrayList<Galleries> galleriesList) {

        home_progress_bar.setVisibility(View.GONE);
        HomeViewAdapter homeViewAdapter = new HomeViewAdapter(getContext(), homeItemsList, galleriesList);
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerview.setAdapter(homeViewAdapter);
        book_club_rel.setVisibility(View.VISIBLE);
    }

//    private void BillingClass(){
//        mBillingClient = BillingClient.newBuilder(getContext()).setListener(new PurchasesUpdatedListener() {
//            @Override
//            public void onPurchasesUpdated(int responseCode, @Nullable List<Purchase> purchases) {
//
//                String sd = "Sd";
//
//                if (responseCode == BillingClient.BillingResponse.OK && purchases != null) {
//
//                    for (Purchase purchase : purchases) {
//                        //When every a new purchase is made
//
//                        String purchase_ok = "purchased";
//
//                        Toast.makeText(getContext().getApplicationContext(), "on purchase updated for loop", Toast.LENGTH_SHORT).show();
//
//                    }
//                }else if (responseCode == BillingClient.BillingResponse.USER_CANCELED){
//                    Toast.makeText(getContext().getApplicationContext(), "User Canceled", Toast.LENGTH_SHORT).show();
//                }
////                }else if (responseCode == BillingClient.BillingResponse.USER_CANCELED){
//                    // Handle an error caused by a user cancelling the purchase flow.
////                    Toast.makeText(getContext().getApplicationContext(), "App Canceled", Toast.LENGTH_SHORT).show();
////                }else {
//                    // Handle any other error codes.
//
////                }
//
//
//            }
//        }).build();
//
//        mBillingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(@BillingClient.BillingResponse int billingResponseCode) {
//                if (billingResponseCode == BillingClient.BillingResponse.USER_CANCELED) {
//                    // The billing client is ready. You can query purchases here.
////                    Toast.makeText(getContext().getApplicationContext(), "item not owned", Toast.LENGTH_SHORT).show();
//
//
//                }
//            }
//            @Override
//            public void onBillingServiceDisconnected() {
//                // Try to restart the connection on the next request to
//                // Google Play by calling the startConnection() method.
//
//            }
//        });
//        toPurchaseSubscription();
//    }


    private void toPurchaseSubscription(){
        BillingFlowParams.Builder builder = BillingFlowParams.newBuilder()
                .setSku("idillionaire_purchases").setType(BillingClient.SkuType.SUBS);
        int responseCode = mBillingClient.launchBillingFlow(getActivity(), builder.build());
        if (responseCode == BillingClient.BillingResponse.ITEM_ALREADY_OWNED){
            Toast.makeText(getContext().getApplicationContext(), "ALready owned", Toast.LENGTH_SHORT).show();
        }else if(responseCode == BillingClient.BillingResponse.ITEM_NOT_OWNED){
            Toast.makeText(getContext().getApplicationContext(), "not owned", Toast.LENGTH_SHORT).show();
        }else if (responseCode == BillingClient.BillingResponse.USER_CANCELED){
            Toast.makeText(getContext().getApplicationContext(), "user canceled", Toast.LENGTH_SHORT).show();
        }else if (responseCode == BillingClient.BillingResponse.ITEM_UNAVAILABLE){
            Toast.makeText(getContext().getApplicationContext(), "item unavailable", Toast.LENGTH_SHORT).show();
        }else if (responseCode == BillingClient.BillingResponse.BILLING_UNAVAILABLE){
            Toast.makeText(getContext().getApplicationContext(), "billing unavailable", Toast.LENGTH_SHORT).show();
        }else if (responseCode == BillingClient.BillingResponse.OK){
            Toast.makeText(getContext().getApplicationContext(), "ok", Toast.LENGTH_SHORT).show();
        }else if (responseCode == BillingClient.BillingResponse.SERVICE_DISCONNECTED){
            Toast.makeText(getContext().getApplicationContext(), "service disconnected", Toast.LENGTH_SHORT).show();

        }



    }


}
