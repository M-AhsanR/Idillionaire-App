package com.idillionaire.app.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idillionaire.app.R;

public class TermstoUse extends AppCompatActivity {

    TextView termstouse_text;
    String termstouse;
    RelativeLayout back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_termsto_use);

        termstouse_text = findViewById(R.id.termstoUse_text);
        termstouse = SplashActivity.termstoUse;
        termstouse_text.setText(termstouse);
        termstouse_text.setMovementMethod(new ScrollingMovementMethod());



        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
