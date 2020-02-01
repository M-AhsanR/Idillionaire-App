package com.idillionaire.app.Models;

public class JournalModel {
    String _id;
    String gratitude_bg;
    String manifestation_bg;
    String daily_manifestation;
    String weekly_manifestation;
    String monthly_manifestation;
    String yearly_manifestation;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGratitude_bg() {
        return gratitude_bg;
    }

    public void setGratitude_bg(String gratitude_bg) {
        this.gratitude_bg = gratitude_bg;
    }

    public String getManifestation_bg() {
        return manifestation_bg;
    }

    public void setManifestation_bg(String manifestation_bg) {
        this.manifestation_bg = manifestation_bg;
    }

    public String getDaily_manifestation() {
        return daily_manifestation;
    }

    public void setDaily_manifestation(String daily_manifestation) {
        this.daily_manifestation = daily_manifestation;
    }

    public String getWeekly_manifestation() {
        return weekly_manifestation;
    }

    public void setWeekly_manifestation(String weekly_manifestation) {
        this.weekly_manifestation = weekly_manifestation;
    }

    public String getMonthly_manifestation() {
        return monthly_manifestation;
    }

    public void setMonthly_manifestation(String monthly_manifestation) {
        this.monthly_manifestation = monthly_manifestation;
    }

    public String getYearly_manifestation() {
        return yearly_manifestation;
    }

    public void setYearly_manifestation(String yearly_manifestation) {
        this.yearly_manifestation = yearly_manifestation;
    }
}
