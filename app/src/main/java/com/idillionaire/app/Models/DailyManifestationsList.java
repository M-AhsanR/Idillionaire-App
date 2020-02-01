package com.idillionaire.app.Models;

public class DailyManifestationsList {
    String _id;
    String user;
    String text;
    Boolean completion_status;
    String type;
    String date;

    public DailyManifestationsList() {
    }

    public DailyManifestationsList(String _id, String user, String text, Boolean completion_status, String type, String date) {
        this._id = _id;
        this.user = user;
        this.text = text;
        this.completion_status = completion_status;
        this.type = type;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getCompletion_status() {
        return completion_status;
    }

    public void setCompletion_status(Boolean completion_status) {
        this.completion_status = completion_status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
