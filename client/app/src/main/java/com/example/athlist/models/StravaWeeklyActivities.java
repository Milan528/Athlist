package com.example.athlist.models;

import java.time.LocalDate;
import java.util.ArrayList;


public class StravaWeeklyActivities {
    private LocalDate weekStart;
    private LocalDate weekEnd;
    private String tag;
    private ArrayList<StravaActivity> weeklyActivities;

    public StravaWeeklyActivities(){}

    public LocalDate getWeekStart() {
        return weekStart;
    }

    public void setWeekStart(LocalDate weekStart) {
        this.weekStart = weekStart;
    }

    public LocalDate getWeekEnd() {
        return weekEnd;
    }

    public void setWeekEnd(LocalDate weekEnd) {
        this.weekEnd = weekEnd;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public ArrayList<StravaActivity> getWeeklyActivities() {
        return weeklyActivities;
    }

    public void setWeeklyActivities(ArrayList<StravaActivity> weeklyActivities) {
        this.weeklyActivities = weeklyActivities;
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof StravaWeeklyActivities)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        StravaWeeklyActivities c = (StravaWeeklyActivities) o;

        // Compare the data members and return accordingly
        return this.tag.equals(c.getTag());
    }
}
