package com.idillionaire.app.Models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Galleries {
    int order_number;
    String updated_at;
    String _id_galleries;
    ArrayList<MediaFiles> media_files;
    String _id_gallery;
    String title_gallery;
    String media_file_name;
//    JSONObject gallery;





    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String get_id_galleries() {
        return _id_galleries;
    }

    public void set_id_galleries(String _id_galleries) {
        this._id_galleries = _id_galleries;
    }

    public ArrayList<MediaFiles> getMedia_files() {
        return media_files;
    }

    public void setMedia_files(ArrayList<MediaFiles> media_files) {
        this.media_files = media_files;
    }

    public String get_id_gallery() {
        return _id_gallery;
    }

    public void set_id_gallery(String _id_gallery) {
        this._id_gallery = _id_gallery;
    }

    public String getTitle_gallery() {
        return title_gallery;
    }

    public void setTitle_gallery(String title_gallery) {
        this.title_gallery = title_gallery;
    }

    public String getMedia_file_name() {
        return media_file_name;
    }

    public void setMedia_file_name(String media_file_name) {
        this.media_file_name = media_file_name;
    }


}
