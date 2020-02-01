package com.idillionaire.app.Models;

import java.util.ArrayList;

public class Signup {
    String _id;
    Boolean confirmed;
    String full_name;
    String gender;
    Boolean admin_access;
    ArrayList<String> favourite_images;
    String token;
    int manifestation_count;
    int gratitude_count;
    boolean subscribed;
    String subscription_token;

    public boolean isSubscribed() {
        return subscribed;
    }

    public void setSubscribed(boolean subscribed) {
        this.subscribed = subscribed;
    }

    public String getSubscription_token() {
        return subscription_token;
    }

    public void setSubscription_token(String subscription_token) {
        this.subscription_token = subscription_token;
    }

    public int getManifestation_count() {
        return manifestation_count;
    }

    public void setManifestation_count(int manifestation_count) {
        this.manifestation_count = manifestation_count;
    }

    public int getGratitude_count() {
        return gratitude_count;
    }

    public void setGratitude_count(int gratitude_count) {
        this.gratitude_count = gratitude_count;
    }

    public ArrayList<String> getFavourite_images() {
        return favourite_images;
    }

    public void setFavourite_images(ArrayList<String> favourite_images) {
        this.favourite_images = favourite_images;
    }
    public String get_id() {
        return _id;
    }

    public Boolean getConfirmed() {
        return confirmed;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getAdmin_access() {
        return admin_access;
    }

    public String getToken() {
        return token;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public void setConfirmed(Boolean confirmed) {
        this.confirmed = confirmed;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAdmin_access(Boolean admin_access) {
        this.admin_access = admin_access;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
