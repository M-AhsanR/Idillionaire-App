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
import com.idillionaire.app.Activities.UpdateGratitude;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.DailyGratitudeItems;
import com.idillionaire.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class GratitudeAdapter extends RecyclerSwipeAdapter<GratitudeAdapter.MyViewHolder>{

    Context gcontext;
    public static List<DailyGratitudeItems> gdata;
    public static String gratitude_id;
    public static List<String> text_arr;
    public static String date_string;

    public GratitudeAdapter(Context gcontext, List<DailyGratitudeItems> gdata) {
        this.gdata = gdata;
        this.gcontext = gcontext;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gratitude_items,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final DailyGratitudeItems items = gdata.get(position);

        gratitude_id = items.get_id();


        holder.gtxt.setText(gdata.get(position).getTexts().get(0));

        // set time //////////////////////////

        String tim = gdata.get(position).getDate();
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
        holder.gdate.setText(final_date);

        // set time //////////////////////////


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

                gratitude_id = items.get_id();
                text_arr = items.getTexts();
                date_string = items.getDate();
                Intent uIntent = new Intent(gcontext, UpdateGratitude.class);
                gcontext.startActivity(uIntent);
                ((Activity)gcontext).finish();
            }
        });

        holder.notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gratitude_id = items.get_id();
                text_arr = items.getTexts();
                date_string = items.getDate();
                Intent uIntent = new Intent(gcontext, UpdateGratitude.class);
                gcontext.startActivity(uIntent);
                ((Activity)gcontext).finish();
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gratitude_id = items.get_id();
                apiRemoveGratitude();
                mItemManger.removeShownLayouts(holder.swipeLayout);
                gdata.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, gdata.size());
                mItemManger.closeAllItems();
                if (position == 0 && gdata.isEmpty()){
                    ((Activity) gcontext).finish();
                }
            }
        });
        mItemManger.bindView(holder.itemView, position);

    }

    @Override
    public int getItemCount() {
        return gdata.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipe;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView gtxt;
        private TextView gdate;
        private ImageView next_btn;

        public SwipeLayout swipeLayout;
        public LinearLayout notes;
        public LinearLayout delete;

        public MyViewHolder(View itemView) {
            super(itemView);

            gtxt = (TextView) itemView.findViewById(R.id.main_txt);
            gdate = (TextView) itemView.findViewById(R.id.date);
            next_btn = (ImageView) itemView.findViewById(R.id.next_btn);

            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
            notes = (LinearLayout) itemView.findViewById(R.id.edit);
            delete = (LinearLayout) itemView.findViewById(R.id.delete);

        }
    }

    private void apiRemoveGratitude(){
        SharedPreferences mPrefs = gcontext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVEGRATITUDE + gratitude_id, gcontext, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()) {
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        String message = jsonObject.getString("message");

                        Toast.makeText(gcontext.getApplicationContext(), "Item Removed", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(gcontext.getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
