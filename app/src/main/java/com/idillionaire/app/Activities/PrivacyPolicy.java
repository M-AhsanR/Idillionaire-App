package com.idillionaire.app.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idillionaire.app.R;

public class PrivacyPolicy extends AppCompatActivity {

    TextView privacypolicy_text;
    String privacypolicy;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        privacypolicy_text = findViewById(R.id.privacyPolicy_text);
        privacypolicy = SplashActivity.privacyPolicy;
        privacypolicy_text.setText(privacypolicy);
        privacypolicy_text.setMovementMethod(new ScrollingMovementMethod());

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
