package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import com.idillionaire.app.Fragments.FiveFragment;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText change_name;
    TextView change_email;
    TextView btn_update;
    LinearLayout edit_main_parent;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        TextView txt = (TextView) findViewById(R.id.title_profile);
        change_name = (EditText) findViewById(R.id.change_name);
        change_email = (TextView) findViewById(R.id.change_email);
        btn_update = (TextView) findViewById(R.id.update_btn);

        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");

        txt.setTypeface(font);
        change_name.setTypeface(font);
        change_email.setTypeface(font);
        btn_update.setTypeface(font);

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String EMAIL = mPrefs.getString("USER_EMAIL","");
        String NAME = mPrefs.getString("NAME", "");

        change_email.setText(EMAIL);
        change_name.setText(NAME);


        back = (RelativeLayout) findViewById(R.id.back_settings);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiEditProfile();

            }
        });

        edit_main_parent = findViewById(R.id.edit_main_parent);
        setupUI(findViewById(R.id.edit_main_parent));
    }


    private void apiEditProfile(){
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN = mPrefs.getString("USER_TOKEN","");
        String EMAIL = mPrefs.getString("USER_EMAIL","");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("full_name", change_name.getText().toString());
//        postParam.put("active_dp", "");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.EDITPROFILE, EditProfile.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    SharedPreferences sp = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    sp.edit().putString("NAME", change_name.getText().toString()).apply();


                    Toast.makeText(getApplicationContext(), "Profile Edited Successfully", Toast.LENGTH_SHORT).show();
                    finish();
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
                    hideSoftKeyboard(EditProfile.this);
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

