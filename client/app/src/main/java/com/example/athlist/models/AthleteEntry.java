package com.example.athlist.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class AthleteEntry {
    private String entryName;
    private ArrayList<StravaMonthlyActivities> activities;

    public AthleteEntry() { }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }

    public ArrayList<StravaMonthlyActivities> getActivities() {
        return activities;
    }

    public void setActivities(ArrayList<StravaMonthlyActivities> activities) {
        this.activities = activities;
    }

    @Exclude
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof AthleteEntry)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        AthleteEntry c = (AthleteEntry) o;

        // Compare the data members and return accordingly
        return this.entryName.equals(c.getEntryName());
    }



    @Exclude
    public void addMonthlyActivity(StravaMonthlyActivities monthlyActivity) {
        if(activities==null)
            activities=new ArrayList<>();


        if(activities.contains(monthlyActivity))
            activities.remove(monthlyActivity);

        activities.add(monthlyActivity);
    }
}
