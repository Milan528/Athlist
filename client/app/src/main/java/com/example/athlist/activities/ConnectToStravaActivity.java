package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IConnectToStravaCallback;
import com.example.athlist.interfaces.IScrapeUserDataCallback;

public class ConnectToStravaActivity extends AppCompatActivity implements View.OnClickListener {

    Button btnConnectToStrava,btnScrapeUserData,btnViewProfile,btnViewActivities;
    EditText editTextEmail,editTextPassword;
    TextView textViewConnectionStatus;
    ProgressBar progressBar;
    IConnectToStravaCallback connectToStravaCallback;
    IScrapeUserDataCallback scrapeUserDataCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_strava);

        initializeComponents();
        setupPage();
    }

    private void setupPage() {
        displayConnectionStatus();

        if(AppClient.getInstance().getLoggedUser().getConnectionStatus()== StravaConnectionStatus.NOT_CONNECTED) {
            btnScrapeUserData.setEnabled(false);
        }else{
            btnScrapeUserData.setEnabled(true);
        }
        if(AppClient.getInstance().getLoggedUser().getStravaProfile()==null){
            btnViewProfile.setEnabled(false);
            btnViewActivities.setEnabled(false);
        }else{
            btnViewProfile.setEnabled(true);
            btnViewActivities.setEnabled(true);
        }



    }

    private void displayConnectionStatus() {
        String status="Connection status: \n"+AppClient.getInstance().getLoggedUser().getConnectionStatus().getName();
        textViewConnectionStatus.setText(status);
    }

    private void initializeComponents() {
        btnConnectToStrava=findViewById(R.id.connect_to_strava_buttonConnect);
        btnScrapeUserData=findViewById(R.id.connect_to_strava_buttonScrapeUserData);
        btnViewProfile=findViewById(R.id.connect_to_strava_buttonViewProfile);
        btnViewActivities=findViewById(R.id.connect_to_strava_buttonViewActivities);
        editTextEmail=findViewById(R.id.connect_to_strava_editTextEmail);
        editTextPassword=findViewById(R.id.connect_to_strava_editTextTextPassword);
        textViewConnectionStatus=findViewById(R.id.connect_to_strava_textViewConnectionStatus);
        progressBar=findViewById(R.id.connect_to_strava_progressBar);

        connectToStravaCallback=new ConnectToStravaCallback();
        scrapeUserDataCallback=new ScrapeUserDataCallback();

        btnConnectToStrava.setOnClickListener(this);
        btnScrapeUserData.setOnClickListener(this);
        btnViewProfile.setOnClickListener(this);
        btnViewActivities.setOnClickListener(this);



    }



    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.connect_to_strava_buttonConnect){
                connectToStrava();
        }else if(clickedId==R.id.connect_to_strava_buttonScrapeUserData){
            scrapeUserData();
        }else if(clickedId==R.id.connect_to_strava_buttonViewProfile){
//            Intent intent = new Intent(this, RegisterActivity.class);
//            startActivity(intent);
        }else if(clickedId==R.id.connect_to_strava_buttonViewActivities){
            viewActivities();
        }
    }

    private void viewActivities() {
        Intent intent = new Intent(this, StravaActivitiesActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void scrapeUserData() {
        progressBar.setVisibility(View.VISIBLE);
        AppClient.getInstance().scrapeUserData(AppClient.getInstance().getLoggedUser().getUserID(),scrapeUserDataCallback);
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

    private void connectToStrava()
    {
        String emailText = editTextEmail.getText().toString();
        String passwordText = editTextPassword.getText().toString();
        if(validateInfo(emailText,passwordText))
        {
            AppClient.getInstance().connectToStrava(emailText,passwordText,AppClient.getInstance().getLoggedUser().getUserID(),connectToStravaCallback);
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
    }


    private class ConnectToStravaCallback implements IConnectToStravaCallback{

        @Override
        public void connectToStravaSuccess(String msg) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ConnectToStravaActivity.this,msg,Toast.LENGTH_LONG).show();
            setupPage();
        }

        @Override
        public void connectToStravaFail(String msg) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ConnectToStravaActivity.this,msg,Toast.LENGTH_LONG).show();
            setupPage();
        }
    }

    private class ScrapeUserDataCallback implements IScrapeUserDataCallback{

        @Override
        public void scrapeUserDataSuccess(String msg) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ConnectToStravaActivity.this,msg,Toast.LENGTH_LONG).show();
            setupPage();
        }

        @Override
        public void scrapeUserDataFail(String msg) {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(ConnectToStravaActivity.this,msg,Toast.LENGTH_LONG).show();
            setupPage();
        }
    }
}