package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idillionaire.app.Activities.StoryMode;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.Classes.SelectedCategoriesItems;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.StoryMediaImages;
import com.idillionaire.app.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.VISIBLE;

public class MagAdapter extends RecyclerView.Adapter<MagAdapter.MyViewHolder> {

    private Context context;
    public static ArrayList<Galleries> arr;
    public static String title;

    public static ArrayList<StoryMediaImages> story_media_images;

    public MagAdapter(Context context, ArrayList<Galleries> arr) {
        this.context = context;
        this.arr = arr;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.mag_items, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.mag_textview.setText(arr.get(position).getTitle_gallery());

        Glide.with(context)
                .load(Constants.URL.BASE_URL + arr.get(position).getMedia_file_name())
                .into(holder.mag_images);
        holder.mag_images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                story_media_images = new ArrayList<>();

                for (int u = position; u < arr.size(); u++) {

                    StoryMediaImages obj = new StoryMediaImages();

                    String title = arr.get(u).getTitle_gallery();
                    String media_file_name = arr.get(u).getMedia_file_name();
                    ArrayList<MediaFiles> mediaData = arr.get(u).getMedia_files();

                    obj.setTitle(title);
                    obj.setMedia_file_name(media_file_name);
                    obj.setMediaData(mediaData);

                    story_media_images.add(obj);
                }

                Intent storyintent = new Intent(context, StoryMode.class);
                storyintent.putExtra("Notification", "false");
                context.startActivity(storyintent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mag_images;
        TextView mag_textview;

        public MyViewHolder(View view) {
            super(view);
            mag_images = (ImageView) view.findViewById(R.id.mag_images);
            mag_textview = (TextView) view.findViewById(R.id.mag_textview);
        }
    }
}
