package com.idillionaire.app.Models;

import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Categories  {
    String _id;
    String title;
    Boolean is_active;
    int order_number;
    ArrayList<Galleries> galleries;
    String Detail_image;
    String list_image;

    public ArrayList<Galleries> getGalleries() {
        return galleries;
    }

    public void setGalleries(ArrayList<Galleries> galleries) {
        this.galleries = galleries;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public Boolean getIs_active() {
        return is_active;
    }

    public int getOrder_number() {
        return order_number;
    }


    public String getDetail_image() {
        return Detail_image;
    }

    public String getList_image() {
        return list_image;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIs_active(Boolean is_active) {
        this.is_active = is_active;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }


    public void setDetail_image(String detail_image) {
        Detail_image = detail_image;
    }

    public void setList_image(String list_image) {
        this.list_image = list_image;
    }
}
