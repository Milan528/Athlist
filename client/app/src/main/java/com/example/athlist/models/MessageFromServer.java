package com.example.athlist.models;

import java.util.ArrayList;

public class MessageFromServer {
    private String message;
    private int status;
    private ArrayList<StravaActivity> activities;


    public ArrayList<StravaActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<StravaActivity> activities) {
        this.activities = activities;
    }


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
