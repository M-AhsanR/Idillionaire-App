package com.idillionaire.app.Classes;

public class HomeItems {

    private String hometxt;
    private String images;


    public HomeItems(String hometxt, String images) {
        this.hometxt = hometxt;
        this.images = images;
    }


    public String getHometxt() {
        return hometxt;
    }

    public String getImages() {
        return images;
    }

    public void setHometxt(String hometxt) {
        this.hometxt = hometxt;
    }

    public void setImages(String images) {
        this.images = images;
    }



}
