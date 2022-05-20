package com.example.athlist.clients;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.athlist.R;
import com.example.athlist.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

public class FirebaseMapperClient extends MyFirebaseClient{
    public FirebaseMapperClient() {
        super();
    }

    public void writeUserProfile(User user) {
        String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
        databaseUserReference.child(uid).setValue(user);
        Bitmap profileDefault= BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.profile_default);
        Bitmap backgroundDefault=BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.background_default);
        writeUserImages(uid,profileDefault,backgroundDefault);
    }

    private void writeUserImages(String uid, Bitmap profilePhoto, Bitmap backgroundPhoto) {
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
}
