package com.idillionaire.app.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idillionaire.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

//import android.text.format.DateFormat;

public class UpdateGratitudeAdapter extends RecyclerView.Adapter<UpdateGratitudeAdapter.MyViewHolder> {

    Context uContext;
    List<String> text_arr;
    String date_string;
    String text;

    public UpdateGratitudeAdapter(Context uContext, List<String> text_arr) {
        this.text_arr = text_arr;
        this.uContext = uContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(uContext).inflate(R.layout.update_gratitud_items, parent, false);
        MyViewHolder aholder = new UpdateGratitudeAdapter.MyViewHolder(v);
        return aholder;
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        date_string = GratitudeAdapter.date_string;
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
//
//        try {
//            Date date1 = format.parse(date_string);
//            DateFormat dateFormat = DateFormat.getDateInstance();
//            String final_date = dateFormat.format(date1);
//
//            SimpleDateFormat date_formate = new SimpleDateFormat("dd MMM yyyy" );
//
//            Date date = date_formate.parse(final_date);
//            SimpleDateFormat df = new SimpleDateFormat("MMM dd, yyyy");
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            final_date = df.format(calendar.getTime());
//            holder.date_bold.setText(final_date);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        // set time //////////////////////////

        String tim = GratitudeAdapter.date_string;
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
        holder.date_bold.setText(final_date);

        // set time //////////////////////////


        holder.txt.setText(text_arr.get(position));

        holder.txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = s.toString();
                text_arr.set(position, text);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        if (position == text_arr.size()-1){
            holder.add_btn.setVisibility(View.VISIBLE);
        } else{
            holder.add_btn.setVisibility(View.GONE);
        }
        if (position == 0){
            holder.header.setVisibility(View.VISIBLE);
        } else {
            holder.header.setVisibility(View.GONE);
        }
        holder.add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.txt.getText().toString().isEmpty()){
                    Toast.makeText(uContext.getApplicationContext(), "Enter Text", Toast.LENGTH_SHORT).show();
                }else {
                text_arr.add(new String(""));
                notifyItemChanged(position);
                    holder.txt.requestFocus();

                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return text_arr.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private EditText txt;
        private LinearLayout header;
        private TextView date_bold;
        private TextView add_btn;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (EditText) itemView.findViewById(R.id.entry_text);
            header = (LinearLayout) itemView.findViewById(R.id.header);
            date_bold = (TextView) itemView.findViewById(R.id.date_bold);
            add_btn = (TextView) itemView.findViewById(R.id.add_btn);

        }
    }
}
