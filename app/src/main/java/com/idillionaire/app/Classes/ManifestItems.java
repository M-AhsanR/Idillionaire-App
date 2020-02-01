package com.idillionaire.app.Classes;

public class ManifestItems {
    private String menifesttxt;
    private int menifestimages;

    public ManifestItems() {
    }

    public ManifestItems(String menifesttxt, int menifestimages) {
        this.menifesttxt = menifesttxt;
        this.menifestimages = menifestimages;
    }

    public String getMenifesttxt() {
        return menifesttxt;
    }

    public int getMenifestimages() {
        return menifestimages;
    }

    public void setMenifesttxt(String menifesttxt) {
        this.menifesttxt = menifesttxt;
    }

    public void setMenifestimages(int menifestimages) {
        this.menifestimages = menifestimages;
    }
}
