package com.example.athlist.clients;



import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IConnectToStravaCallback;
import com.example.athlist.interfaces.IRetrofitAPI;
import com.example.athlist.interfaces.IRetrofitClient;
import com.example.athlist.interfaces.IScrapeMonthlyActivitiesCallback;
import com.example.athlist.interfaces.IScrapeUserDataCallback;
import com.example.athlist.models.MessageFromServer;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient implements IRetrofitClient {
    private  Retrofit mRetrofit;
    private  IRetrofitAPI mRetrofitAPI;
    private  String BASE_URL;  //"http://192.168.1.12:4000"; //"https://athlist.nutri4run.com/";

    public RetrofitClient(String serverAddress){
        BASE_URL="http://"+serverAddress+":4000";
         OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(6000, TimeUnit.SECONDS)
                .connectTimeout(6000, TimeUnit.SECONDS)
                .build();
        mRetrofit=new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        mRetrofitAPI=mRetrofit.create(IRetrofitAPI.class);
    }



    @Override
    public void connectToStrava(String email, String password, String uid,IConnectToStravaCallback callback) {
        HashMap<String, String> map=new HashMap<>();
        map.put("email",email);
        map.put("password",password);
        map.put("uid",uid);
        Call<MessageFromServer> call=mRetrofitAPI.connectToStrava(map);
        call.enqueue(new Callback<MessageFromServer>() {
            @Override
            public void onResponse(Call<MessageFromServer> call, Response<MessageFromServer> response) {
                if(!response.isSuccessful()){
                    callback.connectToStravaFail("Response was not successful");

                }else{
                    MessageFromServer msg= response.body();
                    if(response.code()==200){
                        AppClient.getInstance().updateStravaConnectionStatus(StravaConnectionStatus.CONNECTED);
                        callback.connectToStravaSuccess(msg.getMessage());

                    }else if(response.code()==500){
                        AppClient.getInstance().updateStravaConnectionStatus(StravaConnectionStatus.NOT_CONNECTED);
                        callback.connectToStravaFail(msg.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageFromServer> call, Throwable t) {
                callback.connectToStravaFail(t.getMessage());
            }
        });
    }

    @Override
    public void scrapeUserData(String uid,String date,IScrapeUserDataCallback callback) {
        HashMap<String, String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("date",date);

        Call<MessageFromServer> call=mRetrofitAPI.scrapeUserData(map);
        call.enqueue(new Callback<MessageFromServer>() {
            @Override
            public void onResponse(Call<MessageFromServer> call, Response<MessageFromServer> response) {
                if(!response.isSuccessful()){
                    callback.scrapeUserDataFail("Response was not successful");

                }else{
                    MessageFromServer msg = response.body();
                    //AppClient.getInstance().getLoggedUser().setStravaProfile(msg.getStravaProfile());
                    if(response.code()==200){
                        callback.scrapeUserDataSuccess(msg.getMessage(),msg.getActivities());
                    }else if(response.code()==500){
                        callback.scrapeUserDataFail(msg.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageFromServer> call, Throwable t) {
                callback.scrapeUserDataFail(t.getMessage());
            }
        });
    }

    @Override
    public void scrapeMonthlyActivities(String uid, String monthlyLink,IScrapeMonthlyActivitiesCallback callback) {
        HashMap<String, String> map=new HashMap<>();
        map.put("uid",uid);
        map.put("link",monthlyLink);
        Call<MessageFromServer> call=mRetrofitAPI.scrapeMonthlyActivities(map);
        call.enqueue(new Callback<MessageFromServer>() {
            @Override
            public void onResponse(Call<MessageFromServer> call, Response<MessageFromServer> response) {
                if(!response.isSuccessful()){
                    callback.scrapeUserMonthlyActivitiesFail("Response was not successful");

                }else{
                    MessageFromServer msg = response.body();
                    if(response.code()==200){
                        callback.scrapeUserMonthlyActivitiesSuccess(msg.getMessage(),msg.getActivities());
                    }else if(response.code()==500){
                        callback.scrapeUserMonthlyActivitiesFail(msg.getMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<MessageFromServer> call, Throwable t) {
                callback.scrapeUserMonthlyActivitiesFail(t.getMessage());
            }
        });
    }
}
