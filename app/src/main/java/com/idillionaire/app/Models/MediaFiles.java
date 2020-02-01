package com.idillionaire.app.Models;

public class MediaFiles {
    int order_number;
    String _id;
    String title;
    String list_image;
    String Detailed_img_name;
    boolean tf;


    String gallery_title;

    public String getGallery_title() {
        return gallery_title;
    }

    public void setGallery_title(String gallery_title) {
        this.gallery_title = gallery_title;
    }

    public boolean isTf() {
        return tf;
    }

    public void setTf(boolean tf) {
        this.tf = tf;
    }

    public int getOrder_number() {
        return order_number;
    }

    public String get_id() {
        return _id;
    }

    public String getTitle() {
        return title;
    }

    public String getList_image() {
        return list_image;
    }

    public String getDetailed_img_name() {
        return Detailed_img_name;
    }

    public void setOrder_number(int order_number) {
        this.order_number = order_number;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setList_image(String list_image) {
        this.list_image = list_image;
    }

    public void setDetailed_img_name(String detailed_img_name) {
        Detailed_img_name = detailed_img_name;
    }
}
