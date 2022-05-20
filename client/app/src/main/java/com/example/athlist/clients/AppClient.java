package com.example.athlist.clients;

import com.example.athlist.interfaces.ILoginUserCallback;
import com.example.athlist.interfaces.IRecoverPasswordCallback;
import com.example.athlist.interfaces.IUserRegistrationCallback;
import com.example.athlist.models.User;

import java.util.HashMap;

public class AppClient {
    private FirebaseAuthClient firebaseAuthClient;
    private FirebaseMapperClient firebaseMapperClient;

    private AppClient(){
        firebaseAuthClient=new FirebaseAuthClient();
        firebaseMapperClient=new FirebaseMapperClient();
    }

    public static AppClient getInstance(){return SingletonHolder.instance;}

    public void loginUser(String emailText, String passwordText, ILoginUserCallback loginUserCallback) {
        firebaseAuthClient.loginUser(emailText,passwordText,loginUserCallback);
    }

    public void registerUser(HashMap<String, String> userData, IUserRegistrationCallback userRegistrationCallback) {
        firebaseAuthClient.registerUser(userData,userRegistrationCallback);
    }

    public void writeUserProfile(User user) {
        firebaseMapperClient.writeUserProfile(user);
    }

    public void recoverPassword(String emailRecoverText, IRecoverPasswordCallback recoverPasswordCallback) {
        firebaseAuthClient.recoverPassword(emailRecoverText,recoverPasswordCallback);
    }


    private static class SingletonHolder{
        public static final AppClient instance=new AppClient();
    }
}




