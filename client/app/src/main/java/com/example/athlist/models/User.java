package com.example.athlist.models;

import android.graphics.Bitmap;

import com.google.firebase.database.Exclude;

public class User {
    private String email;
    private String phoneNumber;
    private String username;
    @Exclude
    private String userID;
    @Exclude
    private Bitmap profilePhoto;
    @Exclude
    private Bitmap backgroundPhoto;

    public User() {}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    @Exclude
    public String getUserID() {
        return userID;
    }
    @Exclude
    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @Exclude
    public Bitmap getProfilePhoto() {
        return profilePhoto;
    }
    @Exclude
    public void setProfilePhoto(Bitmap profilePhoto) {
        this.profilePhoto = profilePhoto;
    }
    @Exclude
    public Bitmap getBackgroundPhoto() {
        return backgroundPhoto;
    }
    @Exclude
    public void setBackgroundPhoto(Bitmap backgroundPhoto) {
        this.backgroundPhoto = backgroundPhoto;
    }
}
