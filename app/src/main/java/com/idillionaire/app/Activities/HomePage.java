package com.idillionaire.app.Activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.Image;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.idillionaire.app.Adapters.PagerAdapterr;
import com.idillionaire.app.Fragments.TwoFragment;
import com.idillionaire.app.R;


public class HomePage extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    ImageView tab_icon;
    PagerAdapterr adapter;
    String noti_title;
    String noti_msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.home_icon_selected));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.like));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.schedule));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.magzine));
        tabLayout.addTab(tabLayout.newTab().setCustomView(R.layout.custom_tab).setIcon(R.drawable.settings));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
        for(int i = 0; i < 5; i++) {
            TabLayout.Tab tabb = tabLayout.getTabAt(i); // fourth tab
            View tabView = tabb.getCustomView();
            tab_icon = tabView.findViewById(R.id.tab_icon);

            if (i == 0) {tab_icon.setImageResource(R.drawable.home_icon_selected);}
            if (i == 1) {tab_icon.setImageResource(R.drawable.like);}
            if (i == 2) {tab_icon.setImageResource(R.drawable.schedule);}
            if (i == 3) {tab_icon.setImageResource(R.drawable.magzine);}
            if (i == 4) {tab_icon.setImageResource(R.drawable.settings);}

        }
        adapter = new PagerAdapterr(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setOffscreenPageLimit(5);         /* limit is a fixed integer*/

        viewPager.setAdapter(adapter);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                int pos=tab.getPosition();

                if (tab.getPosition() == 0){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.home_icon_selected);
//                    tab.setIcon(R.drawable.home_blue);


                }
                if(tab.getPosition() == 1){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition()); // fourth tab
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);

                    tab_icon.setImageResource(R.drawable.like_filled);

//                    tab.setIcon(R.drawable.search_blue);


                }
                if(tab.getPosition() == 2){

                    SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                    String purchase_token = sp.getString("PURCHASETOKEN", "");

                    if(!purchase_token.isEmpty()){
                        TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                        View tabView = tabb.getCustomView();
                        tab_icon = tabView.findViewById(R.id.tab_icon);
                        tab_icon.setImageResource(R.drawable.schedule_filled);
                    }else {

                        TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                        View tabView = tabb.getCustomView();
                        tab_icon = tabView.findViewById(R.id.tab_icon);
                        tab_icon.setImageResource(R.drawable.schedule_filled);

                        Intent intent = new Intent(HomePage.this, PremiumActivity.class);
                        intent.putExtra("premium", "Homepage");
                        startActivity(intent);
                    }

                }
                if(tab.getPosition() == 3){

                    SharedPreferences sp = getSharedPreferences("PURCHASEDATA", MODE_PRIVATE);
                    String purchase_token = sp.getString("PURCHASETOKEN", "");

                    if(!purchase_token.isEmpty()){
                        TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                        View tabView = tabb.getCustomView();
                        tab_icon = tabView.findViewById(R.id.tab_icon);
                        tab_icon.setImageResource(R.drawable.magzine_filled);
                    }else {

                        TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                        View tabView = tabb.getCustomView();
                        tab_icon = tabView.findViewById(R.id.tab_icon);
                        tab_icon.setImageResource(R.drawable.magzine_filled);

                        Intent intent = new Intent(HomePage.this, PremiumActivity.class);
                        intent.putExtra("premium", "Homepage");
                        startActivity(intent);
                    }

                }
                if(tab.getPosition() == 4){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.settings_filled);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                int pos=tab.getPosition();

                if(tab.getPosition() == 0){
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.home_icon);
                }
                if(tab.getPosition() == 1){
//                    tab.setIcon(R.drawable.search_gray);
                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.like);

                }
                if(tab.getPosition() == 2){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.schedule);
                }
                if(tab.getPosition() == 3){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.magzine);

                }
                if(tab.getPosition() == 4){

                    TabLayout.Tab tabb = tabLayout.getTabAt(tab.getPosition());
                    View tabView = tabb.getCustomView();
                    tab_icon = tabView.findViewById(R.id.tab_icon);
                    tab_icon.setImageResource(R.drawable.settings);

                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        Bundle bundle = getIntent().getExtras();
        boolean fromNotification = bundle.getBoolean("fromNotification");

        if (fromNotification){

            noti_msg = getIntent().getStringExtra("msg");
            noti_title = getIntent().getStringExtra("title");

            final AlertDialog.Builder builder = new AlertDialog.Builder(HomePage.this);
            LayoutInflater inflater = HomePage.this.getLayoutInflater();
            View dailogview = inflater.inflate(R.layout.notification_dialogue, null);
            builder.setView(dailogview);

            final AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView cancel = dailogview.findViewById(R.id.cancel_btn);
            TextView title_txt = dailogview.findViewById(R.id.noti_title);
            TextView msg_txt = dailogview.findViewById(R.id.noti_msg);
            alertDialog.show();

            Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
            title_txt.setTypeface(font);
            msg_txt.setTypeface(font);
            cancel.setTypeface(font);

            title_txt.setText(noti_title);
            msg_txt.setText(noti_msg);

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                }
            });


        }else {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            tab.select();
        }
    }
}
