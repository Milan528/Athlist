package com.example.athlist.interfaces;

public interface IRetrofitClient {
    void connectToStrava(String email,String password,String uid,IConnectToStravaCallback callback);
    void scrapeUserData(String uid, IScrapeUserDataCallback callback);
}
