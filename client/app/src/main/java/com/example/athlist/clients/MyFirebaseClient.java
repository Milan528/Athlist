package com.example.athlist.clients;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public abstract class MyFirebaseClient {
    protected final FirebaseAuth firebaseAuth;
    protected final FirebaseDatabase firebaseDatabase;
    protected final DatabaseReference databaseUserReference;
    protected final StorageReference storageReference;

    private static final String USER = "user";


    public MyFirebaseClient() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseUserReference = firebaseDatabase.getReference(USER);
    }

    public FirebaseAuth getFirebaseAuth() {
        return firebaseAuth;
    }
}
