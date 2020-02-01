package com.idillionaire.app.Classes;

public class SelectedCategoriesItems {
    private String categtxt;
    private int categimg;

    public SelectedCategoriesItems() {
    }

    public SelectedCategoriesItems(String categtxt, int categimg) {
        this.categtxt = categtxt;
        this.categimg = categimg;
    }

    public String getCategtxt() {
        return categtxt;
    }

    public int getCategimg() {
        return categimg;
    }

    public void setCategtxt(String categtxt) {
        this.categtxt = categtxt;
    }

    public void setCategimg(int categimg) {
        this.categimg = categimg;
    }
}
