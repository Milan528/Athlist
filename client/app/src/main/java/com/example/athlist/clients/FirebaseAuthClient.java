package com.example.athlist.clients;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.Toast;
import com.example.athlist.R;
import androidx.annotation.NonNull;

import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IChangePasswordCallback;
import com.example.athlist.interfaces.ILoginUserCallback;
import com.example.athlist.interfaces.IRecoverPasswordCallback;
import com.example.athlist.interfaces.IUserRegistrationCallback;
import com.example.athlist.models.User;
import com.example.athlist.activities.*;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Objects;

public class FirebaseAuthClient extends MyFirebaseClient {

    public FirebaseAuthClient() {
        super();
    }

    public void loginUser(String emailText, String passwordText, final ILoginUserCallback callback){

        firebaseAuth.signInWithEmailAndPassword(emailText,passwordText).addOnCompleteListener(
                task -> {
                    if(!task.isSuccessful()) {
                        FirebaseAuthException e=(FirebaseAuthException)task.getException();
                        callback.userLoginFailed();
                        Log.e("LoginActivity","Login Registration",e);
                    }
                    else {
                        callback.userLoginSuccess();
                    }
                }
        );
    }



    public void registerUser(final HashMap<String,String> params, final IUserRegistrationCallback callback, Context context) {
        firebaseAuth.createUserWithEmailAndPassword(Objects.requireNonNull(params.get("email")), Objects.requireNonNull(params.get("password"))).addOnCompleteListener(
                task -> {
                    if(!task.isSuccessful()) {
                        FirebaseAuthException e=(FirebaseAuthException)task.getException();
                        Log.e("RegistrationActivity","Failed Registration",e);
                        callback.userRegistrationFailed();
                    }
                    else {
                        User user = new User();
                        user.setEmail(params.get("email"));
                        user.setUsername(params.get("username"));
                        user.setConnectionStatus(StravaConnectionStatus.NOT_CONNECTED);
                        AppClient.getInstance().writeUserProfile(user,context);
                        callback.userRegistrationSuccess();
                    }
                });
    }

    public void recoverPassword(String email, IRecoverPasswordCallback callback) {
        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        callback.passwordRecoveryResult("Email sent");
                    }
                    else {
                        callback.passwordRecoveryResult("Failed to send email");
                    }
                }).addOnFailureListener(e -> callback.passwordRecoveryResult(e.getMessage()));
    }




    public void changePassword(String newPassword,String oldPassword, IChangePasswordCallback listener){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(AppClient.getInstance().getLoggedUser().getEmail(), oldPassword);


        assert user != null;
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            user.updatePassword(newPassword).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                       listener.onPasswordChangeSuccess("Password update successful");
                                    } else {
                                        listener.onPasswordChangeFailed("Error password not updated");
                                    }
                                }
                            });
                        } else {
                            listener.onPasswordChangeFailed("Error auth failed");
                        }
                    }
                });

    }



}
