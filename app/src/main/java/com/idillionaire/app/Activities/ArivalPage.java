package com.idillionaire.app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.ArivalAdapter;
import com.idillionaire.app.Adapters.JournalListAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Fragments.FourFragment;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArivalPage extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RelativeLayout back;
    List<String> add_text_arr;
    LinearLayout main_parent;
    RelativeLayout save;

    public static String text;

    ArrayList<DailyGratitudeItems> gratitudeItemsList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (gratitudeItemsList.isEmpty()){
            finish();
        }else {
            Intent intent = new Intent(ArivalPage.this, DailyGratitude.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arival_page);

        main_parent = (LinearLayout) findViewById(R.id.main_parent);
        setupUI(findViewById(R.id.main_parent));

        gratitudeItemsList = FourFragment.gratitudeItemsList;

        back = (RelativeLayout) findViewById(R.id.back_arival);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gratitudeItemsList.isEmpty()){
                    finish();
                }else {
                    Intent intent = new Intent(ArivalPage.this, DailyGratitude.class);
                    startActivity(intent);
                    finish();
                }
            }
        });

        add_text_arr = new ArrayList<>();
        add_text_arr.add(new String(""));


        recyclerView = (RecyclerView) findViewById(R.id.today_recycler);
        final ArivalAdapter arivalAdapter = new ArivalAdapter(this, add_text_arr);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(arivalAdapter);
        recyclerView.setEnabled(false);
        recyclerView.smoothScrollToPosition(add_text_arr.size()-1);
        arivalAdapter.notifyDataSetChanged();

        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                boolean empty = false;

                for (int i = 0; i < add_text_arr.size(); i++){

                    String textfield = add_text_arr.get(i);
                    if (textfield.isEmpty()){
                        empty = true;
                    }
                }
                if (empty){
                    save.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "Enter text", Toast.LENGTH_SHORT).show();
                }else {
                    apiAddGratitude();
                    Intent intent = new Intent(ArivalPage.this, DailyGratitude.class);
                    startActivity(intent);
                    finish();
                }
                save.setEnabled(true);



//                if (add_text_arr.get(add_text_arr.size()).isEmpty()){
//                    save.setEnabled(false);
//                }else {
//                    apiAddGratitude();
//                    Intent intent = new Intent(ArivalPage.this, DailyGratitude.class);
//                    startActivity(intent);
//                    finish();
//                }
            }
        });

    }

    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(ArivalPage.this);
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

    private void apiAddGratitude(){

        SharedPreferences mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN = mprefs.getString("USER_TOKEN", "");

        Map<String, List<String>> postParam = new HashMap<String, List<String>>();
        postParam.put("texts", add_text_arr);

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponseArray(Request.Method.POST, Constants.URL.ADDGRATITUDE, ArivalPage.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        JSONObject gratitudeObj = jsonObject.getJSONObject("gratitude");
                        String _id = gratitudeObj.getString("_id");
                        String user = gratitudeObj.getString("user");
                        JSONArray texts = gratitudeObj.getJSONArray("texts");
                        String date = gratitudeObj.getString("date");
                        List<String> add_text = new ArrayList<>();
                        for (int i = 0; i < texts.length(); i++){
                            add_text.add(String.valueOf(texts.get(i)));
                        }

                        Toast.makeText(getApplicationContext(), "Gratitude Successfully Added", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
