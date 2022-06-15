package com.example.athlist.models;

import android.graphics.Bitmap;

import com.example.athlist.enums.StravaConnectionStatus;
import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String email;
    private String username;
    private StravaConnectionStatus connectionStatus;

    @Exclude
    private List<AthleteEntry> athleteProfiles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }


    public StravaConnectionStatus getConnectionStatus() {
        return connectionStatus;
    }

    public void setConnectionStatus(StravaConnectionStatus connectionStatus) {
        this.connectionStatus = connectionStatus;
    }

    @Exclude
    public List<AthleteEntry> getAthleteProfiles() {
        return athleteProfiles;
    }
    @Exclude
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

    @Exclude
    public ArrayList<StravaActivity> getActivitiesForEntry(String entryName){
        AthleteEntry entry=new AthleteEntry();
        entry.setEntryName(entryName);
        ArrayList<StravaActivity> activities=new ArrayList<>();
        if(athleteProfiles!=null && athleteProfiles.contains(entry)){
            for(AthleteEntry athleteEntry:athleteProfiles){
                if(athleteEntry.getEntryName().equals(entryName)){
                    entry=athleteEntry;
                }
            }
          if(entry.getActivities()!=null && entry.getActivities().size()>0) {
              for (StravaMonthlyActivities monthlyActivity : entry.getActivities()) {
                  activities.addAll(monthlyActivity.getMonthlyActivities());
              }
          }
        }
        return activities;
    }

    @Exclude
    public AthleteEntry getAthleteEntryByName(String name){
        AthleteEntry entry=new AthleteEntry();
        entry.setEntryName(name);
        if(athleteProfiles.contains(entry)){
            for(AthleteEntry e : athleteProfiles){
                if (e.getEntryName().equals(name))
                    return e;
            }
        }
        return null;
    }

    public void removeAthleteEntryByName(String name) {
        AthleteEntry entry=new AthleteEntry();
        entry.setEntryName(name);
       athleteProfiles.remove(entry);
    }
}
