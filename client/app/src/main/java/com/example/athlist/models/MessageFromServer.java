package com.example.athlist.models;

import java.util.ArrayList;

public class MessageFromServer {
    private String message;
    private int status;
    private StravaProfile stravaProfile;
    private ArrayList<StravaActivity> activities;

    public StravaProfile getStravaProfile() {
        return stravaProfile;
    }

    public ArrayList<StravaActivity> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<StravaActivity> activities) {
        this.activities = activities;
    }

    public void setStravaProfile(StravaProfile stravaProfile) {
        this.stravaProfile = stravaProfile;
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
