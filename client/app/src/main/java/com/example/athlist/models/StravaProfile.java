package com.example.athlist.models;

import java.util.List;

public class StravaProfile {
    private String athleteName;
    private String imageURL;
    private int following;
    private int followers;
    private int activitiesCount;
    private List<StravaActivity> activities;

    public StravaProfile() { }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public int getActivitiesCount() {
        return activitiesCount;
    }

    public void setActivitiesCount(int activitiesCount) {
        this.activitiesCount = activitiesCount;
    }

    public List<StravaActivity> getActivities() {
        return activities;
    }

    public void setActivities(List<StravaActivity> activities) {
        this.activities = activities;
    }
}
