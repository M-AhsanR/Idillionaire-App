package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.idillionaire.app.Activities.AddManifestation;
import com.idillionaire.app.Activities.DailyManifestation;
import com.idillionaire.app.Activities.ManifestaionPage;
import com.idillionaire.app.Classes.ManifestItems;
import com.idillionaire.app.Models.DailyManifestItems;
import com.idillionaire.app.R;

import java.util.ArrayList;
import java.util.List;

public class ManifestAdapter extends RecyclerView.Adapter<ManifestAdapter.MyViewHolder> {
    Context mcontext;
    List<ManifestItems> mdata;
    ArrayList<DailyManifestItems> public_arr;
//    public static ArrayList<DailyManifestationsList> dailyManifestationsLists;
//    public static ArrayList<WeeklyManifestationsList> weeklyManifestationsLists;
//    public static ArrayList<MonthlyManifestations> monthlyManifestationsLists;
//    public static ArrayList<YearlyManifestations> yearlyManifestationsLists;
    public static String addMainString;
    public static String title_barString;
    public static String type_String;

    public ManifestAdapter(Context mcontext, List<ManifestItems> mdata) {
        this.mcontext = mcontext;
        this.mdata = mdata;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mcontext).inflate(R.layout.manifest_items,parent,false);
        ManifestAdapter.MyViewHolder mHolder = new ManifestAdapter.MyViewHolder(v);
        return mHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        holder.mtxt.setText(mdata.get(position).getMenifesttxt());
        holder.mimg.setBackgroundResource(mdata.get(position).getMenifestimages());
        if (position == 0){
            holder.mimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    public_arr = ManifestaionPage.dailyManifestationsLists;
                    if (public_arr == null || public_arr.isEmpty()){
                        addMainString = "What do you want to manifest today?";
                        title_barString = "Daily Manifestation";
                        type_String = "Daily Manifestation";
                        Intent addintent = new Intent(mcontext, AddManifestation.class);
                        mcontext.startActivity(addintent);
                    }else {
                        addMainString = "What do you want to manifest today?";
                        title_barString = "Daily Manifestation";
                        type_String = "Daily Manifestation";
                        Intent intent = new Intent(mcontext, DailyManifestation.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }
        if (position == 1){
            holder.mimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    public_arr = ManifestaionPage.weeklyManifestationsLists;
                    if (public_arr.isEmpty()){
                        addMainString = "What do you want to manifest this week?";
                        title_barString = "Weekly Manifestation";
                        type_String = "Weekly Manifestation";
                        Intent addintent = new Intent(mcontext, AddManifestation.class);
                        mcontext.startActivity(addintent);
                    }else {
                        addMainString = "What do you want to manifest this week?";
                        title_barString = "Weekly Manifestation";
                        type_String = "Weekly Manifestation";
                        Intent intent = new Intent(mcontext, DailyManifestation.class);
                        mcontext.startActivity(intent);
                    }

                }
            });
        }
        if (position == 2){
            holder.mimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    public_arr = ManifestaionPage.monthlyManifestationsLists;
                    if (public_arr.isEmpty()){
                        addMainString = "What do you want to manifest this month?";
                        title_barString = "Monthly Manifestation";
                        type_String = "Monthly Manifestation";
                        Intent addintent = new Intent(mcontext, AddManifestation.class);
                        mcontext.startActivity(addintent);
                    }else {
                        addMainString = "What do you want to manifest this month?";
                        title_barString = "Monthly Manifestation";
                        type_String = "Monthly Manifestation";
                        Intent intent = new Intent(mcontext, DailyManifestation.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }
        if (position == 3){
            holder.mimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    public_arr = ManifestaionPage.yearlyManifestationsLists;
                    if (public_arr.isEmpty()){
                        addMainString = "What do you want to manifest this year?";
                        title_barString = "Yearly Manifestation";
                        type_String = "Yearly Manifestation";
                        Intent addintent = new Intent(mcontext, AddManifestation.class);
                        mcontext.startActivity(addintent);
                    }else {
                        addMainString = "What do you want to manifest this year?";
                        title_barString = "Yearly Manifestation";
                        type_String = "Yearly Manifestation";
                        Intent intent = new Intent(mcontext, DailyManifestation.class);
                        mcontext.startActivity(intent);
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView mtxt;
        private RelativeLayout mimg;

        public MyViewHolder(View itemView) {
            super(itemView);
            mtxt = (TextView) itemView.findViewById(R.id.manifest_txt);
            mimg = (RelativeLayout) itemView.findViewById(R.id.menifest_image);
        }
    }

}
