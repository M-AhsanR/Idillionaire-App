package com.idillionaire.app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BookClubDetail extends AppCompatActivity {

    TextView title_txt, description_txt, main_title, buy_book;
    RelativeLayout back_btn;
    ImageView book_img;
    String book_id;
    String link;
    WebView webview_detail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_club_detail);

        initialization();

        book_id = getIntent().getStringExtra("book_id");

        ApibookDetail(book_id);

        Typeface font = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Roman.otf");
        Typeface font_bold = Typeface.createFromAsset(getAssets(), "HelveticaLTStd-Bold.otf");
        title_txt.setTypeface(font_bold);
        description_txt.setTypeface(font);
        main_title.setTypeface(font);


        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        buy_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webview = new Intent(Intent.ACTION_VIEW);
                webview.setData(Uri.parse(link));
                startActivity(webview);
            }
        });




    }

    private void ApibookDetail(String id){
        SharedPreferences mprefs = getSharedPreferences("USER_DATA", MODE_PRIVATE);
        String USER_TOKEN = mprefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BOOKDETAIL + id, BookClubDetail.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()){

                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200){

                            JSONObject book = jsonObject.getJSONObject("book");

                            String title = book.getString("title");
                            String author = book.getString("author");
                            String description = book.getString("description");
                            String book_image = book.getString("book_image");
                            link = book.getString("link");

//                            title_txt.setText(title);
//                            description_txt.setText(description);
                            main_title.setText(title);

                            webview_detail.loadData(description, "text/html", "UTF-8");

                            Picasso.with(BookClubDetail.this)
                                    .load(Constants.URL.BASE_URL + book_image)
                                    .into(book_img);


                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
        });
    }


    private void initialization(){

        title_txt = findViewById(R.id.book_title_txt);
        description_txt = findViewById(R.id.book_des_txt);
        main_title = findViewById(R.id.title_detail_book);
        back_btn = findViewById(R.id.back_bookclub);
        book_img = findViewById(R.id.bookdetail_img);
        buy_book = findViewById(R.id.buy_book);
        webview_detail = findViewById(R.id.webview_detail);

    }
}
