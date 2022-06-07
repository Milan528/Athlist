package com.example.athlist.models;

import android.graphics.Bitmap;

import com.example.athlist.enums.StravaConnectionStatus;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String phoneNumber;
    private String username;
    private List<ProfileInformationElements> additionalInformation;
    private StravaConnectionStatus connectionStatus;
    private List<AthleteEntry> athleteProfiles;

    @Exclude
    private StravaProfile stravaProfile;
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

    public List<ProfileInformationElements> getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(List<ProfileInformationElements> additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public StravaConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(StravaConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    public List<AthleteEntry> getAthleteProfiles() {
        return athleteProfiles;
    }

    public void setAthleteProfiles(List<AthleteEntry> athleteEntries) {
        this.athleteProfiles = athleteEntries;
    }


    @Exclude
    public String getUserID() {
        return userID;
    }
    @Exclude
    public void setUserID(String userID) {
        this.userID = userID;
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
    @Exclude
    public StravaProfile getStravaProfile() {
        return stravaProfile;
    }
    @Exclude
    public void setStravaProfile(StravaProfile stravaProfile) {
        this.stravaProfile = stravaProfile;
    }

    @Exclude
    public void addAthleteProfile(String entryName, StravaMonthlyActivities monthlyActivity) {
        AthleteEntry newEntry=new AthleteEntry();
        newEntry.setEntryName(entryName);
        if(athleteProfiles.contains(newEntry)){
            for(AthleteEntry entry:athleteProfiles){
                if(entry.getEntryName().equals(entryName)){
                    entry.addMonthlyActivity(monthlyActivity);
                }
            }
        }else{
            newEntry.addMonthlyActivity(monthlyActivity);
            athleteProfiles.add(newEntry);
        }
    }
}
