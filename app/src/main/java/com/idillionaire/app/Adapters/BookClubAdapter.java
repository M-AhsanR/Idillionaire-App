package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.idillionaire.app.Activities.BookClubDetail;
import com.idillionaire.app.Activities.StoryMode;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.Models.BooksModel;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.Models.MediaFiles;
import com.idillionaire.app.Models.StoryMediaImages;
import com.idillionaire.app.R;

import java.util.ArrayList;

public class BookClubAdapter extends RecyclerView.Adapter<BookClubAdapter.MyViewHolder> {


        private Context context;
        public static ArrayList<BooksModel> arr;
        public static String title;

        public BookClubAdapter(Context context, ArrayList<BooksModel> arr) {
            this.context = context;
            this.arr = arr;
        }

        @Override
        public BookClubAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.book_club_items, parent, false);

            return new BookClubAdapter.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final BookClubAdapter.MyViewHolder holder, final int position) {

            holder.mag_textview.setText(arr.get(position).getTitle());

            Glide.with(context).load(Constants.URL.BASE_URL + arr.get(position).getBook_image()).into(holder.mag_images);
            holder.mag_images.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent webview = new Intent(Intent.ACTION_VIEW);
//                    webview.setData(Uri.parse(arr.get(position).getLink()));
//                    context.startActivity(webview);

                    Intent intent = new Intent(context, BookClubDetail.class);
                    intent.putExtra("book_id", arr.get(position).get_id());
                    context.startActivity(intent);


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
