package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class ManifestDetail extends AppCompatActivity {

    EditText menifest_txt;
    RelativeLayout back;
    public static String type_String;
    SwitchCompat update_switch;
    TextView submit_btn;
    String id_String;
    String manifest_text;
    public static String truefalse;
    LinearLayout main_parent;
    String text;
    boolean completion_state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manifest_detail);

        main_parent = (LinearLayout) findViewById(R.id.main_parent);
        setupUI(findViewById(R.id.main_parent));

        menifest_txt = (EditText) findViewById(R.id.menifest_txt);

        type_String = ManifestAdapter.type_String;

        manifest_text = DailyManifestAdapter.manifest_text;
        menifest_txt.setText(manifest_text);
        completion_state = DailyManifestAdapter.completion_state;

        back =(RelativeLayout) findViewById(R.id.back_meni_detail);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManifestDetail.this, DailyManifestation.class);
                startActivity(intent);
                finish();
            }
        });

        update_switch = findViewById(R.id.update_switch);
        if (completion_state == true){
            update_switch.setChecked(true);
        }else {
            update_switch.setChecked(false);
        }

        id_String = DailyManifestAdapter.id_String;
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (update_switch.isChecked()){
                    truefalse = update_switch.getTextOn().toString();
                }else {
                    truefalse = update_switch.getTextOff().toString();
                }
                if (menifest_txt.getText().toString().isEmpty()){
                    submit_btn.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
                }else {
                    apiUpdateManifestation();
                    Intent intent = new Intent(ManifestDetail.this, DailyManifestation.class);
                    startActivity(intent);
                    finish();
                }
                submit_btn.setEnabled(true);

            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ManifestDetail.this, DailyManifestation.class);
        startActivity(intent);
        finish();
    }

    private void apiUpdateManifestation(){

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("text", menifest_txt.getText().toString());
        postParam.put("type", type_String);
        postParam.put("completion_status", truefalse);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATEMANIFESTATION + id_String, ManifestDetail.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        JSONArray daily_manifestations = jsonObject.getJSONArray("daily_manifestations");
                        ArrayList<DailyManifestItems> dailyManifestationsLists = new ArrayList<>();
//                        Datalist = ManifestAdapter.public_arr;
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
                        ArrayList<DailyManifestItems> weeklyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.weeklyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
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
                        ArrayList<DailyManifestItems> monthlyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.monthlyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
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
                        ArrayList<DailyManifestItems> yearlyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.yearlyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
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

                        Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ManifestDetail.this);
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
    public static void hideSoftKeyboard(Activity activity ) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }


}
