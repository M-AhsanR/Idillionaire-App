package com.idillionaire.app.Activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportProblem extends AppCompatActivity {

    TextView report_send;
    EditText report_text;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_problem);

        report_send = findViewById(R.id.report_send);
        report_text = findViewById(R.id.report_text);

        report_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                apiReportProblem();
                finish();
            }
        });

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void apiReportProblem(){
        SharedPreferences mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN = mprefs.getString("USER_TOKEN", "");

        Map<String, String> postpram = new HashMap<String, String>();
        postpram.put("text", report_text.getText().toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.REPORTPROBLEM, ReportProblem.this, postpram, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        Toast.makeText(getApplicationContext(), "Report Sent", Toast.LENGTH_SHORT).show();

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
