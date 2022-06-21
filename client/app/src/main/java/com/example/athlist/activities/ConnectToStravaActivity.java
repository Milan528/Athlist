package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.dialogs.AddServerAddressDialog;
import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.interfaces.IConnectToStravaCallback;
import com.example.athlist.interfaces.IScrapeUserDataCallback;
import com.example.athlist.models.StravaActivity;
import com.example.athlist.models.StravaMonthlyActivities;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ConnectToStravaActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Button btnConnectToStrava,btnScrapeUserData,btnViewActivities,btcAddServerAddress;
    EditText editTextEmail,editTextPassword,editTextYear;
    TextView textViewConnectionStatus;
    ProgressBar progressBar;
    Spinner monthsSpinner;
    IConnectToStravaCallback connectToStravaCallback;
    IScrapeUserDataCallback scrapeUserDataCallback;
    ArrayList<String> months;
    int selectedMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_to_strava);

        initializeComponents();
        setupPage();
        createSpinnerChoices();
    }


    private void initializeComponents() {
        btnConnectToStrava=findViewById(R.id.connect_to_strava_buttonConnect);
        btnScrapeUserData=findViewById(R.id.connect_to_strava_buttonScrapeUserData);
        btnViewActivities=findViewById(R.id.connect_to_strava_buttonViewActivities);
        editTextEmail=findViewById(R.id.connect_to_strava_editTextEmail);
        editTextPassword=findViewById(R.id.connect_to_strava_editTextTextPassword);
        editTextYear=findViewById(R.id.connect_to_strava_yearToSearch_editText);
        textViewConnectionStatus=findViewById(R.id.connect_to_strava_textViewConnectionStatus);
        progressBar=findViewById(R.id.connect_to_strava_progressBar);
        monthsSpinner=findViewById(R.id.months_spinner);
        btcAddServerAddress=findViewById(R.id.connect_to_strava_buttonAddServerAddress);

        connectToStravaCallback=new ConnectToStravaCallback();
        scrapeUserDataCallback=new ScrapeUserDataCallback();
        selectedMonth=0;

        btnConnectToStrava.setOnClickListener(this);
        btnScrapeUserData.setOnClickListener(this);
        btnViewActivities.setOnClickListener(this);
        monthsSpinner.setOnItemSelectedListener(this);
        btcAddServerAddress.setOnClickListener(this);

    }


    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.connect_to_strava_buttonConnect){
                connectToStrava();
        }else if(clickedId==R.id.connect_to_strava_buttonScrapeUserData){
            scrapeUserData();
        }else if(clickedId==R.id.connect_to_strava_buttonViewActivities){
            viewActivities();
        }else if(clickedId==R.id.connect_to_strava_buttonAddServerAddress){
            addServerIpAddress();
        }
    }

    private void setupPage() {
        displayConnectionStatus();
        if(AppClient.getInstance().getLoggedUser().getConnectionStatus()== StravaConnectionStatus.NOT_CONNECTED) {
            btnScrapeUserData.setEnabled(false);
        }else{
            btnScrapeUserData.setEnabled(true);
        }
    }

    private void createSpinnerChoices() {
        months=new ArrayList<>();
        months.add("");
        months.add("January");
        months.add("February");
        months.add("March");
        months.add("April");
        months.add("May");
        months.add("June");
        months.add("July");
        months.add("August");
        months.add("September");
        months.add("October");
        months.add("November");
        months.add("December");


        ArrayAdapter athleteAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,months);
        athleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        monthsSpinner.setAdapter(athleteAdapter);

    }

    private void displayConnectionStatus() {
        String status="Connection status: \n"+AppClient.getInstance().getLoggedUser().getConnectionStatus().getName();
        textViewConnectionStatus.setText(status);
    }

    private void viewActivities() {
        Intent intent = new Intent(this, StravaActivitiesActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void scrapeUserData() {
        if(checkIfRetrofitCreated()) {
            if (monthsSpinner.getSelectedItem().toString().isEmpty()) {
                Toast.makeText(this, "Select a month to download!", Toast.LENGTH_LONG).show();
            } else if (editTextYear.getText().toString().isEmpty()) {
                Toast.makeText(this, "Enter a year to download!", Toast.LENGTH_LONG).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                AppClient.getInstance().scrapeUserData(AppClient.getInstance().getLoggedUser().getUserID(), selectedMonth + "/" + editTextYear.getText().toString(), scrapeUserDataCallback);
            }
        }
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

    private void connectToStrava() {
        if(checkIfRetrofitCreated()) {
            String emailText = editTextEmail.getText().toString();
            String passwordText = editTextPassword.getText().toString();
            if (validateInfo(emailText, passwordText)) {
                Toast.makeText(this, "Connecting to Strava. Please wait!", Toast.LENGTH_LONG).show();
                AppClient.getInstance().connectToStrava(emailText, passwordText, AppClient.getInstance().getLoggedUser().getUserID(), connectToStravaCallback);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addServerIpAddress() {
        AddServerAddressDialog dialog=new AddServerAddressDialog(this);
        dialog.show(getSupportFragmentManager(),"ServerAddressDialog");
    }

    private boolean checkIfRetrofitCreated(){
        if(AppClient.getInstance().getRetrofitClient()==null){
            Toast.makeText(this,"Add server address before downloading data!",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(this, HomePageActivity.class);
//        startActivity(intent);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(!months.get(position).isEmpty()) {
           selectedMonth=position;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        public void scrapeUserDataSuccess(String msg, ArrayList<StravaActivity> activities) {
            progressBar.setVisibility(View.INVISIBLE);
            if(activities==null || activities.size() == 0)
                Toast.makeText(ConnectToStravaActivity.this, "No activities found for selected date", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(ConnectToStravaActivity.this, msg, Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
                StravaMonthlyActivities monthlyActivity=new StravaMonthlyActivities();
                monthlyActivity.setMonthlyActivities(activities);


                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
                String date =monthlyActivity.getMonthlyActivities().get(0).getDate();      //'\nWednesday, June 1, 2022\n'
                LocalDate dateToTest = LocalDate.parse(date, formatter);
                YearMonth yearMonth=YearMonth.from(dateToTest);
                monthlyActivity.setMonthYear(yearMonth.toString());
                AppClient.getInstance().getLoggedUser().addAthleteProfile(AppClient.getInstance().getLoggedUser().getUsername(),monthlyActivity);
            }
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