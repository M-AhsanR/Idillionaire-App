package com.idillionaire.app.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.constraint.solver.widgets.ConstraintAnchor;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.R;
import com.squareup.picasso.Picasso;

public class GetStarted extends AppCompatActivity {
   TextView name, firstline, secondline, thirdline;
   TextView get_started;
   ImageView getstarted_bg_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        name = (TextView) findViewById(R.id.name);
        firstline = (TextView) findViewById(R.id.firstline);
        secondline = (TextView) findViewById(R.id.secondline);
        thirdline = (TextView) findViewById(R.id.thirdline);
        get_started = (TextView) findViewById(R.id.get_started);
        getstarted_bg_img = (ImageView) findViewById(R.id.getstarted_bg_img);

        Picasso.with(this)
                .load(Constants.URL.BASE_URL + SplashActivity.p_getstarted_bg)
                .into(getstarted_bg_img);

        Typeface nametf = Typeface.createFromAsset(getAssets(), "Natural-Script-Bold.ttf");
        name.setTypeface(nametf);
        Typeface description = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        firstline.setTypeface(description);
        secondline.setTypeface(description);
        thirdline.setTypeface(description);
        get_started.setTypeface(description);

        get_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentstart = new Intent(getApplicationContext(), SignupActivity.class);
                startActivity(intentstart);
                finish();
            }
        });

    }

}
