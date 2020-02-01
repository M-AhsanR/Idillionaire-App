package com.idillionaire.app.Models;

public class AddFavorite {
    String _id;
    String title;
    String image_id;
    String Detailed_img_name;
    String list_img;
    int order_number;

    public AddFavorite() {
    }

    public AddFavorite(String _id, String title, String image_id, String detailed_img_name, String list_img, int order_number) {
        this._id = _id;
        this.title = title;
        this.image_id = image_id;
        Detailed_img_name = detailed_img_name;
        this.list_img = list_img;
        this.order_number = order_number;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getDetailed_img_name() {
        return Detailed_img_name;
    }

    public void setDetailed_img_name(String detailed_img_name) {
        Detailed_img_name = detailed_img_name;
    }

    public String getList_img() {
        return list_img;
    }

    public void setList_img(String list_img) {
        this.list_img = list_img;
    }

    public int getOrder_number() {
        return order_number;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }
}
