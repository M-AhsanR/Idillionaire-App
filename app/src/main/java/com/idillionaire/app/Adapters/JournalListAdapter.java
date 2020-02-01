package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.daimajia.swipe.util.Attributes;
import com.idillionaire.app.Activities.ArivalPage;
import com.idillionaire.app.Activities.DailyGratitude;
import com.idillionaire.app.Activities.ManifestaionPage;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Classes.JournalItems;
import com.idillionaire.app.Fragments.FourFragment;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.Models.JournalModel;
import com.idillionaire.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JournalListAdapter extends RecyclerView.Adapter<JournalListAdapter.MyViewHolder> {

    Context jcontext;
    ArrayList<JournalModel> jdata;
    ArrayList<String> text_arr;
    public static ArrayList<DailyGratitudeItems> gratitudeItemsList;

    public JournalListAdapter(Context mcontext, ArrayList<JournalModel> mdata) {
        this.jdata = mdata;
        this.jcontext = mcontext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(jcontext).inflate(R.layout.journal_list,parent,false);
        MyViewHolder jHolder = new MyViewHolder(v);
        return jHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        apiListDailyGratitude();


        holder.txt.setText("Daily Gratitude");
        holder.txt.setText("Manifestation");

        Picasso.with(jcontext)
                .load(Constants.URL.BASE_URL + jdata.get(0).getGratitude_bg())
                .into(holder.img);
        Picasso.with(jcontext)
                .load(Constants.URL.BASE_URL + jdata.get(1).getManifestation_bg())
                .into(holder.img);

//        holder.img.setBackgroundResource(jdata.get(position).getJournalimages());
//        gratitudeItemsList = FourFragment.gratitudeItemsList;
//        gratitudeItemsList = new ArrayList<>();

        if (position == 0) {

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (gratitudeItemsList.isEmpty()){
                        Intent intent = new Intent(jcontext, ArivalPage.class);
                        jcontext.startActivity(intent);
                    }else {
                        Intent gratitudeintent = new Intent(jcontext, DailyGratitude.class);
                        jcontext.startActivity(gratitudeintent);
                    }
                }
            });
        }

        if (position == 1) {

        holder.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manifestintent = new Intent(jcontext, ManifestaionPage.class);
                jcontext.startActivity(manifestintent);
            }
        });
        }

    }

    @Override
    public int getItemCount() {
        return jdata.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView txt;
        private ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (TextView) itemView.findViewById(R.id.journal_txt);
            img = (ImageView) itemView.findViewById(R.id.journal_image);

        }
    }
    private void apiListDailyGratitude(){

        SharedPreferences mPrefs = jcontext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LISTGRATITUDE, jcontext, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        JSONArray daily_gratitude = jsonObject.getJSONArray("daily_gratitude");
                        gratitudeItemsList = new ArrayList<>();
                        for (int i = 0; i < daily_gratitude.length(); i++){
                            JSONObject gratitudeOBJ = daily_gratitude.getJSONObject(i);
                            DailyGratitudeItems dailyGratitude = new DailyGratitudeItems();

                            String _id = gratitudeOBJ.getString("_id");
                            String user = gratitudeOBJ.getString("user");
                            JSONArray texts = gratitudeOBJ.getJSONArray("texts");
                            String date = gratitudeOBJ.getString("date");

                            ArrayList <String> text_arr = new ArrayList<>();

                            for (int a = 0; a < texts.length(); a++){

                                text_arr.add(String.valueOf(texts.get(a)));

                            }


                            dailyGratitude.set_id(_id);
                            dailyGratitude.setUser(user);
                            dailyGratitude.setTexts(text_arr);
                            dailyGratitude.setDate(date);
                            gratitudeItemsList.add(dailyGratitude);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {
                    Toast.makeText(jcontext.getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}