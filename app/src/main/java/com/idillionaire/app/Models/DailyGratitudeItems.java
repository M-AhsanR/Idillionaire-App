package com.idillionaire.app.Models;

import org.json.JSONArray;

import java.util.ArrayList;

public class DailyGratitudeItems {
    String _id;
    String user;
    ArrayList<String> texts;
    String date;

    public DailyGratitudeItems() {
    }

    public DailyGratitudeItems(String _id, String user, ArrayList<String> texts, String date) {
        this._id = _id;
        this.user = user;
        this.texts = texts;
        this.date = date;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public ArrayList<String> getTexts() {
        return texts;
    }

    public void setTexts(ArrayList<String> texts) {
        this.texts = texts;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
