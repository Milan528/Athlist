package com.example.athlist.models;

public class MessageFromServer {
    private String message;
    private int status;
    private StravaProfile stravaProfile;

    public StravaProfile getStravaProfile() {
        return stravaProfile;
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
