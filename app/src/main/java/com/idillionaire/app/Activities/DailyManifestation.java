package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.DailyManifestAdapter;
import com.idillionaire.app.Adapters.ManifestAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.DailyManifestItems;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DailyManifestation extends AppCompatActivity {

    private RecyclerView recyclerView;
    RelativeLayout addmanifest;
    RelativeLayout back;
    TextView title_manifest;
    public static String title_string;
    String type_string;

    public static ArrayList<DailyManifestItems> dailyManifestationsLists;
    public static ArrayList<DailyManifestItems> weeklyManifestationsLists;
    public static ArrayList<DailyManifestItems> monthlyManifestationsLists;
    public static ArrayList<DailyManifestItems> yearlyManifestationsLists;

    private ArrayList<DailyManifestItems> listManifestItems;

    @Override
    protected void onResume() {
        super.onResume();
        apiListManifestation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_manifestation);

        title_string = ManifestAdapter.title_barString;
        title_manifest = findViewById(R.id.daily_menifest_title);
        title_manifest.setText(title_string);
        addmanifest = (RelativeLayout) findViewById(R.id.add_manifest);

        back = (RelativeLayout) findViewById(R.id.back_daily_meni);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        addmanifest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addIntent = new Intent(DailyManifestation.this, SecondaryAddManifestation.class);
                startActivity(addIntent);
                finish();
            }
        });

//        listManifestItems = ManifestAdapter.public_arr;

        recyclerView = (RecyclerView) findViewById(R.id.daily_manifest_recycler);

        apiListManifestation();

    }

    private void apiListManifestation(){
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTMANIFESTATION, DailyManifestation.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");

                        JSONArray daily_manifestations = jsonObject.getJSONArray("daily_manifestations");
                        dailyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < daily_manifestations.length(); i++){
                            JSONObject dailyManifestationObject = daily_manifestations.getJSONObject(i);
                            DailyManifestItems dailyManifestationsListObject = new DailyManifestItems();

                            String _id = dailyManifestationObject.getString("_id");
                            String user = dailyManifestationObject.getString("user");
                            String text = dailyManifestationObject.getString("text");
                            Boolean completion_status = dailyManifestationObject.getBoolean("completion_status");
                            String type = dailyManifestationObject.getString("type");
                            String date = dailyManifestationObject.getString("date");

                            dailyManifestationsListObject.set_id(_id);
                            dailyManifestationsListObject.setUser(user);
                            dailyManifestationsListObject.setText(text);
                            dailyManifestationsListObject.setCompletion_status(completion_status);
                            dailyManifestationsListObject.setType(type);
                            dailyManifestationsListObject.setDate(date);
                            dailyManifestationsLists.add(dailyManifestationsListObject);
                        }
                        JSONArray weekly_manifestations = jsonObject.getJSONArray("weekly_manifestations");
                        weeklyManifestationsLists = new ArrayList<>();
                        for (int a = 0; a < weekly_manifestations.length(); a++){
                            JSONObject weeklyManifestationObject = weekly_manifestations.getJSONObject(a);
                            DailyManifestItems weeklyManifestationsListObject = new DailyManifestItems();

                            String weekly_id = weeklyManifestationObject.getString("_id");
                            String weeklyuser = weeklyManifestationObject.getString("user");
                            String weeklytext = weeklyManifestationObject.getString("text");
                            Boolean weeklycompletion_status = weeklyManifestationObject.getBoolean("completion_status");
                            String weeklytype = weeklyManifestationObject.getString("type");
                            String weeklydate = weeklyManifestationObject.getString("date");

                            weeklyManifestationsListObject.set_id(weekly_id);
                            weeklyManifestationsListObject.setUser(weeklyuser);
                            weeklyManifestationsListObject.setText(weeklytext);
                            weeklyManifestationsListObject.setCompletion_status(weeklycompletion_status);
                            weeklyManifestationsListObject.setType(weeklytype);
                            weeklyManifestationsListObject.setDate(weeklydate);
                            weeklyManifestationsLists.add(weeklyManifestationsListObject);
                        }
                        JSONArray monthly_manifestations = jsonObject.getJSONArray("monthly_manifestations");
                        monthlyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < monthly_manifestations.length(); i++){
                            JSONObject monthlyManifestationObject = monthly_manifestations.getJSONObject(i);
                            DailyManifestItems monthlyManifestationsListObject = new DailyManifestItems();

                            String monthly_id = monthlyManifestationObject.getString("_id");
                            String monthlyuser = monthlyManifestationObject.getString("user");
                            String monthlytext = monthlyManifestationObject.getString("text");
                            Boolean monthlycompletion_status = monthlyManifestationObject.getBoolean("completion_status");
                            String monthlytype = monthlyManifestationObject.getString("type");
                            String monthlydate = monthlyManifestationObject.getString("date");

                            monthlyManifestationsListObject.set_id(monthly_id);
                            monthlyManifestationsListObject.setUser(monthlyuser);
                            monthlyManifestationsListObject.setText(monthlytext);
                            monthlyManifestationsListObject.setCompletion_status(monthlycompletion_status);
                            monthlyManifestationsListObject.setType(monthlytype);
                            monthlyManifestationsListObject.setDate(monthlydate);
                            monthlyManifestationsLists.add(monthlyManifestationsListObject);
                        }
                        JSONArray yearly_manifestations = jsonObject.getJSONArray("yearly_manifestations");
                        yearlyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < yearly_manifestations.length(); i++){
                            JSONObject yearlyManifestationObject = yearly_manifestations.getJSONObject(i);
                            DailyManifestItems yearlyManifestationsListObject = new DailyManifestItems();

                            String yearly_id = yearlyManifestationObject.getString("_id");
                            String yearlyuser = yearlyManifestationObject.getString("user");
                            String yearlytext = yearlyManifestationObject.getString("text");
                            Boolean yearlycompletion_status = yearlyManifestationObject.getBoolean("completion_status");
                            String yearlytype = yearlyManifestationObject.getString("type");
                            String yearlydate = yearlyManifestationObject.getString("date");

                            yearlyManifestationsListObject.set_id(yearly_id);
                            yearlyManifestationsListObject.setUser(yearlyuser);
                            yearlyManifestationsListObject.setText(yearlytext);
                            yearlyManifestationsListObject.setCompletion_status(yearlycompletion_status);
                            yearlyManifestationsListObject.setType(yearlytype);
                            yearlyManifestationsListObject.setDate(yearlydate);
                            yearlyManifestationsLists.add(yearlyManifestationsListObject);
                        }

                        type_string = ManifestAdapter.type_String;

                        if (type_string == "Daily Manifestation"){
                            listManifestItems = dailyManifestationsLists;
                        }if (type_string == "Weekly Manifestation"){
                            listManifestItems = weeklyManifestationsLists;
                        }if(type_string == "Monthly Manifestation"){
                            listManifestItems = monthlyManifestationsLists;
                        }if (type_string == "Yearly Manifestation"){
                            listManifestItems = yearlyManifestationsLists;
                        }
                        DailyManifestAdapter dailyManifestAdapter = new DailyManifestAdapter(DailyManifestation.this, listManifestItems);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DailyManifestation.this));
                        recyclerView.setAdapter(dailyManifestAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}
