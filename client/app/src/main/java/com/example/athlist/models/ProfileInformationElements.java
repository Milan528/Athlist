package com.example.athlist.models;
import com.google.firebase.database.Exclude;

public class ProfileInformationElements {
    private String title;
    private String data;
    @Exclude
    private int imageID;

    public ProfileInformationElements() { }

    public ProfileInformationElements(String title, String data, int imageID) {
        this.title = title;
        this.data = data;
        this.imageID = imageID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Exclude
    public int getImageID() {
        return imageID;
    }
    @Exclude
    public void setImageID(int imageID) {
        this.imageID = imageID;
    }
}
