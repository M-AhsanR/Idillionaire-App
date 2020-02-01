package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.ManifestAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.DailyManifestItems;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddManifestation extends AppCompatActivity {

    RelativeLayout back;
    TextView main_title, add_title, submit_btn;
    EditText menifest_txt;
    LinearLayout main_parent;

    public static String addmainString;
    public static String title_string;
    public static String type_String;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manifestation);

        main_parent = (LinearLayout) findViewById(R.id.main_parent);
        setupUI(findViewById(R.id.main_parent));
        menifest_txt = findViewById(R.id.menifest_txt);

        addmainString = ManifestAdapter.addMainString;
        main_title = findViewById(R.id.main_title);
        main_title.setText(addmainString);
        submit_btn = findViewById(R.id.submit_btn);

        title_string = ManifestAdapter.title_barString;
        add_title = findViewById(R.id.add_menifest_title);
        add_title.setText(title_string);

        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        main_title.setTypeface(font);
        add_title.setTypeface(font);
        submit_btn.setTypeface(font);
        menifest_txt.setTypeface(font);

        back = (RelativeLayout) findViewById(R.id.back_add_meni);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        type_String = ManifestAdapter.type_String;

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menifest_txt.getText().toString().isEmpty()){
                    submit_btn.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
                }else {
                    apiAddManifestation();
                    Intent intent = new Intent(AddManifestation.this, DailyManifestation.class);
                    startActivity(intent);
                    finish();
                }
                submit_btn.setEnabled(true);
            }
        });

    }
    private void apiAddManifestation(){
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("text", menifest_txt.getText().toString());
        postParam.put("type", type_String);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.ADDMANIFESTATION, AddManifestation.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), "Successfully Completed", Toast.LENGTH_SHORT).show();

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
                    hideSoftKeyboard(AddManifestation.this);
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
