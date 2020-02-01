package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.idillionaire.app.Activities.ScheduleActivity;
import com.idillionaire.app.Activities.ScheduledDetailImage;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.Models.ScheduleModel;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SchedualAdapter extends RecyclerSwipeAdapter<SchedualAdapter.MyViewHolder> {
    Context scontext;
    ArrayList<ScheduleModel> sData;
    String complete_time;
    SharedPreferences sp;

    public static int fragment_three_count;
    public static int fragment_three_hour;
    public static int fragment_three_minute;

    int schedule_hour;
    int schedule_minute;
    String schedule_count;

    String activity_name;

    public static String listimage_to_schedule;
    public static String detailedimage_to_schedule;
    public static String galleryname_to_schedule;


    public SchedualAdapter(Context scontext, ArrayList<ScheduleModel> sData) {
        this.scontext = scontext;
        this.sData = sData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedual_items,parent,false);
        SchedualAdapter.MyViewHolder viewHolder = new SchedualAdapter.MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ScheduleModel items = sData.get(position);

            holder.schedual_count.setText("( " + sData.get(position).getCount() + " )");

            if (sData.get(position).getHour() > 12){
                complete_time = sData.get(position).getHour()-12 + " : " + sData.get(position).getMinute() + " PM";
                holder.stime.setText(complete_time);
            }else {
                complete_time = sData.get(position).getHour() + " : " + sData.get(position).getMinute() + " AM";
                holder.stime.setText(complete_time);
            }



        holder.stxt.setText(sData.get(position).getGallery_name());

        Picasso.with(scontext).load(Constants.URL.BASE_URL + sData.get(position).getList_image())
                .into(holder.simg);

        // Notifications


        // Notifications

        holder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
        holder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, holder.swipeLayout.findViewById(R.id.slide_right));
        holder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onStartOpen(SwipeLayout layout) {
                holder.next_btn.setVisibility(View.GONE);
            }

            @Override
            public void onOpen(SwipeLayout layout) {

            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onClose(SwipeLayout layout) {
                holder.next_btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {

            }
        });

        holder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                fragment_three_count = sData.get(position).getCount();
//                fragment_three_hour = sData.get(position).getHour();
//                fragment_three_minute = sData.get(position).getMinute();
//
//                detailedimage_to_schedule = sData.get(position).getDetailed_image();
//                listimage_to_schedule = sData.get(position).getList_image();
//                galleryname_to_schedule = sData.get(position).getGallery_name();

                Intent intent = new Intent(scontext, ScheduledDetailImage.class);
                intent.putExtra("gallery_name", sData.get(position).getGallery_name());
                intent.putExtra("listimage", sData.get(position).getList_image());
                intent.putExtra("detailedimage", sData.get(position).getDetailed_image());
                intent.putExtra("image_id", sData.get(position).get_id());
                scontext.startActivity(intent);
            }
        });
        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment_three_count = sData.get(position).getCount();
                fragment_three_hour = sData.get(position).getHour();
                fragment_three_minute = sData.get(position).getMinute();

                detailedimage_to_schedule = sData.get(position).getDetailed_image();
                listimage_to_schedule = sData.get(position).getList_image();
                galleryname_to_schedule = sData.get(position).getGallery_name();

                Intent intent = new Intent(scontext, ScheduleActivity.class);
                intent.putExtra("activity_name", "ScheduleAdapter");
                scontext.startActivity(intent);
            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(holder.swipeLayout);
                sData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, sData.size());
                mItemManger.closeAllItems();

                SharedPreferences sp = scontext.getSharedPreferences("SCHEDULE_ARR", Context.MODE_PRIVATE);
                Gson gson = new Gson();
                String araylist = gson.toJson(sData);
                sp.edit().putString("schedule_array", araylist).apply();
            }
        });
        mItemManger.bindView(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        try{
            return sData.size();
        }catch (Exception e){
            Log.e("your app", e.toString());
            return 0;
        }

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public SwipeLayout swipeLayout;
        public LinearLayout notes;
        public LinearLayout delete;

        public TextView stxt;
        public TextView stime;
        public ImageView simg;
        private ImageView next_btn;
        public TextView schedual_count;

        public MyViewHolder(View itemView) {
            super(itemView);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            notes = (LinearLayout) itemView.findViewById(R.id.edit);
            delete = (LinearLayout) itemView.findViewById(R.id.delete);

            stxt = (TextView) itemView.findViewById(R.id.schedual_txt);
            stime = (TextView) itemView.findViewById(R.id.schedual_time);
            simg = (ImageView) itemView.findViewById(R.id.schedual_img);
            next_btn = (ImageView) itemView.findViewById(R.id.schedual_next);
            schedual_count = (TextView) itemView.findViewById(R.id.schedual_count);

        }
    }
}
