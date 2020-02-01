package com.idillionaire.app.Classes;

public class JournalItems {
    private String journaltxt;
    private int journalimages;

    public JournalItems() {
    }

    public JournalItems(String journaltxt, int journalimages) {
        this.journaltxt = journaltxt;
        this.journalimages = journalimages;
    }

    public String getJournaltxt() {
        return journaltxt;
    }

    public int getJournalimages() {
        return journalimages;
    }

    public void setJournaltxt(String journaltxt) {
        this.journaltxt = journaltxt;
    }

    public void setJournalimages(int journalimages) {
        this.journalimages = journalimages;
    }
}
