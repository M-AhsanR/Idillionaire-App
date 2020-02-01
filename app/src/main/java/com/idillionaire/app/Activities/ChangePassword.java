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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.FavoriteList;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    EditText current_pass, new_pass, confirm_pass;
    TextView btn_update;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        TextView txt = (TextView) findViewById(R.id.title_pass);
        current_pass = (EditText) findViewById(R.id.current_pass);
        new_pass = (EditText) findViewById(R.id.new_pass);
        confirm_pass = (EditText) findViewById(R.id.confirm_pass);
        btn_update = (TextView) findViewById(R.id.update_btn);
        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        txt.setTypeface(font);
        current_pass.setTypeface(font);
        new_pass.setTypeface(font);
        confirm_pass.setTypeface(font);
        btn_update.setTypeface(font);

        RelativeLayout back = (RelativeLayout) findViewById(R.id.back_settings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (new_pass.getText().toString().isEmpty()|| confirm_pass.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Field is empty", Toast.LENGTH_SHORT).show();
                }else{
                    if (new_pass.getText().toString().equals(confirm_pass.getText().toString())){
                        apiChangepass();
                    }else {
                        Toast.makeText(getApplicationContext(), "Password is not matched", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        LinearLayout change_main_parent = findViewById(R.id.change_main_parent);
        setupUI(findViewById(R.id.change_main_parent));

    }
    private void apiChangepass(){

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN = mPrefs.getString("USER_TOKEN","");
        String EMAIL = mPrefs.getString("USER_EMAIL", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("email", EMAIL);
        postParam.put("old_password", current_pass.getText().toString());
        postParam.put("password", new_pass.getText().toString());
        postParam.put("device_token", "");
        postParam.put("platform", "android");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.CHANGEPASS, ChangePassword.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");
                        String token = jsonObject.getString("token");
                        JSONObject user_obj = jsonObject.getJSONObject("user");

                        String _id = user_obj.getString("_id");
                        Boolean confirmed = user_obj.getBoolean("confirmed");
                        String full_name = user_obj.getString("full_name");
                        String gender = user_obj.getString("gender");
                        Boolean admin_access = user_obj.getBoolean("admin_access");
                        JSONArray favourite_images = user_obj.getJSONArray("favourite_images");

                        ArrayList<FavoriteList> favoriteimage_arr = new ArrayList<>();
                        for (int i= 0; i < favourite_images.length(); i++){
                            FavoriteList favoriteList = new FavoriteList();
                            JSONObject favrtimagesObj = favourite_images.getJSONObject(i);
                            favoriteList.set_id(favrtimagesObj.getString("_id"));
                            favoriteList.setTitle(favrtimagesObj.getString("title"));
                            favoriteList.setImage_id(favrtimagesObj.getString("image_id"));
                            favoriteList.setDetailed_img_name(favrtimagesObj.getString("Detailed_img_name"));
                            favoriteList.setList_img(favrtimagesObj.getString("list_img"));
                            favoriteList.setOrder_number(favrtimagesObj.getInt("order_number"));
                            favoriteimage_arr.add(favoriteList);
                        }

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_PASSWORD", new_pass.getText().toString());
                        mEditor.putString("USER_TOKEN", token);
                        mEditor.putString("USER_ID", _id);
                        mEditor.apply();

                        Toast.makeText(getApplicationContext(), "Password changed", Toast.LENGTH_SHORT).show();
                        finish();

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
                    hideSoftKeyboard(ChangePassword.this);
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
