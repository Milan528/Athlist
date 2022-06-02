package com.example.athlist.clients;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.athlist.R;
import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IFetchLoggedUserDataListener;
import com.example.athlist.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class FirebaseMapperClient extends MyFirebaseClient{
    public FirebaseMapperClient() {
        super();
    }

    public void writeUserProfile(User user, Context context) {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        databaseUserReference.child(uid).setValue(user);
        Bitmap profileDefault= BitmapFactory.decodeResource(context.getResources(),R.drawable.profile_default);
        Bitmap backgroundDefault=BitmapFactory.decodeResource(context.getResources(),R.drawable.background_default);
        writeUserImages(uid,profileDefault,backgroundDefault);
    }

    private void writeUserImages(String uid, Bitmap profilePhoto, Bitmap backgroundPhoto) {
        writeUserProfileImage(uid,profilePhoto);
        writeUserBackgroundImage(uid,backgroundPhoto);
    }

    public void writeUserProfileImage(String uid, Bitmap profilePhoto){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profilePhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        storageReference.child("images").child(uid).child("profile.jpeg").putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("UploadPhoto","onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("UploadPhoto","onFailure",e.getCause());
            }
        });
    }
    public void writeUserBackgroundImage(String uid,Bitmap backgroundPhoto){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        backgroundPhoto.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        storageReference.child("images").child(uid).child("background.jpeg").putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.e("UploadPhoto","onSuccess");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("UploadPhoto","onFailure",e.getCause());
            }
        });
    }

    public void readUserProfile(String userID, IFetchLoggedUserDataListener callback) {
        databaseUserReference.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                assert user != null;
                user.setUserID(userID);

                if(user.getAdditionalInformation()==null)
                    user.setAdditionalInformation(new ArrayList<>());


                AppClient.getInstance().setLoggedUser(user);
                databaseUserReference.child(userID).removeEventListener(this);
                callback.loggedUserFetchSuccess();
                getUserImages(userID, callback);
                readStravaProfile();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                callback.loggedUserFetchFailed(error.getMessage());
            }
        });

    }

    private void readStravaProfile() {
        //not implemented
    }

    private void writeStravaProfile(){
        //not implemented
    }

    private void getUserImages(String userID, IFetchLoggedUserDataListener callback) {
        StorageReference profileImageStorageReference=storageReference.child("images").child(userID).child("profile.jpeg");
        StorageReference backgroundImageStorageReference=storageReference.child("images").child(userID).child("background.jpeg");

        getProfileImageFromLocalFile("profile",".jpeg",profileImageStorageReference,callback);
        getBackgroundImageFromLocalFile("background",".jpeg",backgroundImageStorageReference,callback);
    }

    private void getProfileImageFromLocalFile(String prefix, String suffix, StorageReference imageStorageReference, IFetchLoggedUserDataListener callback) {
        try {
            File localFile=File.createTempFile(prefix,suffix);
            imageStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    AppClient.getInstance().getLoggedUser().setProfilePhoto(bm);
                    callback.loggedUserProfileImageFetchSuccess();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getBackgroundImageFromLocalFile(String prefix, String suffix, StorageReference imageStorageReference, IFetchLoggedUserDataListener callback) {
        try {
            File localFile=File.createTempFile(prefix,suffix);
            imageStorageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bm = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    AppClient.getInstance().getLoggedUser().setBackgroundPhoto(bm);
                    callback.loggedUserBackgroundFetchSuccess();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readLoggedUserProfile(IFetchLoggedUserDataListener callback) {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        readUserProfile(uid,callback);
    }

    public void writeLoggedUserProfileInformation() {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        databaseUserReference.child(uid).child("additionalInformation").setValue(AppClient.getInstance().getLoggedUser().getAdditionalInformation());
    }

    public void writeConnectionStatus(String userID, StravaConnectionStatus status) {
        databaseUserReference.child(userID).child("connectionStatus").setValue(status);
    }
}
