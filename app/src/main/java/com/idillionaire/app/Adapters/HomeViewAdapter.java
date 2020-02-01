package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idillionaire.app.Activities.SelectedCategory;
import com.idillionaire.app.Activities.SplashActivity;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.Fragments.OneFragment;
import com.idillionaire.app.Models.Categories;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeViewAdapter extends RecyclerView.Adapter<HomeViewAdapter.MyViewHolder> {

    Context mContext;
    public static ArrayList<Categories> mData;
    public static String sharedData;
    public static String sharedImage;
    public static ArrayList<Galleries> gData;

//    RequestOptions option;

    public HomeViewAdapter(Context mContext, ArrayList<Categories> mData, ArrayList<Galleries> gData) {
        this.mData = mData;
        this.mContext = mContext;
        this.gData = gData;


//        notifyItemRangeChanged(0, mData.size());
//        option = new RequestOptions().centerCrop().paddingLefaceholder(R.drawable.loading_shape).error(R.drawable.loading_shape);

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(mContext).inflate(R.layout.home_list_items,parent,false);
        MyViewHolder vHolder = new MyViewHolder(v);
        return vHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        Categories currentFeed = mData.get(position);
        holder.tv_text.setText(currentFeed.getTitle());

//        holder.img_view.setImageResource(Integer.parseInt(currentFeed.getList_image()));
//        holder.first_img.setBackgroundResource(R.drawable.home1);


        if (SplashActivity.homebg != null){
            Picasso.with(mContext)
                    .load(Constants.URL.BASE_URL + SplashActivity.homebg)
                    .into(holder.first_img);
        }else {
            Picasso.with(mContext)
                    .load(Constants.URL.BASE_URL + OneFragment.home_bg)
                    .into(holder.first_img);
        }




        Glide.with(mContext)
                .load(Constants.URL.BASE_URL+mData
                .get(position)
                .getList_image())
                .into(holder.img_view);

        if(position == 0){
            holder.first_layout.setVisibility(View.VISIBLE);
        }
        else {
            holder.first_layout.setVisibility(View.GONE);

        }
//        holder.tv_text.setText(mData.get(position).getHometxt());
//        holder.img_view.setBackgroundResource(Integer.parseInt(mData.get(position).getImages()));

            holder.img_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sharedData = mData.get(position).getTitle();
                    sharedImage = mData.get(position).getDetail_image();
                    gData=mData.get(position).getGalleries();
                    Intent selected_category = new Intent(mContext, SelectedCategory.class);
                    mContext.startActivity(selected_category);
                }
            });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tv_text;
        private ImageView img_view;
        private ImageView first_img;
        private RelativeLayout first_layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_text = (TextView) itemView.findViewById(R.id.txtview);
            img_view = (ImageView) itemView.findViewById(R.id.imageview);
            first_img = (ImageView) itemView.findViewById(R.id.first_img);
            first_layout = (RelativeLayout) itemView.findViewById(R.id.first_layout);


        }
    }


}
