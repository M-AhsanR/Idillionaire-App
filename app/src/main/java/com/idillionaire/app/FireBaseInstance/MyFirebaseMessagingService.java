package com.idillionaire.app.FireBaseInstance;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.idillionaire.app.Activities.HomePage;
import com.idillionaire.app.Activities.SelectedCategory;
import com.idillionaire.app.Activities.StoryMode;
import com.idillionaire.app.R;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG="";
    String gallery_id;
    String flag;
    Intent intent;
    String title;
    String msg;
    NotificationChannel mChannel;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        sendNotification(remoteMessage);



    }
    private void sendNotification(RemoteMessage message) {

        title = message.getData().get("title");
        msg = message.getData().get("message");
        gallery_id=message.getData().get("gallery_id");
        flag=message.getData().get("flag");
//        Log.e(TAG, "GET ID" + gallery_id);

        // set badge count
        SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount", Context.MODE_PRIVATE);

        int count=mPrefs.getInt("count",0);
        count=count+1;

        SharedPreferences.Editor editor=mPrefs.edit();
        editor.putInt("count",count);
        editor.apply();

        // set badge count

        if (flag.equals("true")){

            intent = new Intent(this, HomePage.class);
            intent.putExtra("title", title);
            intent.putExtra("msg", msg);
            intent.putExtra("fromNotification", true);

        }else {
            intent = new Intent(this, StoryMode.class);
            intent.putExtra("Gallery_id", gallery_id);
            intent.putExtra("Notification", "true");
        }

        String channel_ID = "idillionaire_channel";
        CharSequence name = "Push Notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channel_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.YELLOW);
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificatin_builder=new NotificationCompat.Builder(this, channel_ID);
        notificatin_builder
                .setSmallIcon(R.drawable.logo)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.icon_main))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(msg)
                .setSound(defaultSoundUri)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setChannelId(channel_ID)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())

                .setPriority(android.app.Notification.PRIORITY_HIGH);

        NotificationManager notification_manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification_manager.notify(0,notificatin_builder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification_manager.createNotificationChannel(mChannel);
        }
    }
}

