package com.idillionaire.app.Notificatins;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.idillionaire.app.Activities.DetailedImage;
import com.idillionaire.app.Activities.HomePage;
import com.idillionaire.app.Activities.LoginActivity;
import com.idillionaire.app.Activities.ScheduledDetailImage;
import com.idillionaire.app.Models.ScheduleModel;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.MODE_PRIVATE;

public class NotificationReciever extends BroadcastReceiver{

    SharedPreferences sp;
    Gson gson = new Gson();
    Type type;
    ArrayList<ScheduleModel> item_list;
    int codee;
    NotificationChannel mChannel;


    String detailed_image;
    String list_image;
    String image_id;
    String gallery_name;

    int position = 0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {

        // Share Data

        int code=intent.getIntExtra("code",0);
         codee= code;

        sp = context.getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);
        String arr = sp.getString("schedule_array", "");
        type = new TypeToken<ArrayList<ScheduleModel>>(){}.getType();

        item_list = gson.fromJson(arr, type);

        // Share Data
        for(int a=0;a<item_list.size();a++){

            int chk_code = item_list.get(a).getCode();
            if(chk_code == codee){
                int present_count= item_list.get(a).getCount();
                position = a;
                detailed_image = item_list.get(position).getDetailed_image();
                list_image = item_list.get(position).getList_image();
                image_id = item_list.get(position).get_id();
                gallery_name = item_list.get(position).getGallery_name();


                if(present_count == 0){
                    // notification end
                }
                else {
//                    int index = a;
//                  then set again notification
                    codee=item_list.get(a).getCode();
                    item_list.get(a).setCount(item_list.get(a).getCount()-1);
                    sendNotification(context,item_list.get(a).getHour(), item_list.get(a).getMinute(), item_list.get(a).getCode());

                    position = a;
                    detailed_image = item_list.get(position).getDetailed_image();
                    list_image = item_list.get(position).getList_image();
                    image_id = item_list.get(position).get_id();
                    gallery_name = item_list.get(position).getGallery_name();

                    // store in shared

                    if (item_list.get(a).getCount() < 1){
                        position = a;
                        detailed_image = item_list.get(position).getDetailed_image();
                        list_image = item_list.get(position).getList_image();
                        image_id = item_list.get(position).get_id();
                        gallery_name = item_list.get(position).getGallery_name();
                        item_list.remove(a);

                    }



                    sp = context.getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);

                    String list = gson.toJson(item_list);
                    sp.edit().putString("schedule_array", list).apply();

                    // store in shared

                }
            }


        }

//        if (position != 0) {
//            detailed_image = item_list.get(position).getDetailed_image();
//            list_image = item_list.get(position).getList_image();
//            image_id = item_list.get(position).get_id();
//            gallery_name = item_list.get(position).getGallery_name();
//        }

        if (detailed_image != null) {
            String channel_ID = "idillionaire_channel";
            CharSequence name = "Scheduled Notification";
            int importance = NotificationManager.IMPORTANCE_HIGH;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                mChannel = new NotificationChannel(channel_ID, name, importance);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.YELLOW);
                mChannel.setShowBadge(true);
                mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            }

            Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            sp = context.getSharedPreferences("login", MODE_PRIVATE);

            Intent repeating_intent = new Intent(context, ScheduledDetailImage.class);
            repeating_intent.putExtra("gallery_name", gallery_name);
            repeating_intent.putExtra("image_id", image_id);
            repeating_intent.putExtra("detailedimage", detailed_image);
            repeating_intent.putExtra("listimage", list_image);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, codee, repeating_intent, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, channel_ID)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.drawable.logo)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_main))
                    .setSound(sound)
                    .setContentText("Scheduled Notification")
                    .setContentTitle("Idillionaire")
                    .setPriority(android.app.Notification.PRIORITY_HIGH)
                    .setChannelId(channel_ID)
                    .setAutoCancel(true);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(mChannel);
            }


            notificationManager.notify(codee, builder.build());
        }

    }

    public void sendNotification(Context ctxt,int hour,int minute, int codee) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 00);

        if (calendar.getTime().compareTo(new Date()) < 0){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(ctxt, NotificationReciever.class);
        intent.putExtra("code", codee);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctxt, codee, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) ctxt.getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }

}
