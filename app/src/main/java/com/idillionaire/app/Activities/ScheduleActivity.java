package com.idillionaire.app.Activities;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.idillionaire.app.Adapters.SchedualAdapter;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.ScheduleModel;
import com.idillionaire.app.Notificatins.NotificationReciever;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class ScheduleActivity extends AppCompatActivity {

    RelativeLayout done;
    RelativeLayout cancle;
    TimePicker timePicker;
    RelativeLayout up_counter;
    RelativeLayout down_counter;
    TextView counter_text;
    Calendar calendar;

    ScheduleModel scheduleModel;

    int hours;
    int minutes;
    int n;
    int codee;

    public static ArrayList<ScheduleModel> items_list;

    String detail_image;
    String list_image;
    String gallery_name;
    String activity_name;
    String id;
    Gson gson = new Gson();
    SharedPreferences sp;

    int count_numbers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);


        // Counter

        n = 1;
        counter_text = findViewById(R.id.counter_text);
        up_counter = findViewById(R.id.up_counter);
        down_counter = findViewById(R.id.down_counter);

        counter_text.setText(String.valueOf(n));
        count_numbers = Integer.parseInt(counter_text.getText().toString());

        up_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                n++;
                counter_text.setText(String.valueOf(n));
                count_numbers = Integer.parseInt(counter_text.getText().toString());
            }
        });
        down_counter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (n >= 2) {
                    n--;
                    counter_text.setText(String.valueOf(n));
                    count_numbers = Integer.parseInt(counter_text.getText().toString());
                }
            }
        });

        // Counter

        // TimePicker

        timePicker = findViewById(R.id.time_picker);

        hours = timePicker.getCurrentHour();
        minutes = timePicker.getCurrentMinute();

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hours = timePicker.getCurrentHour();
                minutes = timePicker.getCurrentMinute();
            }
        });

        Bundle bundle = getIntent().getExtras();
        activity_name = bundle.getString("activity_name");
        if (activity_name.equals("DetailedImage")) {
            detail_image = DetailedImage.schedule_detail_image;
            list_image = DetailedImage.schedule_list_image;
            gallery_name = DetailedImage.schedule_gallery_name;
            id = DetailedImage.schedule_image_id;
            activity_name = "";

        }
        if (activity_name.equals("StoryMode")) {
            detail_image = StoryMode.schedule_detail_image;
            list_image = StoryMode.schedule_list_image;
            gallery_name = StoryMode.schedule_gallery_name;
            id = StoryMode.schedule_image_id;
            activity_name = "";
        }
//        if (activity_name.equals("ScheduleAdapter")) {
//            detail_image = SchedualAdapter.detailedimage_to_schedule;
//            list_image = SchedualAdapter.listimage_to_schedule;
//            gallery_name = SchedualAdapter.galleryname_to_schedule;
//
//            count_numbers = SchedualAdapter.fragment_three_count;
//            hours = SchedualAdapter.fragment_three_hour;
//            minutes = SchedualAdapter.fragment_three_minute;
//
//            counter_text.setText(String.valueOf(count_numbers));
//            n = count_numbers;
//
//            timePicker.setCurrentHour(hours);
//            timePicker.setCurrentMinute(minutes);
//            activity_name = "";
//        }
        if (activity_name.equals("PremiumActivity")){

            detail_image = PremiumActivity.schedule_detail_image;
            list_image = PremiumActivity.schedule_list_image;
            gallery_name = PremiumActivity.schedule_gallery_name;
            id = getIntent().getStringExtra("main_id");
            activity_name = "";

        }
        if (activity_name.equals("ScheduleDetailImage")) {
            detail_image = ScheduledDetailImage.detailed_image;
            list_image = ScheduledDetailImage.listimage;
            gallery_name = ScheduledDetailImage.galleryname;
            id = ScheduledDetailImage.schedule_detail_image_id;
            activity_name = "";

        }

        sp = getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);
        String array = sp.getString("schedule_array", "");
        Type type = new TypeToken<List<ScheduleModel>>() {
        }.getType();

        String substring = id.substring(14, 24);

        try {
            codee = Integer.parseInt(substring.replaceAll("[\\D]",""));
        }catch (NumberFormatException nfe){
            codee = 837237;
        }

        if (array.equals("[]") || array.isEmpty()) {
            items_list = new ArrayList<>();
//            codee = Integer.parseInt(id);
        } else {
            items_list = gson.fromJson(array, type);
//            codee = items_list.get(items_list.size() - 1).getCode();
        }

        // Test

        scheduleModel = new ScheduleModel();


        done = findViewById(R.id.done_schedule);
        cancle = findViewById(R.id.cancle_schedule);
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            //            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                // Saving the data

                if (items_list.isEmpty()) {
                    scheduleModel.setDetailed_image(detail_image);
                    scheduleModel.setList_image(list_image);
                    scheduleModel.setGallery_name(gallery_name);
                    scheduleModel.setCount(count_numbers);
                    scheduleModel.setHour(hours);
                    scheduleModel.setMinute(minutes);
                    scheduleModel.setCode(codee);
                    scheduleModel.set_id(id);
                    items_list.add(scheduleModel);

                    sp = getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);
                    String araylist = gson.toJson(items_list);
                    sp.edit().putString("schedule_array", araylist).apply();
                    sendNotification(scheduleModel.getHour(), scheduleModel.getMinute(), scheduleModel.getCode());
                    finish();
                } else {
                    boolean sameExist = false;
                    int samePosition = 0;

                    for (int i = 0; i < items_list.size(); i++) {
                        String detailed_image_of_list = items_list.get(i).getDetailed_image();
                        if (detailed_image_of_list.equals(detail_image)) {
                            sameExist = true;
                            samePosition = i;
                        }
                    }
                    if (sameExist) {
                        items_list.get(samePosition).setMinute(minutes);
                        items_list.get(samePosition).setHour(hours);
                        items_list.get(samePosition).setCount(count_numbers);

                        sendNotification(items_list.get(samePosition).getHour(), items_list.get(samePosition).getMinute(), items_list.get(samePosition).getCode());

                    } else {
                        scheduleModel.setDetailed_image(detail_image);
                        scheduleModel.setList_image(list_image);
                        scheduleModel.setGallery_name(gallery_name);
                        scheduleModel.setCount(count_numbers);
                        scheduleModel.setHour(hours);
                        scheduleModel.setMinute(minutes);
                        scheduleModel.set_id(id);
                        scheduleModel.setCode(codee + 55);
                        items_list.add(scheduleModel);

                        sendNotification(scheduleModel.getHour(), scheduleModel.getMinute(), scheduleModel.getCode());
                    }

                    sp = getSharedPreferences("SCHEDULE_ARR", MODE_PRIVATE);
                    String araylist = gson.toJson(items_list);
                    sp.edit().putString("schedule_array", araylist).apply();
                    finish();
                }
                // Saving the data

            }
        });
    }

    public void sendNotification(int hours, int minutes, int codee) {

        Calendar calendar = Calendar.getInstance();

        calendar.set(Calendar.HOUR_OF_DAY, hours);
        calendar.set(Calendar.MINUTE, minutes);
        calendar.set(Calendar.SECOND, 00);

        if (calendar.getTime().compareTo(new Date()) < 0){
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        Intent intent = new Intent(getApplicationContext(), NotificationReciever.class);
        intent.putExtra("code",codee);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), codee, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
}
