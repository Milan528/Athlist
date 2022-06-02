package com.example.athlist.clients;

import android.content.Context;
import android.graphics.Bitmap;

import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IConnectToStravaCallback;
import com.example.athlist.interfaces.IFetchLoggedUserDataListener;
import com.example.athlist.interfaces.ILoginUserCallback;
import com.example.athlist.interfaces.IRecoverPasswordCallback;
import com.example.athlist.interfaces.IRetrofitClient;
import com.example.athlist.interfaces.IScrapeUserDataCallback;
import com.example.athlist.interfaces.IUserRegistrationCallback;
import com.example.athlist.models.User;

import java.util.HashMap;

public class AppClient {
    private FirebaseAuthClient firebaseAuthClient;
    private FirebaseMapperClient firebaseMapperClient;
    private IRetrofitClient retrofitClient;
    private User loggedUser;

    private AppClient(){
        firebaseAuthClient=new FirebaseAuthClient();
        firebaseMapperClient=new FirebaseMapperClient();
        retrofitClient=new RetrofitClient();
    }

    public void connectToStrava(String email, String password, String uid, IConnectToStravaCallback callback){
        retrofitClient.connectToStrava(email,password,uid,callback);
    }

    public void scrapeUserData(String uid, IScrapeUserDataCallback callback){
        retrofitClient.scrapeUserData(uid,callback);
    }

    public User getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(User loggedUser) {
        this.loggedUser = loggedUser;
    }

    public static AppClient getInstance(){return SingletonHolder.instance;}

    public void loginUser(String emailText, String passwordText, ILoginUserCallback loginUserCallback) {
        firebaseAuthClient.loginUser(emailText,passwordText,loginUserCallback);
    }

    public void registerUser(HashMap<String, String> userData, IUserRegistrationCallback userRegistrationCallback,Context context) {
        firebaseAuthClient.registerUser(userData,userRegistrationCallback,context);
    }

    public void writeUserProfile(User user, Context context) {
        firebaseMapperClient.writeUserProfile(user,context);
    }

    public void writeUserProfileImage(String uid, Bitmap profilePhoto) {
        firebaseMapperClient.writeUserProfileImage(uid,profilePhoto);
    }

    public void writeUserBackgroundImage(String uid, Bitmap backgroundPhoto) {
        firebaseMapperClient.writeUserBackgroundImage(uid,backgroundPhoto);
    }

    public void readLoggedUserProfile(IFetchLoggedUserDataListener callback){
        firebaseMapperClient.readLoggedUserProfile(callback);
    }

    public void readUserProfile(String userID, IFetchLoggedUserDataListener callback){ firebaseMapperClient.readUserProfile(userID,callback);}

    public void recoverPassword(String emailRecoverText, IRecoverPasswordCallback recoverPasswordCallback) {
        firebaseAuthClient.recoverPassword(emailRecoverText,recoverPasswordCallback);
    }
    public void updateLoggedUserProfileInformation(){
        firebaseMapperClient.writeLoggedUserProfileInformation();
    }

    public void updateStravaConnectionStatus(StravaConnectionStatus status) {
        loggedUser.setConnectionStatus(status);
        firebaseMapperClient.writeConnectionStatus(loggedUser.getUserID(),status);
    }


    private static class SingletonHolder{
        public static final AppClient instance=new AppClient();
    }


}




