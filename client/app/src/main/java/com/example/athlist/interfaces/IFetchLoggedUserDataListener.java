package com.example.athlist.interfaces;

public interface IFetchLoggedUserDataListener {
    void loggedUserFetchSuccess();
    void loggedUserProfileImageFetchSuccess();
    void loggedUserBackgroundFetchSuccess();
    void loggedUserFetchFailed(String message);
}
