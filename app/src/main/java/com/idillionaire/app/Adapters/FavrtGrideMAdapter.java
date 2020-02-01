package com.idillionaire.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Activities.DetailedImage;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.R;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FavrtGrideMAdapter extends BaseAdapter {

    private List<Login.Favourite_images> favrtData;
    private Context context;
    private LayoutInflater inflater;
    public static String image_id;
    public static String _id;
//    public static String main_id;
    public static String detailed_image;
    public static String detailed_image_name;
    SharedPreferences mPrefs;
    Gson gson;
    String favoriteListPost;

    public static String schedule_detail_image;
    public static String schedule_list_image;
    public static String schedule_gallery_name;

    public FavrtGrideMAdapter(Context context, List<Login.Favourite_images> favrtData) {
        this.favrtData = favrtData;
        this.context = context;


    }


    @Override
    public int getCount() {

        return favrtData.size();
            }

    @Override
    public Object getItem(int position) {

        return favrtData.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;

    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {

        View gridView = convertView;

        if (convertView == null){

            inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            gridView = inflater.inflate(R.layout.gride_layout_fvrt,null);
        }


        ImageView images = (ImageView) gridView.findViewById(R.id.fvrt_img);
        RelativeLayout remove = (RelativeLayout) gridView.findViewById(R.id.remove);


//        images.setImageResource(favrtData[position]);
        Picasso.with(context).load(Constants.URL.BASE_URL + favrtData.get(position).getList_img())
                .into(images);

        images.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                schedule_detail_image = favrtData.get(position).getDetailed_img_name();
                schedule_list_image = favrtData.get(position).getList_img();

                detailed_image_name = favrtData.get(position).getDetailed_img_name();
                image_id = favrtData.get(position).getImage_id();
                _id = favrtData.get(position).getImage_id();
//                main_id = favrtData.get(position).get_id();
//                detailed_image = favrtData.get(position).getDetailed_img_name();
                Intent detailed_intent = new Intent(context, DetailedImage.class);
                context.startActivity(detailed_intent);
            }
        });



        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_id = favrtData.get(position).getImage_id();
                _id = favrtData.get(position).getImage_id();
                apiRemoveFvrt();
                notifyDataSetChanged();

            }
        });

        return gridView;
    }

    private void  apiRemoveFvrt(){
        mPrefs = context.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.RMVFVRT + image_id, context, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
                        favrtData = new ArrayList<>();
                        for (int i = 0; i < favourite_images.length(); i++) {
                            JSONObject a = favourite_images.getJSONObject(i);
                            Login.Favourite_images favoriteListObj = new Login.Favourite_images();

                            String _id = a.getString("_id");
                            String title = a.getString("title");
                            String image_id = a.getString("image_id");
                            String Detailed_img_name = a.getString("Detailed_img_name");
                            String list_img = a.getString("list_img");
                            int order_number = a.getInt("order_number");

                            favoriteListObj.set_id(_id);
                            favoriteListObj.setTitle(title);
                            favoriteListObj.setImage_id(image_id);
                            favoriteListObj.setDetailed_img_name(Detailed_img_name);
                            favoriteListObj.setList_img(list_img);
                            favoriteListObj.setOrder_number(order_number);
                            favrtData.add(favoriteListObj);
                        }

                        gson = new Gson();
                        favoriteListPost = gson.toJson(favrtData);
                        mPrefs.edit().putString("FAVORITELIST", favoriteListPost).apply();

                        Toast.makeText(context.getApplicationContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

}
