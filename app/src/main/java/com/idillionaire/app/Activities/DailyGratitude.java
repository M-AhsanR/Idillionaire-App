package com.idillionaire.app.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.daimajia.swipe.util.Attributes;
import com.idillionaire.app.Adapters.GratitudeAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyGratitude extends AppCompatActivity {

    private RecyclerView recyclerView;
    public static List<DailyGratitudeItems> gratitudeItemsList;
    private RelativeLayout back;
    ArrayList<String> text_arr;

    @Override
    protected void onResume() {
        super.onResume();
        apiListDailyGratitude();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_gratitude);

        back = (RelativeLayout) findViewById(R.id.back_gratitude);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.gratitude_recycler);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                Log.e("RecyclerView", "onScrollStateChanged");
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        RelativeLayout edit_notes = (RelativeLayout) findViewById(R.id.edit_notes);
        edit_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent arivalIntent = new Intent(DailyGratitude.this, ArivalPage.class);
                startActivity(arivalIntent);
                finish();
            }
        });

        apiListDailyGratitude();
    }

    private void apiListDailyGratitude(){

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTGRATITUDE, DailyGratitude.this, postParam, headers, new ServerCallback() {
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

                            text_arr = new ArrayList<>();

                            for (int a = 0; a < texts.length(); a++){

                                text_arr.add(String.valueOf(texts.get(a)));

                            }


                            dailyGratitude.set_id(_id);
                            dailyGratitude.setUser(user);
                            dailyGratitude.setTexts(text_arr);
                            dailyGratitude.setDate(date);
                            gratitudeItemsList.add(dailyGratitude);
                        }

                        GratitudeAdapter gratitudeAdapter = new GratitudeAdapter(DailyGratitude.this, gratitudeItemsList);
                        gratitudeAdapter.setMode(Attributes.Mode.Single);
                        recyclerView.setLayoutManager(new LinearLayoutManager(DailyGratitude.this));
                        recyclerView.setAdapter(gratitudeAdapter);
                        gratitudeAdapter.notifyDataSetChanged();



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
