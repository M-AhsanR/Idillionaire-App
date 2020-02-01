package com.idillionaire.app.Models;

import java.util.ArrayList;
import java.util.List;

public class StoryMediaImages {
    String title;
    String media_file_name;
    ArrayList<MediaFiles> mediaData;

    public StoryMediaImages() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMedia_file_name() {
        return media_file_name;
    }

    public void setMedia_file_name(String media_file_name) {
        this.media_file_name = media_file_name;
    }

    public ArrayList<MediaFiles> getMediaData() {
        return mediaData;
    }

    public void setMediaData(ArrayList<MediaFiles> mediaData) {
        this.mediaData = mediaData;
    }
}
