package com.example.athlist.interfaces;


import com.example.athlist.models.MessageFromServer;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface IRetrofitAPI {
    @POST("/connectToStrava")
    Call<MessageFromServer> connectToStrava(@Body HashMap<String, String> map);

    @POST("/scrapeUserData")
    Call<MessageFromServer> scrapeUserData(@Body HashMap<String, String> map);

    @POST("/scrapeMonthlyActivities")
    Call<MessageFromServer> scrapeMonthlyActivities(@Body HashMap<String, String> map);
}
