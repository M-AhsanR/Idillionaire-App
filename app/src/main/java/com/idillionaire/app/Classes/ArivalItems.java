package com.idillionaire.app.Classes;

import com.idillionaire.app.Activities.DailyGratitude;

import java.util.ArrayList;

public class ArivalItems {
    private String a_txt;
    public static ArrayList<String> text_arr;

    public ArivalItems() {
    }

    public ArivalItems(String a_txt) {
        this.a_txt = a_txt;
    }

    public String getA_txt() {
        return a_txt;
    }

    public void setA_txt(String a_txt) {
        this.a_txt = a_txt;
    }
}
