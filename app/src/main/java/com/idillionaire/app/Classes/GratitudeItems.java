package com.idillionaire.app.Classes;

public class GratitudeItems {

    private String gratitude_txt;
    private String gratitude_date;

    public GratitudeItems() {
    }

    public GratitudeItems(String gratitude_txt, String gratitude_date) {
        this.gratitude_txt = gratitude_txt;
        this.gratitude_date = gratitude_date;
    }

    public String getGratitude_txt() {
        return gratitude_txt;
    }

    public String getGratitude_date() {
        return gratitude_date;
    }

    public void setGratitude_txt(String gratitude_txt) {
        this.gratitude_txt = gratitude_txt;
    }

    public void setGratitude_date(String getGratitude_date) {
        this.gratitude_date = getGratitude_date;
    }
}
