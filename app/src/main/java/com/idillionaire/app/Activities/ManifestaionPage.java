package com.idillionaire.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.ManifestAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Classes.ManifestItems;
import com.idillionaire.app.Models.DailyManifestItems;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManifestaionPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<ManifestItems> manifestItemsList;
    RelativeLayout back;
    public static ArrayList<DailyManifestItems> dailyManifestationsLists;
    public static ArrayList<DailyManifestItems> weeklyManifestationsLists;
    public static ArrayList<DailyManifestItems> monthlyManifestationsLists;
    public static ArrayList<DailyManifestItems> yearlyManifestationsLists;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        apiListManifestation();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifestaion_page);

        back = (RelativeLayout) findViewById(R.id.back_mani);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        manifestItemsList = new ArrayList<>();

        manifestItemsList.add(new ManifestItems("Daily Manifestation", R.drawable.mani_1st));
        manifestItemsList.add(new ManifestItems("Weekly Manifestation", R.drawable.mani_2nd));
        manifestItemsList.add(new ManifestItems("Monthly Manifestation", R.drawable.mani_3rd));
        manifestItemsList.add(new ManifestItems("Yearly Manifestation", R.drawable.mani_4th));

        recyclerView = (RecyclerView) findViewById(R.id.menifest_list_recycler);
        ManifestAdapter manifestAdapter = new ManifestAdapter(this, manifestItemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(manifestAdapter);

        apiListManifestation();

    }
    private void apiListManifestation(){
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTMANIFESTATION, ManifestaionPage.this, postParam, headers, new ServerCallback() {
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
