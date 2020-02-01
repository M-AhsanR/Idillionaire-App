package com.idillionaire.app.Models;

import java.util.ArrayList;

public class Login {
    String _id;
    boolean confirmed;
    String full_name;
    String gender;
    boolean admin_access;
    ArrayList<Favourite_images> favourite_images;
    int gratitude_count;
    int manifestation_count;
    String login_type;
    boolean subscribed_from_ios;
    boolean subscribed_from_android;

    public String getLogin_type() {
        return login_type;
    }

    public void setLogin_type(String login_type) {
        this.login_type = login_type;
    }

    public boolean isSubscribed_from_ios() {
        return subscribed_from_ios;
    }

    public void setSubscribed_from_ios(boolean subscribed_from_ios) {
        this.subscribed_from_ios = subscribed_from_ios;
    }

    public boolean isSubscribed_from_android() {
        return subscribed_from_android;
    }

    public void setSubscribed_from_android(boolean subscribed_from_android) {
        this.subscribed_from_android = subscribed_from_android;
    }

    public int getGratitude_count() {
        return gratitude_count;
    }

    public void setGratitude_count(int gratitude_count) {
        this.gratitude_count = gratitude_count;
    }

    public int getManifestation_count() {
        return manifestation_count;
    }

    public void setManifestation_count(int manifestation_count) {
        this.manifestation_count = manifestation_count;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed) {
        this.confirmed = confirmed;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public boolean isAdmin_access() {
        return admin_access;
    }

    public void setAdmin_access(boolean admin_access) {
        this.admin_access = admin_access;
    }

    public ArrayList<Favourite_images> getFavourite_images() {
        return favourite_images;
    }

    public void setFavourite_images(ArrayList<Favourite_images> favourite_images) {
        this.favourite_images = favourite_images;
    }

    public static class Favourite_images {
        String _id;
        String title;
        String image_id;
        String Detailed_img_name;
        String list_img;
        int order_number;
        boolean tf;

        public boolean isTf() {
            return tf;
        }

        public void setTf(boolean tf) {
            this.tf = tf;
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
}
