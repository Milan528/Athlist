package com.example.athlist.interfaces;

import com.example.athlist.models.StravaActivity;

import java.util.ArrayList;

public interface IScrapeUserDataCallback {
    void scrapeUserDataSuccess(String msg, ArrayList<StravaActivity> activities);
    void scrapeUserDataFail(String msg);
}
