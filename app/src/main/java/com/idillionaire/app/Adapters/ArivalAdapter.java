package com.idillionaire.app.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.idillionaire.app.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ArivalAdapter extends RecyclerView.Adapter<ArivalAdapter.MyViewHolder> {

    Context aContext;
    List<String> aData;
    String date_string;
    public static String text;

    public ArivalAdapter(Context aContext, List<String> aData) {
        this.aData = aData;
        this.aContext = aContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(aContext).inflate(R.layout.arival_items, parent, false);
        MyViewHolder aholder = new MyViewHolder(v);
        return aholder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        date_string = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        holder.date_bold.setText(date_string);
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
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


        holder.txt.setText(aData.get(position));

        holder.txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                text = s.toString();
                aData.set(position, text);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        if (position == aData.size()-1){
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
                    Toast.makeText(aContext.getApplicationContext(), "Enter Text", Toast.LENGTH_SHORT).show();
                }else {
                aData.add(new String(""));
                notifyItemChanged(position);
                holder.txt.requestFocus();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return aData.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private EditText txt;
        private TextView add_btn;
        private LinearLayout header;
        private TextView date_bold;

        public MyViewHolder(View itemView) {
            super(itemView);
            txt = (EditText) itemView.findViewById(R.id.entry_text);
            add_btn = (TextView) itemView.findViewById(R.id.add_btn);
            header = (LinearLayout) itemView.findViewById(R.id.header);
            date_bold = (TextView) itemView.findViewById(R.id.date_bold);
        }

    }

}
