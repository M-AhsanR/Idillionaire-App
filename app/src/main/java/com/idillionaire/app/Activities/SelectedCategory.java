package com.idillionaire.app.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idillionaire.app.Adapters.HomeViewAdapter;
import com.idillionaire.app.Adapters.MagAdapter;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.Classes.GridSpacingItemDecoration;
import com.idillionaire.app.Models.Categories;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class SelectedCategory extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NestedScrollView scrollView;
    private RelativeLayout backhome;
    private ArrayList<Galleries> galleriesList;
    private TextView title_selected;
    private ImageView headerr;
    public String title;
    public String detailed_image;
    Gson gson;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_category);

        // Title Bar

        title_selected = (TextView) findViewById(R.id.title_selected);
        backhome = (RelativeLayout) findViewById(R.id.back_home);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_selected = (TextView) findViewById(R.id.title_selected);
        title = HomeViewAdapter.sharedData;
        title_selected.setText(title);

        // Title Bar

        // Detailed Image

        headerr = (ImageView) findViewById(R.id.headerr);
        detailed_image = HomeViewAdapter.sharedImage;
        Picasso.with(SelectedCategory.this)
                .load(Constants.URL.BASE_URL + detailed_image)
                .into(headerr);

        // Detailed Image

        // RecyclerView/ScrollView

        scrollView = findViewById(R.id.nestedscrollView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        scrollView.setSmoothScrollingEnabled(true);
        recyclerView.setFocusable(false);

        galleriesList = HomeViewAdapter.gData;
        MagAdapter magAdapter = new MagAdapter(SelectedCategory.this, galleriesList);
        recyclerView.setAdapter(magAdapter);
        GridLayoutManager MyLayoutManager = new GridLayoutManager(SelectedCategory.this, 2);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 2; // 50px
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

        // RecyclerView/ScrollView


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
            finish();
    }
}
