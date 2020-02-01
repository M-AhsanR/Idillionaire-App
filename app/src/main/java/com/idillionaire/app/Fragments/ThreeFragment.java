package com.idillionaire.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.nfc.TagLostException;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.daimajia.swipe.util.Attributes;
import com.idillionaire.app.Activities.GetStarted;
import com.idillionaire.app.Activities.HomePage;
import com.idillionaire.app.Activities.PremiumActivity;
import com.idillionaire.app.Adapters.FavrtGrideMAdapter;
import com.idillionaire.app.Adapters.SchedualAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Categories;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.Models.JournalModel;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.ScheduleModel;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class ThreeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<ScheduleModel> schedualItemsList;
    SharedPreferences sp;
    Gson gson = new Gson();
    RelativeLayout gone_layout;
    String purchase_token;

    TextView get_subscribed;

    @Override
    public void onResume() {
        super.onResume();

        sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");


        if (!purchase_token.isEmpty()){
            gone_layout.setVisibility(View.GONE);
        }else {
            gone_layout.setVisibility(View.VISIBLE);
        }

        sp = getActivity().getSharedPreferences("SCHEDULE_ARR", Context.MODE_PRIVATE);

        String arayList = sp.getString("schedule_array", "");
        Type type = new TypeToken<ArrayList<ScheduleModel>>() {
        }.getType();
        schedualItemsList = gson.fromJson(arayList, type);

        SchedualAdapter schedualAdapter = new SchedualAdapter(getContext(), schedualItemsList);
        schedualAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(schedualAdapter);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {

            try {

                sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                purchase_token = sp.getString("PURCHASETOKEN", "");

                if (!purchase_token.isEmpty()){
                    gone_layout.setVisibility(View.GONE);
                }else {
                    gone_layout.setVisibility(View.VISIBLE);
                }

                sp = getActivity().getSharedPreferences("SCHEDULE_ARR", Context.MODE_PRIVATE);

                String arayList = sp.getString("schedule_array", "");
                Type type = new TypeToken<ArrayList<ScheduleModel>>() {
                }.getType();
                schedualItemsList = gson.fromJson(arayList, type);

                SchedualAdapter schedualAdapter = new SchedualAdapter(getContext(), schedualItemsList);
                schedualAdapter.setMode(Attributes.Mode.Single);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(schedualAdapter);

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } catch (Exception e) {
                Log.e("Notification Error", "With Notification");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);


        gone_layout = view.findViewById(R.id.gone_layout_f3);

        sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");


        if (!purchase_token.isEmpty()){
            gone_layout.setVisibility(View.GONE);
        }else {
            gone_layout.setVisibility(View.VISIBLE);
        }

        get_subscribed = view.findViewById(R.id.get_subscribed_f3);
        get_subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PremiumActivity.class);
                intent.putExtra("premium", "Homepage");
                startActivity(intent);
            }
        });
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaLTStd-Roman.otf");
        TextView first_string1 = view.findViewById(R.id.first_string1);
        TextView second_string1 = view.findViewById(R.id.second_string1);
        TextView third_string1 = view.findViewById(R.id.third_string1);

        first_string1.setTypeface(font);
        second_string1.setTypeface(font);
        third_string1.setTypeface(font);
        get_subscribed.setTypeface(font);

        sp = getActivity().getSharedPreferences("SCHEDULE_ARR", Context.MODE_PRIVATE);

        String arayList = sp.getString("schedule_array", "");
        Type type = new TypeToken<ArrayList<ScheduleModel>>() {
        }.getType();
        schedualItemsList = gson.fromJson(arayList, type);

//        schedualItemsList = ScheduleActivity.items_list;
//        schedualItemsList.add(new SchedualItems("Life", "02 : 45 pm", R.drawable.schedual_1st));
//        schedualItemsList.add(new SchedualItems("Nature", "02 : 45 pm", R.drawable.schedual_2nd));
//        schedualItemsList.add(new SchedualItems("Sunset", "02 : 45 pm", R.drawable.schedual_3rd));
//        schedualItemsList.add(new SchedualItems("Beach", "02 : 45 pm", R.drawable.schedual_4th));
//        schedualItemsList.add(new SchedualItems("Walk", "02 : 45 pm", R.drawable.schedual_5th));
//        schedualItemsList.add(new SchedualItems("Candles", "02 : 45 pm", R.drawable.schedual_6th));

        recyclerView = (RecyclerView) view.findViewById(R.id.schedual_recycler);
        SchedualAdapter schedualAdapter = new SchedualAdapter(getContext(), schedualItemsList);
        schedualAdapter.setMode(Attributes.Mode.Single);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(schedualAdapter);

        sp = getActivity().getSharedPreferences("SCHEDULE_ARR", Context.MODE_PRIVATE);
        String aray = gson.toJson(schedualItemsList);
        sp.edit().putString("schedule_array", aray);


        return view;
    }

//    private void apiHome() {
//
//        final SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");
//
//        Map<String, String> postParam = new HashMap<String, String>();
//
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("Content-Type", "application/json");
//        headers.put("x-sh-auth", USER_TOKEN);
//
//        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.HOME, getContext(),postParam, headers, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result, String ERROR) {
//
//                if (ERROR.isEmpty()){
//                    try {
//
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        int code = jsonObject.getInt("code");
//
//                        JSONArray categories = jsonObject.getJSONArray("categories");
//
//                        ArrayList<Categories> homeItemsList = new ArrayList<>();
//                        array_for_shared = new ArrayList<>();
//                        for (int i = 0; i < categories.length(); i++){
//                            JSONObject a = categories.getJSONObject(i);
//                            Categories categories_obj = new Categories();
//
//                            String _id = a.getString("_id");
//                            String title = a.getString("title");
//                            Boolean is_active = a.getBoolean("is_active");
//                            int order_number = a.getInt("order_number");
//                            JSONArray galleries = a.getJSONArray("galleries");
//                            String Detail_image =a.getString("Detail_image");
//                            String list_image =a.getString("list_image");
//
//                            galleriesList = new ArrayList<>();
//
//                            for (int b = 0; b < galleries.length(); b++){
//                                Galleries galleries_obj = new Galleries();
//
//                                JSONObject o = galleries.getJSONObject(b);
//
//                                int order_number_gall = o.getInt("order_number");
//                                String updated_at = o.getString("updated_at");
//                                String _id_galleries = o.getString("_id");
//                                JSONObject gallery = o.getJSONObject("gallery");
//
//                                JSONArray media_files = gallery.getJSONArray("media_files");
//                                String _id_gallery = gallery.getString("_id");
//                                String title_gallery = gallery.getString("title");
//                                String media_file_name = gallery.getString("media_file_name");
//
//                                media_files_arr = new ArrayList<>();
//                                for (int x = 0; x < media_files.length(); x++ ){
//                                    JSONObject s = media_files.getJSONObject(x);
//                                    MediaFiles mediaFiles_obj = new MediaFiles();
//
//                                    int order_number_media = s.getInt("order_number");
//                                    String _id_media = s.getString("_id");
//                                    String title_media = s.getString("title");
//                                    String list_image_media = s.getString("list_image");
//                                    String Detailed_img_name = s.getString("Detailed_img_name");
//
//                                    mediaFiles_obj.set_id(_id_media);
//                                    mediaFiles_obj.setDetailed_img_name(Detailed_img_name);
//                                    mediaFiles_obj.setList_image(list_image_media);
//                                    mediaFiles_obj.setTitle(title_media);
//                                    mediaFiles_obj.setOrder_number(order_number_media);
//                                    mediaFiles_obj.setGallery_title(title_gallery);
//                                    media_files_arr.add(mediaFiles_obj);
//                                    array_for_shared.add(mediaFiles_obj);
//
//                                }
//
//                                galleries_obj.setOrder_number(order_number_gall);
//                                galleries_obj.setUpdated_at(updated_at);
//                                galleries_obj.set_id_galleries(_id_galleries);
////                            galleries_obj.setGallery(gallery);
//
//
//                                galleries_obj.set_id_gallery(_id_gallery);
//                                galleries_obj.setTitle_gallery(title_gallery);
//                                galleries_obj.setMedia_file_name(media_file_name);
//                                galleries_obj.setMedia_files(media_files_arr);
//
//                                galleriesList.add(galleries_obj);
//
//                            }
//
//                            categories_obj.set_id(_id);
//                            categories_obj.setTitle(title);
//                            categories_obj.setIs_active(is_active);
//                            categories_obj.setOrder_number(order_number);
//                            categories_obj.setGalleries(galleriesList);
//                            categories_obj.setDetail_image(Detail_image);
//                            categories_obj.setList_image(list_image);
//
//                            homeItemsList.add(categories_obj);
//
//                        }
//
//                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor mEditor = mPrefs.edit();
//                        Gson gson = new Gson();
//                        String galleryList = gson.toJson(array_for_shared);
//                        mEditor.putString("GALLERYLIST", galleryList);
//                        mEditor.apply();
//
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    setuprecyclerview(homeItemsList, galleriesList);
//                }
//
//                else {
//                    if (ERROR.equals("401")) {
//                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                        SharedPreferences.Editor mEditor = mPrefs.edit();
//                        mEditor.putString("USER_EMAIL", "");
//                        mEditor.putString("USER_PASSWORD", "");
//                        mEditor.putString("USER_TOKEN", "");
//                        mEditor.putString("USER_PIC", "");
//                        mEditor.putString("USER_ID", "");
//                        mEditor.apply();
//
//                        Intent intentturn = new Intent(getContext(), GetStarted.class);
//                        startActivity(intentturn);
//                        getActivity().finish();
//                    } else {
//                        Toast.makeText(getContext(), ERROR, Toast.LENGTH_LONG).show();
//                    }
//                }
//
//            }
//        });
//
//    }
//    private void apiJournal(){
//        final SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");
//
//        Map<String, String> postParam = new HashMap<String, String>();
//
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("x-sh-auth", USER_TOKEN);
//
//        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.JOURNAL, getContext(),postParam, headers, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result, String ERROR) {
//
//                if (ERROR.isEmpty()){
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        int code = jsonObject.getInt("code");
//                        JSONArray journal_Images = jsonObject.getJSONArray("journal_Images");
//                        journalItemsList = new ArrayList<>();
//                        for (int i = 0; i < journal_Images.length(); i++){
//                            JSONObject journal_Images_obj = journal_Images.getJSONObject(i);
//
//                            JournalModel journalModel = new JournalModel();
//
//                            String _id = journal_Images_obj.getString("_id");
//                            String gratitude_bg = journal_Images_obj.getString("gratitude_bg");
//                            String manifestation_bg = journal_Images_obj.getString("manifestation_bg");
//                            String daily_manifestation = journal_Images_obj.getString("daily_manifestation");
//                            String weekly_manifestation = journal_Images_obj.getString("weekly_manifestation");
//                            String monthly_manifestation = journal_Images_obj.getString("monthly_manifestation");
//                            String yearly_manifestation = journal_Images_obj.getString("yearly_manifestation");
//
//                            journalModel.set_id(_id);
//                            journalModel.setGratitude_bg(gratitude_bg);
//                            journalModel.setManifestation_bg(manifestation_bg);
//                            journalModel.setDaily_manifestation(daily_manifestation);
//                            journalModel.setWeekly_manifestation(weekly_manifestation);
//                            journalModel.setMonthly_manifestation(monthly_manifestation);
//                            journalModel.setYearly_manifestation(yearly_manifestation);
//
//                            journalItemsList.add(journalModel);
//                            Picasso.with(getContext())
//                                    .load(Constants.URL.BASE_URL + journalItemsList.get(i).getGratitude_bg())
//                                    .into(gratitude_image);
//                            Picasso.with(getContext())
//                                    .load(Constants.URL.BASE_URL + journalItemsList.get(i).getManifestation_bg())
//                                    .into(manifestation_image);
//                        }
////                        JournalListAdapter journalListAdapter = new JournalListAdapter(getContext(), journalItemsList);
////                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////                        recyclerView.setAdapter(journalListAdapter);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//    }
//    private void apiListDailyGratitude(){
//
//        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");
//
//        Map<String, String> postParam = new HashMap<String, String>();
//
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("x-sh-auth", USER_TOKEN);
//
//        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTGRATITUDE, getContext(), postParam, headers, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result, String ERROR) {
//                if (ERROR.isEmpty()){
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        int code = jsonObject.getInt("code");
//                        JSONArray daily_gratitude = jsonObject.getJSONArray("daily_gratitude");
//                        gratitudeItemsList = new ArrayList<>();
//                        for (int i = 0; i < daily_gratitude.length(); i++){
//                            JSONObject gratitudeOBJ = daily_gratitude.getJSONObject(i);
//                            DailyGratitudeItems dailyGratitude = new DailyGratitudeItems();
//
//                            String _id = gratitudeOBJ.getString("_id");
//                            String user = gratitudeOBJ.getString("user");
//                            JSONArray texts = gratitudeOBJ.getJSONArray("texts");
//                            String date = gratitudeOBJ.getString("date");
//
//                            ArrayList <String> text_arr = new ArrayList<>();
//
//                            for (int a = 0; a < texts.length(); a++){
//
//                                text_arr.add(String.valueOf(texts.get(a)));
//
//                            }
//
//
//                            dailyGratitude.set_id(_id);
//                            dailyGratitude.setUser(user);
//                            dailyGratitude.setTexts(text_arr);
//                            dailyGratitude.setDate(date);
//                            gratitudeItemsList.add(dailyGratitude);
//                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                }else {
//                    Toast.makeText(getContext().getApplicationContext().getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }
//    private void apiFavourite(){
//        mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");
//
//        Map<String, String> postParam = new HashMap<String, String>();
//
//        HashMap<String, String> headers = new HashMap<String, String>();
//        headers.put("x-sh-auth", USER_TOKEN);
//
//        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.FVRT, getContext(), postParam, headers, new ServerCallback() {
//            @Override
//            public void onSuccess(JSONObject result, String ERROR) {
//                if (ERROR.isEmpty()){
//                    try {
//                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
//                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
////                        ArrayList<FavoriteList> favourite_images_arr = new ArrayList<>();
//                        favoriteLists = new ArrayList<>();
//                        for (int i = 0; i < favourite_images.length(); i++){
//                            JSONObject a = favourite_images.getJSONObject(i);
//                            Login.Favourite_images favoriteListObj = new Login.Favourite_images();
//
//                            String _id = a.getString("_id");
//                            String title = a.getString("title");
//                            String image_id = a.getString("image_id");
//                            String Detailed_img_name = a.getString("Detailed_img_name");
//                            String list_img = a.getString("list_img");
//                            int order_number = a.getInt("order_number");
//
//                            favoriteListObj.set_id(_id);
//                            favoriteListObj.setTitle(title);
//                            favoriteListObj.setImage_id(image_id);
//                            favoriteListObj.setDetailed_img_name(Detailed_img_name);
//                            favoriteListObj.setList_img(list_img);
//                            favoriteListObj.setOrder_number(order_number);
//                            favoriteLists.add(favoriteListObj);
//                        }
//
//                        adapter = new FavrtGrideMAdapter(getContext(), favoriteLists);
//                        gridView.setAdapter(adapter);
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }else {
//                    Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//    }


}
