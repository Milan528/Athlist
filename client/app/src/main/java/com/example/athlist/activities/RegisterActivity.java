package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.interfaces.IUserRegistrationCallback;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnRegister;
    EditText editTextEmail,editTextPassword,editTextConfirmPassword,editTextPhoneNumber,editTextUsername;
    IUserRegistrationCallback userRegistrationCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initializeComponents();
    }

    private void initializeComponents(){
        btnRegister=findViewById(R.id.register_page_buttonRegister);
        editTextEmail=findViewById(R.id.register_page_editTextEmail);
        editTextPassword=findViewById(R.id.register_page_editTextPassword);
        editTextConfirmPassword=findViewById(R.id.register_page_editTextConfirmPassword);
        editTextPhoneNumber=findViewById(R.id.register_page_editTextPhoneNumber);
        editTextUsername=findViewById(R.id.register_page_editTextUsername);

        btnRegister.setOnClickListener(this);

        userRegistrationCallback=new UserRegistrationCallback();
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.register_page_buttonRegister){
          registerNewUser();
        }
    }

    private void registerNewUser() {
        String emailText = editTextEmail.getText().toString();
        String passwordText = editTextPassword.getText().toString();
        String confirmPasswordText = editTextConfirmPassword.getText().toString();
        String usernameText = editTextUsername.getText().toString();
        String phoneNumberText = editTextPhoneNumber.getText().toString();
        ArrayList<String> stringsToCheck=new ArrayList<String>();
        Collections.addAll(stringsToCheck,emailText,passwordText,confirmPasswordText,usernameText,phoneNumberText);
        ArrayList<EditText> errorHolders=new ArrayList<EditText>();
        Collections.addAll(errorHolders,editTextEmail,editTextPassword,editTextConfirmPassword,editTextUsername,editTextPhoneNumber);
        if(validateInfo(stringsToCheck,errorHolders))
        {
            if(validatePasswords(passwordText,confirmPasswordText)) {
                HashMap<String, String> userData = new HashMap<String, String>();
                userData.put("email", emailText);
                userData.put("username", usernameText);
                userData.put("phoneNumber", phoneNumberText);
                userData.put("password", passwordText);
                AppClient.getInstance().registerUser(userData,userRegistrationCallback,this);
            }
        }
    }

    private boolean validatePasswords(String passwordText, String confirmPasswordText) {
        if(passwordText.equals(confirmPasswordText) && passwordText.length()>=6) return true;
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        editTextPassword.setError("Passwords do not match!");
        editTextConfirmPassword.setError("Passwords do not match!");
        if(passwordText.length()<6){
            Toast.makeText(RegisterActivity.this,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
        }
        return false;
    }

    public boolean validateInfo(ArrayList<String> stringsToCheck, ArrayList<EditText> errorHolders) {
        boolean valid=true;
        for(int i=0;i<stringsToCheck.size();i++)
        {
            if(stringsToCheck.get(i).isEmpty()){
                errorHolders.get(i).setError("Please enter "+errorHolders.get(i).getHint());
                valid=false;
            }
        }
        return valid;
    }

    private void newUserRegistrationSuccess() {
        Thread thread = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(Toast.LENGTH_SHORT);
                    openLoginPage();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Toast.makeText(RegisterActivity.this,"Registration completed!",Toast.LENGTH_SHORT).show();
        thread.start();
    }

    private void openLoginPage(){
        Intent i=new Intent(this,LoginActivity.class);
        this.finish();
        startActivity(i);
    }


    private class UserRegistrationCallback implements IUserRegistrationCallback{

        @Override
        public void userRegistrationSuccess() {
            newUserRegistrationSuccess();
        }

        @Override
        public void userRegistrationFailed() {
            Toast.makeText(RegisterActivity.this,"Registration failed.Try again later",Toast.LENGTH_LONG).show();
        }
    }

}