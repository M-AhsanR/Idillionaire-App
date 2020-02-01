package com.idillionaire.app.Models;

public class SharedData {
    String title;
    String Detail_image;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail_image() {
        return Detail_image;
    }

    public void setDetail_image(String detail_image) {
        Detail_image = detail_image;
    }

    public SharedData(String title, String detail_image) {

        this.title = title;
        Detail_image = detail_image;
    }

    public SharedData() {

    }
}
