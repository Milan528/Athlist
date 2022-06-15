package com.example.athlist.interfaces;

public interface IChangePasswordCallback {
    void onPasswordChangeSuccess(String message);
    void onPasswordChangeFailed(String message);
}
