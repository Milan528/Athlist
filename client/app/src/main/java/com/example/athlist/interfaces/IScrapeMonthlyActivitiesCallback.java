package com.example.athlist.interfaces;

import com.example.athlist.models.StravaActivity;
import com.example.athlist.models.StravaMonthlyActivities;

import java.util.ArrayList;

public interface IScrapeMonthlyActivitiesCallback {
    void scrapeUserMonthlyActivitiesSuccess(String msg, ArrayList<StravaActivity> monthlyActivities);
    void scrapeUserMonthlyActivitiesFail(String msg);
}
