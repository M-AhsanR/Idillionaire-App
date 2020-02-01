package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.UpdateAppearance;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.GratitudeAdapter;
import com.idillionaire.app.Adapters.UpdateGratitudeAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Classes.ArivalItems;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UpdateGratitude extends AppCompatActivity {

    private RecyclerView recyclerView;
    List<String> text_arr;
    private RelativeLayout back;
    private TextView save;
    String gratitude_id;
    LinearLayout main_parent;
    String text;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UpdateGratitude.this, DailyGratitude.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_gratitude);
        back = (RelativeLayout) findViewById(R.id.back_arival);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpdateGratitude.this, DailyGratitude.class);
                startActivity(intent);
                finish();
            }
        });

        main_parent = (LinearLayout) findViewById(R.id.main_parent);
        setupUI(findViewById(R.id.main_parent));

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        save = findViewById(R.id.save);

        text_arr = GratitudeAdapter.text_arr;
        gratitude_id = GratitudeAdapter.gratitude_id;

        recyclerView = (RecyclerView) findViewById(R.id.today_recycler);
        final UpdateGratitudeAdapter updateGratitudeAdapter = new UpdateGratitudeAdapter(this, text_arr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(updateGratitudeAdapter);
        recyclerView.setEnabled(false);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiUpdateGratitude();
                Intent intent = new Intent(UpdateGratitude.this, DailyGratitude.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void apiUpdateGratitude(){

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN= mPrefs.getString("USER_TOKEN","");

        Map<String, List<String>> postParam = new HashMap<String, List<String>>();
        postParam.put("texts", text_arr);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponseArray(Request.Method.POST, Constants.URL.UPDATEGRATITUDE + gratitude_id, UpdateGratitude.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();

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
                    hideSoftKeyboard(UpdateGratitude.this);
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
