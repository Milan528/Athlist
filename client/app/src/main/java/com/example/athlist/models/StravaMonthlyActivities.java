package com.example.athlist.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;

public class StravaMonthlyActivities {
    private String monthYear;
    private ArrayList<StravaActivity> monthlyActivities;

    public StravaMonthlyActivities() { }


    public String getMonthYear() {
        return monthYear;
    }

    public void setMonthYear(String monthYear) {
        this.monthYear = monthYear;
    }

    public ArrayList<StravaActivity> getMonthlyActivities() {
        return monthlyActivities;
    }

    public void setMonthlyActivities(ArrayList<StravaActivity> monthlyActivities) {
        this.monthlyActivities = monthlyActivities;
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
        if (!(o instanceof StravaMonthlyActivities)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        StravaMonthlyActivities c = (StravaMonthlyActivities) o;

        // Compare the data members and return accordingly
        return this.monthYear.equals(c.getMonthYear());
    }

}
