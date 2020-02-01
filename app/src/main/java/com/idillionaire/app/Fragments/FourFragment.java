package com.idillionaire.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Activities.ArivalPage;
import com.idillionaire.app.Activities.BookClub;
import com.idillionaire.app.Activities.DailyGratitude;
import com.idillionaire.app.Activities.ManifestaionPage;
import com.idillionaire.app.Activities.PremiumActivity;
import com.idillionaire.app.Adapters.JournalListAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Classes.JournalItems;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.Models.JournalModel;
import com.idillionaire.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FourFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<JournalModel> journalItemsList;
    public static ArrayList<DailyGratitudeItems> gratitudeItemsList;

    private Context context;

    ImageView gratitude_image;
    ImageView manifestation_image, g_shade, m_shade;
    LinearLayout main_parent_layout;
    TextView text1;
    TextView text2;
    SharedPreferences sp;
    String purchase_token;
    RelativeLayout gone_layout, gratitude_layout, manifest_layout;

    TextView get_subscribed;

    @Override
    public void onResume() {
        super.onResume();

        sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");

        if (!purchase_token.isEmpty()){
            gone_layout.setVisibility(View.GONE);
            apiJournal();
            apiListDailyGratitude();
        }else {
            gone_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){

                sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                purchase_token = sp.getString("PURCHASETOKEN", "");

            if (!purchase_token.isEmpty()){
                gone_layout.setVisibility(View.GONE);
                apiJournal();
                apiListDailyGratitude();
            }else {
                gone_layout.setVisibility(View.VISIBLE);
            }

                getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_four, container, false);

        context = getContext();

        gratitude_image = view.findViewById(R.id.gratitude_image);
        manifestation_image = view.findViewById(R.id.Manifestation_image);
        text1 = view.findViewById(R.id.journal_txt);
        text2 = view.findViewById(R.id.gratitude_text);
        gratitude_layout = view.findViewById(R.id.gratitude_layout);
        manifest_layout = view.findViewById(R.id.manifest_layout);
        g_shade = view.findViewById(R.id.g_shade);
        m_shade = view.findViewById(R.id.m_shade);
        main_parent_layout = view.findViewById(R.id.main_parent_layout);

        gone_layout = view.findViewById(R.id.gone_layout_f4);

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels - dpToPx(110);
        int width = dm.widthPixels;

        ViewGroup.LayoutParams gratitude_layoutLayoutParams = gratitude_layout.getLayoutParams();
        ViewGroup.LayoutParams manifest_layoutLayoutParams = manifest_layout.getLayoutParams();
        ViewGroup.LayoutParams gratitude_imageLayoutParams = gratitude_image.getLayoutParams();
        ViewGroup.LayoutParams manifestation_imageLayoutParams = manifestation_image.getLayoutParams();
        ViewGroup.LayoutParams g_shadeLayoutParams = g_shade.getLayoutParams();
        ViewGroup.LayoutParams m_shadeLayoutParams = m_shade.getLayoutParams();

        gratitude_layoutLayoutParams.height = height/2;
        manifest_layoutLayoutParams.height = height/2;
        gratitude_imageLayoutParams.height = height/2;
        manifestation_imageLayoutParams.height = height/2;
        g_shadeLayoutParams.height = height/2;
        m_shadeLayoutParams.height = height/2;

        gratitude_layoutLayoutParams.width = width;
        manifest_layoutLayoutParams.width = width;
        gratitude_imageLayoutParams.width = width;
        manifestation_imageLayoutParams.width = width;
        g_shadeLayoutParams.width = width;
        m_shadeLayoutParams.width = width;

        gratitude_layout.setLayoutParams(gratitude_layoutLayoutParams);
        manifest_layout.setLayoutParams(manifest_layoutLayoutParams);
        gratitude_image.setLayoutParams(gratitude_imageLayoutParams);
        manifestation_image.setLayoutParams(manifestation_imageLayoutParams);
        g_shade.setLayoutParams(g_shadeLayoutParams);
        m_shade.setLayoutParams(m_shadeLayoutParams);

        sp = getContext().getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
        purchase_token = sp.getString("PURCHASETOKEN", "");

        if (!purchase_token.isEmpty()){
            gone_layout.setVisibility(View.GONE);
            apiJournal();
            apiListDailyGratitude();
        }else {
            gone_layout.setVisibility(View.VISIBLE);
        }

        get_subscribed = view.findViewById(R.id.get_subscribed_f4);
        get_subscribed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), PremiumActivity.class);
                intent.putExtra("premium", "Homepage");
                startActivity(intent);
            }
        });
        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "HelveticaLTStd-Roman.otf");
        TextView first_string = view.findViewById(R.id.first_string);
        TextView second_string = view.findViewById(R.id.second_string);
        TextView third_string = view.findViewById(R.id.third_string);

        first_string.setTypeface(font);
        second_string.setTypeface(font);
        third_string.setTypeface(font);
        get_subscribed.setTypeface(font);



//        journalItemsList.add(new JournalItems("Daily Gratitude", R.drawable.journal_1));
//        journalItemsList.add(new JournalItems("Manifestation", R.drawable.journal_2));

//        apiListDailyGratitude();

        gratitude_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gratitudeItemsList.isEmpty()){
                    Intent intent = new Intent(getContext(), ArivalPage.class);
                    startActivity(intent);
                }else {
                    Intent gratitudeintent = new Intent(getContext(), DailyGratitude.class);
                    startActivity(gratitudeintent);
                }
            }
        });
        manifestation_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manifestintent = new Intent(getContext(), ManifestaionPage.class);
                startActivity(manifestintent);
            }
        });

//        recyclerView = (RecyclerView) view.findViewById(R.id.journal_list_recycler);

        return view;
    }


    public int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private void apiJournal(){
        final SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.JOURNAL, getContext(),postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        JSONArray journal_Images = jsonObject.getJSONArray("journal_Images");
                        journalItemsList = new ArrayList<>();
                        for (int i = 0; i < journal_Images.length(); i++){
                            JSONObject journal_Images_obj = journal_Images.getJSONObject(i);

                            JournalModel journalModel = new JournalModel();

                            String _id = journal_Images_obj.getString("_id");
                            String gratitude_bg = journal_Images_obj.getString("gratitude_bg");
                            String manifestation_bg = journal_Images_obj.getString("manifestation_bg");
                            String daily_manifestation = journal_Images_obj.getString("daily_manifestation");
                            String weekly_manifestation = journal_Images_obj.getString("weekly_manifestation");
                            String monthly_manifestation = journal_Images_obj.getString("monthly_manifestation");
                            String yearly_manifestation = journal_Images_obj.getString("yearly_manifestation");

                            journalModel.set_id(_id);
                            journalModel.setGratitude_bg(gratitude_bg);
                            journalModel.setManifestation_bg(manifestation_bg);
                            journalModel.setDaily_manifestation(daily_manifestation);
                            journalModel.setWeekly_manifestation(weekly_manifestation);
                            journalModel.setMonthly_manifestation(monthly_manifestation);
                            journalModel.setYearly_manifestation(yearly_manifestation);

                            journalItemsList.add(journalModel);
                            Picasso.with(getContext())
                                    .load(Constants.URL.BASE_URL + journalItemsList.get(i).getGratitude_bg())
                                    .into(gratitude_image);
                            Picasso.with(getContext())
                                    .load(Constants.URL.BASE_URL + journalItemsList.get(i).getManifestation_bg())
                                    .into(manifestation_image);
                        }
//                        JournalListAdapter journalListAdapter = new JournalListAdapter(getContext(), journalItemsList);
//                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        recyclerView.setAdapter(journalListAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void apiListDailyGratitude(){

        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTGRATITUDE, getContext(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        JSONArray daily_gratitude = jsonObject.getJSONArray("daily_gratitude");
                        gratitudeItemsList = new ArrayList<>();
                        for (int i = 0; i < daily_gratitude.length(); i++){
                            JSONObject gratitudeOBJ = daily_gratitude.getJSONObject(i);
                            DailyGratitudeItems dailyGratitude = new DailyGratitudeItems();

                            String _id = gratitudeOBJ.getString("_id");
                            String user = gratitudeOBJ.getString("user");
                            JSONArray texts = gratitudeOBJ.getJSONArray("texts");
                            String date = gratitudeOBJ.getString("date");

                            ArrayList <String> text_arr = new ArrayList<>();

                            for (int a = 0; a < texts.length(); a++){

                                text_arr.add(String.valueOf(texts.get(a)));

                            }


                            dailyGratitude.set_id(_id);
                            dailyGratitude.setUser(user);
                            dailyGratitude.setTexts(text_arr);
                            dailyGratitude.setDate(date);
                            gratitudeItemsList.add(dailyGratitude);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(getContext().getApplicationContext().getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
