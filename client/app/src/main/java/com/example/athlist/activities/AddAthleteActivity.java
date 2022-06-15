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
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.interfaces.IScrapeMonthlyActivitiesCallback;
import com.example.athlist.models.AthleteEntry;
import com.example.athlist.models.StravaActivity;
import com.example.athlist.models.StravaMonthlyActivities;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class AddAthleteActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener {

    Spinner athleteProfileSpinner;
    EditText athleteEntryEditText,monthlyLinkEditText;
    Button addAthleteBtn,addActivitiesBtn,viewActivitiesBtn;
    ProgressBar progressBar;
    ArrayList<String> athleteProfiles;
    IScrapeMonthlyActivitiesCallback scrapeMonthlyActivitiesCallback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_athlete);
        initializeComponents();
        createSpinnerChoices();





    }

    private void initializeComponents() {
        athleteProfileSpinner=findViewById(R.id.add_athlete_page_spinnerAthleteProfiles);
        athleteEntryEditText=findViewById(R.id.add_athlete_page_editTextProfileName);
        monthlyLinkEditText=findViewById(R.id.add_athlete_page_editTextMonth);
        addAthleteBtn=findViewById(R.id.add_athlete_page_buttonAdd);
        addActivitiesBtn=findViewById(R.id.add_athlete_page_buttonAddActivities);
        viewActivitiesBtn=findViewById(R.id.add_athlete_page_buttonViewActivities);
        progressBar=findViewById(R.id.add_athlete_page_progressBar);

        scrapeMonthlyActivitiesCallback=new ScrapeMonthlyActivitiesCallback();


        athleteProfileSpinner.setOnItemSelectedListener(this);
        addAthleteBtn.setOnClickListener(this);
        addActivitiesBtn.setOnClickListener(this);
        viewActivitiesBtn.setOnClickListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.add_athlete_page_buttonAdd){
            addAthleteEntry();
        }else if(clickedId==R.id.add_athlete_page_buttonAddActivities){
            addActivities();
        }else if(clickedId==R.id.add_athlete_page_buttonViewActivities){
            viewActivities();
        }
    }

    private void addActivities() {
        String text = athleteProfileSpinner.getSelectedItem().toString();
        if(text.isEmpty()){
            Toast.makeText(this,"Athlete profile not selected",Toast.LENGTH_LONG).show();
        }else{
            if(monthlyLinkEditText.getText().toString().isEmpty()){
                monthlyLinkEditText.setError("Link can not be empty!");
            }else{
                String link=monthlyLinkEditText.getText().toString();
                AppClient.getInstance().scrapeMonthlyActivities(AppClient.getInstance().getLoggedUser().getUserID(),link,scrapeMonthlyActivitiesCallback);
                Toast.makeText(this, "Downloading activities may take a few minutes. Please wait", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private void addAthleteEntry() {
        if(athleteEntryEditText.getText().toString().isEmpty()){
            athleteEntryEditText.setError("Please enter "+athleteEntryEditText.getHint());
        }else{
            AthleteEntry entry=new AthleteEntry();
            entry.setEntryName(athleteEntryEditText.getText().toString());
            if(checkIfEntryExists(entry)){
                athleteEntryEditText.setError("Athlete Profile already in use");
            }else{
                AppClient.getInstance().getLoggedUser().getAthleteProfiles().add(entry);
                athleteProfiles.add(entry.getEntryName());
                createSpinnerChoices();
            }
        }
    }

    private boolean checkIfEntryExists(AthleteEntry entry) {
        if(AppClient.getInstance().getLoggedUser().getAthleteProfiles().contains(entry)){
            return true;
        }else{
            return false;
        }
    }

    private void createSpinnerChoices(){
        athleteProfiles=new ArrayList<>();
        athleteProfiles.add("");
        if(AppClient.getInstance().getLoggedUser().getAthleteProfiles()!=null && AppClient.getInstance().getLoggedUser().getAthleteProfiles().size()>0){
            for (AthleteEntry entry: AppClient.getInstance().getLoggedUser().getAthleteProfiles()) {
                athleteProfiles.add(entry.getEntryName());
            }
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,athleteProfiles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        athleteProfileSpinner.setAdapter(aa);
    }

    private void viewActivities() {
        Intent intent = new Intent(this, StravaActivitiesActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private class ScrapeMonthlyActivitiesCallback  implements IScrapeMonthlyActivitiesCallback{

        @Override
        public void scrapeUserMonthlyActivitiesSuccess(String msg, ArrayList<StravaActivity> monthlyActivities) {
           if(monthlyActivities==null || monthlyActivities.size() == 0)
               Toast.makeText(AddAthleteActivity.this, "No activities found in URL", Toast.LENGTH_SHORT).show();
           else{
               Toast.makeText(AddAthleteActivity.this, msg, Toast.LENGTH_SHORT).show();
               progressBar.setVisibility(View.INVISIBLE);
               StravaMonthlyActivities monthlyActivity=new StravaMonthlyActivities();
               monthlyActivity.setMonthlyActivities(monthlyActivities);


               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
               String date =monthlyActivity.getMonthlyActivities().get(0).getDate();      //'\nWednesday, June 1, 2022\n'
               LocalDate dateToTest = LocalDate.parse(date, formatter);
               YearMonth yearMonth=YearMonth.from(dateToTest);
               monthlyActivity.setMonthYear(yearMonth.toString());
               AppClient.getInstance().getLoggedUser().addAthleteProfile(athleteProfileSpinner.getSelectedItem().toString(),monthlyActivity);
           }
        }

        @Override
        public void scrapeUserMonthlyActivitiesFail(String msg) {

        }
    }
}