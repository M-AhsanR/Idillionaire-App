package com.idillionaire.app.Classes;

public class SchedualItems {
    private String sche_txt;
    private String sche_time;
    private int sche_img;

    public SchedualItems() {
    }

    public SchedualItems(String sche_txt, String sche_time, int sche_img) {
        this.sche_txt = sche_txt;
        this.sche_time = sche_time;
        this.sche_img = sche_img;
    }

    public String getSche_txt() {
        return sche_txt;
    }

    public String getSche_time() {
        return sche_time;
    }

    public int getSche_img() {
        return sche_img;
    }

    public void setSche_txt(String sche_txt) {
        this.sche_txt = sche_txt;
    }

    public void setSche_time(String sche_time) {
        this.sche_time = sche_time;
    }

    public void setSche_img(int sche_img) {
        this.sche_img = sche_img;
    }
}
