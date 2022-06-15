package com.example.athlist.interfaces;

public interface IRetrofitClient {
    void connectToStrava(String email,String password,String uid,IConnectToStravaCallback callback);
    void scrapeUserData(String uid,String date ,IScrapeUserDataCallback callback);
    void scrapeMonthlyActivities(String uid, String monthlyLink, IScrapeMonthlyActivitiesCallback callback);
}
