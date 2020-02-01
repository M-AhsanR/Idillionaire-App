package com.idillionaire.app.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.BookClubAdapter;
import com.idillionaire.app.Adapters.HomeViewAdapter;
import com.idillionaire.app.Adapters.MagAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Classes.GridSpacingItemDecoration;
import com.idillionaire.app.Models.BooksModel;
import com.idillionaire.app.Models.Galleries;
import com.idillionaire.app.R;
import com.google.android.gms.common.AccountPicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BookClub extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NestedScrollView scrollView;
    private RelativeLayout backhome;
    private ArrayList<Galleries> galleriesList;
    private TextView title_selected;
    private ImageView headerr;
    public String detailed_image;
    ArrayList<BooksModel> booksModelArrayList;
    String title;
    String bookClub_bg;
    Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_club);

        apiBookClub();

        backhome = (RelativeLayout) findViewById(R.id.back_home);
        backhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        title_selected = (TextView) findViewById(R.id.title_selected);


        // Title Bar

        // Detailed Image


        // Detailed Image

        // RecyclerView/ScrollView

        scrollView = findViewById(R.id.nestedscrollView);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        scrollView.setSmoothScrollingEnabled(true);
        recyclerView.setFocusable(false);


        GridLayoutManager MyLayoutManager = new GridLayoutManager(BookClub.this, 2);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(MyLayoutManager);
        int spacing = 2; // 50px
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

        // RecyclerView/ScrollView

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void apiBookClub(){

        final SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BOOKCLUB, BookClub.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        int code = jsonObject.getInt("code");
                        title = jsonObject.getString("title");
                        bookClub_bg = jsonObject.getString("bookClub_bg");
                        JSONArray books = jsonObject.getJSONArray("books");

                        booksModelArrayList = new ArrayList<>();
                        for (int i = 0; i < books.length(); i++){
                            JSONObject books_obj = books.getJSONObject(i);

                            BooksModel booksModel = new BooksModel();

                            String _id = books_obj.getString("_id");
                            String title_book = books_obj.getString("title");
                            String description = books_obj.getString("description");
                            int price = books_obj.getInt("price");
                            String link = books_obj.getString("link");
                            String book_image = books_obj.getString("book_image");

                            booksModel.set_id(_id);
                            booksModel.setTitle(title_book);
                            booksModel.setDescription(description);
                            booksModel.setPrice(price);
                            booksModel.setLink(link);
                            booksModel.setBook_image(book_image);

                            booksModelArrayList.add(booksModel);

                        }

                        BookClubAdapter bookClubAdapter = new BookClubAdapter(BookClub.this, booksModelArrayList);
                        recyclerView.setAdapter(bookClubAdapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(BookClub.this, ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}
