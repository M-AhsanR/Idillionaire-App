package com.idillionaire.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
import com.idillionaire.app.Activities.ManifestDetail;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.DailyManifestItems;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class DailyManifestAdapter extends RecyclerSwipeAdapter<DailyManifestAdapter.MyViewHolder>{
    Context dcontext;
    ArrayList<DailyManifestItems> dData;
    public static String id_String;
    public static String manifest_text;
    String update_manifest_text;
    String mani_Date;
    String type_string;
    String truefalse;
    public static boolean completion_state;
    ArrayList<DailyManifestItems> update_list_items;

    ArrayList<DailyManifestItems> update_dailyManifestationsLists;
    ArrayList<DailyManifestItems> update_weeklyManifestationsLists;
    ArrayList<DailyManifestItems> update_monthlyManifestationsLists;
    ArrayList<DailyManifestItems> update_yearlyManifestationsLists;

    public DailyManifestAdapter(Context dcontext, ArrayList<DailyManifestItems> dData) {
        this.dcontext = dcontext;
        this.dData = dData;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_manifest_items,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        // set time //////////////////////////

        String tim = dData.get(position).getDate();
        String dat=tim.substring(0,10);
        String time=tim.substring(11,19);
        String fin=dat+" "+time;

        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;

        try {
            date = dff.parse(fin);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dff.setTimeZone(TimeZone.getDefault());
        String formattedDatee = dff.format(date);
        fin=formattedDatee;


        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        String final_date;
        SimpleDateFormat ddf = new SimpleDateFormat("MMM dd, yyyy");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        final_date = ddf.format(calendar.getTime());
        holder.ddate.setText(final_date);

        // set time //////////////////////////


        type_string = ManifestAdapter.type_String;
        id_String = dData.get(position).get_id();
        truefalse = ManifestDetail.truefalse;

        holder.dtxt.setText(dData.get(position).getText());


        if (dData.get(position).getCompletion_status() == true || truefalse == "true"){

            holder.info_img.setImageResource(R.drawable.mani_done);
        }else {
            holder.info_img.setImageResource(R.drawable.mani_no);
        }
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
                id_String = dData.get(position).get_id();
                completion_state = dData.get(position).getCompletion_status();
                manifest_text = dData.get(position).getText();
                Intent detailIntent = new Intent(dcontext, ManifestDetail.class);
                dcontext.startActivity(detailIntent);
                ((Activity)dcontext).finish();
            }
        });
        holder.compl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_String = dData.get(position).get_id();
                update_manifest_text = dData.get(position).getText();
                type_string = ManifestAdapter.type_String;
                apiUpdateManifestation();
                holder.info_img.setImageResource(R.drawable.mani_done);
                holder.swipeLayout.close();
                completion_state = dData.get(position).getCompletion_status();
            }
        });
        holder.del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id_String = dData.get(position).get_id();
                apiRemoveManifestation();
                mItemManger.removeShownLayouts(holder.swipeLayout);
                dData.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, dData.size());
                mItemManger.closeAllItems();
                if (position == 0 && dData.isEmpty()){
                    ((Activity) dcontext).finish();
                }
            }
        });
        mItemManger.bindView(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return dData.size();

    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView dtxt;
        private TextView ddate;
        private ImageView next_btn;
        private ImageView info_img;

        public SwipeLayout swipeLayout;
        public LinearLayout compl;
        public LinearLayout del;

        public MyViewHolder(View itemView) {
            super(itemView);
            dtxt = (TextView) itemView.findViewById(R.id.main_txt);
            ddate = (TextView) itemView.findViewById(R.id.date);
            next_btn = (ImageView) itemView.findViewById(R.id.next_btn);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            compl = (LinearLayout) itemView.findViewById(R.id.compl);
            del = (LinearLayout) itemView.findViewById(R.id.del);
            info_img = (ImageView) itemView.findViewById(R.id.info_img);
        }
    }

    private void apiRemoveManifestation(){

        SharedPreferences mPrefs = dcontext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVEMANIFESTATION + id_String, dcontext, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        JSONArray daily_manifestations = jsonObject.getJSONArray("daily_manifestations");
                        update_dailyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < daily_manifestations.length(); i++){
                            JSONObject dailyManifestationObject = daily_manifestations.getJSONObject(i);
                            DailyManifestItems dailyManifestationsListObject = new DailyManifestItems();

                            String _id = dailyManifestationObject.getString("_id");
                            String user = dailyManifestationObject.getString("user");
                            String text = dailyManifestationObject.getString("text");
                            Boolean completion_status = dailyManifestationObject.getBoolean("completion_status");
                            String type = dailyManifestationObject.getString("type");
                            String date = dailyManifestationObject.getString("date");

                            dailyManifestationsListObject.set_id(_id);
                            dailyManifestationsListObject.setUser(user);
                            dailyManifestationsListObject.setText(text);
                            dailyManifestationsListObject.setCompletion_status(completion_status);
                            dailyManifestationsListObject.setType(type);
                            dailyManifestationsListObject.setDate(date);
                            update_dailyManifestationsLists.add(dailyManifestationsListObject);
                        }
                        JSONArray weekly_manifestations = jsonObject.getJSONArray("weekly_manifestations");
                        update_weeklyManifestationsLists = new ArrayList<>();
                        for (int a = 0; a < weekly_manifestations.length(); a++){
                            JSONObject weeklyManifestationObject = weekly_manifestations.getJSONObject(a);
                            DailyManifestItems weeklyManifestationsListObject = new DailyManifestItems();

                            String weekly_id = weeklyManifestationObject.getString("_id");
                            String weeklyuser = weeklyManifestationObject.getString("user");
                            String weeklytext = weeklyManifestationObject.getString("text");
                            Boolean weeklycompletion_status = weeklyManifestationObject.getBoolean("completion_status");
                            String weeklytype = weeklyManifestationObject.getString("type");
                            String weeklydate = weeklyManifestationObject.getString("date");

                            weeklyManifestationsListObject.set_id(weekly_id);
                            weeklyManifestationsListObject.setUser(weeklyuser);
                            weeklyManifestationsListObject.setText(weeklytext);
                            weeklyManifestationsListObject.setCompletion_status(weeklycompletion_status);
                            weeklyManifestationsListObject.setType(weeklytype);
                            weeklyManifestationsListObject.setDate(weeklydate);
                            update_weeklyManifestationsLists.add(weeklyManifestationsListObject);
                        }
                        JSONArray monthly_manifestations = jsonObject.getJSONArray("monthly_manifestations");
                        update_monthlyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < monthly_manifestations.length(); i++){
                            JSONObject monthlyManifestationObject = monthly_manifestations.getJSONObject(i);
                            DailyManifestItems monthlyManifestationsListObject = new DailyManifestItems();

                            String monthly_id = monthlyManifestationObject.getString("_id");
                            String monthlyuser = monthlyManifestationObject.getString("user");
                            String monthlytext = monthlyManifestationObject.getString("text");
                            Boolean monthlycompletion_status = monthlyManifestationObject.getBoolean("completion_status");
                            String monthlytype = monthlyManifestationObject.getString("type");
                            String monthlydate = monthlyManifestationObject.getString("date");

                            monthlyManifestationsListObject.set_id(monthly_id);
                            monthlyManifestationsListObject.setUser(monthlyuser);
                            monthlyManifestationsListObject.setText(monthlytext);
                            monthlyManifestationsListObject.setCompletion_status(monthlycompletion_status);
                            monthlyManifestationsListObject.setType(monthlytype);
                            monthlyManifestationsListObject.setDate(monthlydate);
                            update_monthlyManifestationsLists.add(monthlyManifestationsListObject);
                        }
                        JSONArray yearly_manifestations = jsonObject.getJSONArray("yearly_manifestations");
                        update_yearlyManifestationsLists = new ArrayList<>();
                        for (int i = 0; i < yearly_manifestations.length(); i++){
                            JSONObject yearlyManifestationObject = yearly_manifestations.getJSONObject(i);
                            DailyManifestItems yearlyManifestationsListObject = new DailyManifestItems();

                            String yearly_id = yearlyManifestationObject.getString("_id");
                            String yearlyuser = yearlyManifestationObject.getString("user");
                            String yearlytext = yearlyManifestationObject.getString("text");
                            Boolean yearlycompletion_status = yearlyManifestationObject.getBoolean("completion_status");
                            String yearlytype = yearlyManifestationObject.getString("type");
                            String yearlydate = yearlyManifestationObject.getString("date");

                            yearlyManifestationsListObject.set_id(yearly_id);
                            yearlyManifestationsListObject.setUser(yearlyuser);
                            yearlyManifestationsListObject.setText(yearlytext);
                            yearlyManifestationsListObject.setCompletion_status(yearlycompletion_status);
                            yearlyManifestationsListObject.setType(yearlytype);
                            yearlyManifestationsListObject.setDate(yearlydate);
                            update_yearlyManifestationsLists.add(yearlyManifestationsListObject);
                        }

                        Toast.makeText(dcontext.getApplicationContext(), "Removed", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(dcontext.getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void apiUpdateManifestation(){

        SharedPreferences mPrefs = dcontext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("text", update_manifest_text);
        postParam.put("type", type_string);
        postParam.put("completion_status", "true");

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.UPDATEMANIFESTATION + id_String, dcontext, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        JSONArray daily_manifestations = jsonObject.getJSONArray("daily_manifestations");
                        ArrayList<DailyManifestItems> dailyManifestationsLists = new ArrayList<>();
//                        Datalist = ManifestAdapter.public_arr;
                        for (int i = 0; i < daily_manifestations.length(); i++){
                            JSONObject dailyManifestationObject = daily_manifestations.getJSONObject(i);
                            DailyManifestItems dailyManifestationsListObject = new DailyManifestItems();

                            String _id = dailyManifestationObject.getString("_id");
                            String user = dailyManifestationObject.getString("user");
                            String text = dailyManifestationObject.getString("text");
                            Boolean completion_status = dailyManifestationObject.getBoolean("completion_status");
                            String type = dailyManifestationObject.getString("type");
                            String date = dailyManifestationObject.getString("date");

                            dailyManifestationsListObject.set_id(_id);
                            dailyManifestationsListObject.setUser(user);
                            dailyManifestationsListObject.setText(text);
                            dailyManifestationsListObject.setCompletion_status(completion_status);
                            dailyManifestationsListObject.setType(type);
                            dailyManifestationsListObject.setDate(date);
                            dailyManifestationsLists.add(dailyManifestationsListObject);
                        }
                        JSONArray weekly_manifestations = jsonObject.getJSONArray("weekly_manifestations");
                        ArrayList<DailyManifestItems> weeklyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.weeklyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
                        for (int a = 0; a < weekly_manifestations.length(); a++){
                            JSONObject weeklyManifestationObject = weekly_manifestations.getJSONObject(a);
                            DailyManifestItems weeklyManifestationsListObject = new DailyManifestItems();

                            String weekly_id = weeklyManifestationObject.getString("_id");
                            String weeklyuser = weeklyManifestationObject.getString("user");
                            String weeklytext = weeklyManifestationObject.getString("text");
                            Boolean weeklycompletion_status = weeklyManifestationObject.getBoolean("completion_status");
                            String weeklytype = weeklyManifestationObject.getString("type");
                            String weeklydate = weeklyManifestationObject.getString("date");

                            weeklyManifestationsListObject.set_id(weekly_id);
                            weeklyManifestationsListObject.setUser(weeklyuser);
                            weeklyManifestationsListObject.setText(weeklytext);
                            weeklyManifestationsListObject.setCompletion_status(weeklycompletion_status);
                            weeklyManifestationsListObject.setType(weeklytype);
                            weeklyManifestationsListObject.setDate(weeklydate);
                            weeklyManifestationsLists.add(weeklyManifestationsListObject);
                        }
                        JSONArray monthly_manifestations = jsonObject.getJSONArray("monthly_manifestations");
                        ArrayList<DailyManifestItems> monthlyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.monthlyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
                        for (int i = 0; i < monthly_manifestations.length(); i++){
                            JSONObject monthlyManifestationObject = monthly_manifestations.getJSONObject(i);
                            DailyManifestItems monthlyManifestationsListObject = new DailyManifestItems();

                            String monthly_id = monthlyManifestationObject.getString("_id");
                            String monthlyuser = monthlyManifestationObject.getString("user");
                            String monthlytext = monthlyManifestationObject.getString("text");
                            Boolean monthlycompletion_status = monthlyManifestationObject.getBoolean("completion_status");
                            String monthlytype = monthlyManifestationObject.getString("type");
                            String monthlydate = monthlyManifestationObject.getString("date");

                            monthlyManifestationsListObject.set_id(monthly_id);
                            monthlyManifestationsListObject.setUser(monthlyuser);
                            monthlyManifestationsListObject.setText(monthlytext);
                            monthlyManifestationsListObject.setCompletion_status(monthlycompletion_status);
                            monthlyManifestationsListObject.setType(monthlytype);
                            monthlyManifestationsListObject.setDate(monthlydate);
                            monthlyManifestationsLists.add(monthlyManifestationsListObject);
                        }
                        JSONArray yearly_manifestations = jsonObject.getJSONArray("yearly_manifestations");
                        ArrayList<DailyManifestItems> yearlyManifestationsLists = new ArrayList<>();
//                        Datalist = DailyManifestAdapter.yearlyManifestationsLists;
//                        Datalist = ManifestAdapter.public_arr;
                        for (int i = 0; i < yearly_manifestations.length(); i++){
                            JSONObject yearlyManifestationObject = yearly_manifestations.getJSONObject(i);
                            DailyManifestItems yearlyManifestationsListObject = new DailyManifestItems();

                            String yearly_id = yearlyManifestationObject.getString("_id");
                            String yearlyuser = yearlyManifestationObject.getString("user");
                            String yearlytext = yearlyManifestationObject.getString("text");
                            Boolean yearlycompletion_status = yearlyManifestationObject.getBoolean("completion_status");
                            String yearlytype = yearlyManifestationObject.getString("type");
                            String yearlydate = yearlyManifestationObject.getString("date");

                            yearlyManifestationsListObject.set_id(yearly_id);
                            yearlyManifestationsListObject.setUser(yearlyuser);
                            yearlyManifestationsListObject.setText(yearlytext);
                            yearlyManifestationsListObject.setCompletion_status(yearlycompletion_status);
                            yearlyManifestationsListObject.setType(yearlytype);
                            yearlyManifestationsListObject.setDate(yearlydate);
                            yearlyManifestationsLists.add(yearlyManifestationsListObject);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(dcontext.getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
