package com.idillionaire.app.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.Request;
import com.idillionaire.app.Adapters.FavrtGrideMAdapter;
import com.idillionaire.app.ApiStructure.ApiModelClass;
import com.idillionaire.app.ApiStructure.Constants;
import com.idillionaire.app.ApiStructure.ServerCallback;
import com.idillionaire.app.Models.Login;
import com.idillionaire.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TwoFragment extends Fragment{

    private GridView gridView;
    public static List<Login.Favourite_images> favoriteLists;
    private FavrtGrideMAdapter adapter;
    List<Login.Favourite_images> list_from_shared;
    SharedPreferences mPrefs;


    @Override
    public void onResume() {
        super.onResume();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ft.detach(TwoFragment.this).attach(TwoFragment.this).commit();
        apiFavourite();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(TwoFragment.this).attach(TwoFragment.this).commit();
            getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            apiFavourite();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        gridView = (GridView) view.findViewById(R.id.fvrt_grid);


        apiFavourite();

        return view;
    }

//     runnable

//     runnable

    private void apiFavourite(){
        mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String USER_TOKEN=mPrefs.getString("USER_TOKEN","");

        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", USER_TOKEN);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.FVRT, getContext(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        JSONArray favourite_images = jsonObject.getJSONArray("favourite_images");
//                        ArrayList<FavoriteList> favourite_images_arr = new ArrayList<>();
                        favoriteLists = new ArrayList<>();
                        for (int i = 0; i < favourite_images.length(); i++){
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
                            favoriteLists.add(favoriteListObj);
                        }

                        adapter = new FavrtGrideMAdapter(getContext(), favoriteLists);
                        gridView.setAdapter(adapter);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Toast.makeText(getContext(), ERROR, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}