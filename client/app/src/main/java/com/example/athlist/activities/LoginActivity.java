package com.example.athlist.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;
import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.dialogs.RecoverPasswordDialog;
import com.example.athlist.interfaces.IFetchLoggedUserDataListener;
import com.example.athlist.interfaces.ILoginUserCallback;
import com.example.athlist.interfaces.IRecoverPasswordCallback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    TextView textViewForgotPassword,textViewRegister;
    EditText editTextEmail,editTextPassword;
    Button btnLogin;
    ProgressBar progressBar;
    ILoginUserCallback loginUserCallback;
    IRecoverPasswordCallback recoverPasswordCallback;
    IFetchLoggedUserDataListener userDataListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeComponents();

    }

    private void initializeComponents() {
        textViewForgotPassword=findViewById(R.id.login_page_textViewForgotPassword);
        textViewRegister=findViewById(R.id.login_page_textViewNewUser);
        editTextEmail=findViewById(R.id.login_page_editTextEmail);
        editTextPassword=findViewById(R.id.login_page_editTextTextPassword);
        btnLogin=findViewById(R.id.login_page_buttonLogin);
        progressBar=findViewById(R.id.login_page_progressBar);

        btnLogin.setOnClickListener(this);
        textViewForgotPassword.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);

        loginUserCallback=new LoginUserCallback();
        recoverPasswordCallback=new RecoverPasswordCallback();
        userDataListener=new FetchLoggedUserDataCallback();

    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.login_page_buttonLogin){
           loginUser();
        }else if(clickedId==R.id.login_page_textViewForgotPassword){
            showRecoverPasswordDialog();
        }else if(clickedId==R.id.login_page_textViewNewUser){
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
            this.finish();
        }
    }
    @Override
    public void onBackPressed() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    private boolean validateInfo(String emailText, String passwordText) {
        boolean valid=true;
        if(emailText.isEmpty())
        {
            editTextEmail.setError("Please enter email");
            editTextEmail.requestFocus();
            valid=false;
        }
        else if(passwordText.isEmpty())
        {
            editTextPassword.setError("Please enter password");
            editTextPassword.requestFocus();
            valid=false;
        }
        return valid;
    }
    private void loginUser()
    {
        String emailText = editTextEmail.getText().toString();
        String passwordText = editTextPassword.getText().toString();
        if(validateInfo(emailText,passwordText))
        {
            AppClient.getInstance().loginUser(emailText,passwordText,loginUserCallback);
            progressBar.setVisibility(View.VISIBLE);
        }
    }
    private void openHomePage(){
            progressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(this, HomePageActivity.class);
            startActivity(intent);
            this.finish();
    }

    private void showRecoverPasswordDialog()
    {
        RecoverPasswordDialog dialog=new RecoverPasswordDialog(this,recoverPasswordCallback);
        dialog.show(getSupportFragmentManager(),"RecoverPasswordDialog");
    }






    private class LoginUserCallback implements ILoginUserCallback {
        @Override
        public void userLoginSuccess() {
           AppClient.getInstance().readLoggedUserProfile(userDataListener);
        }
        @Override
        public void userLoginFailed() {
            Toast.makeText(LoginActivity.this,"Login failed. Please try again later", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private class RecoverPasswordCallback implements IRecoverPasswordCallback{

        @Override
        public void passwordRecoveryResult(String message) {
            Toast.makeText(LoginActivity.this,message, Toast.LENGTH_LONG).show();
        }
    }

    private class FetchLoggedUserDataCallback implements IFetchLoggedUserDataListener {

        @Override
        public void loggedUserFetchSuccess() { }

        @Override
        public void loggedUserProfileImageFetchSuccess() {
            openHomePage();
        }

        @Override
        public void loggedUserBackgroundFetchSuccess() { }

        @Override
        public void loggedUserFetchFailed(String message) { }
    }
}