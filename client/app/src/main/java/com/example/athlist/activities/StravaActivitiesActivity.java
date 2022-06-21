package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.adapters.CalendarViewAdapter;
import com.example.athlist.clients.AppClient;
import com.example.athlist.models.AthleteEntry;
import com.example.athlist.models.MyCustomCalendar;
import com.example.athlist.models.StravaActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StravaActivitiesActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener,CalendarViewAdapter.OnItemListener {

    List<StravaActivity> stravaActivityList;
    List<String> athleteProfiles;
    MyCustomCalendar customCalendar;
    Button btnPreviousMonth,btnNextMonth,btnSave,btnDelete;
    Spinner spinnerAthleteProfiles;
    RecyclerView recyclerViewCalendar;
    TextView textViewMonthYear;
    LinearLayout calendarLinearLayout,daysOfWeekLinearLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_strava_activities);
        initializeComponents();
        setUpPage();
    }

    private void initializeComponents() {
        recyclerViewCalendar=(RecyclerView)findViewById(R.id.recyclerViewCalendar);
        textViewMonthYear=(TextView)findViewById(R.id.textViewMonthYear);
        btnNextMonth=(Button)findViewById(R.id.buttonNextMonth);
        btnPreviousMonth=(Button)findViewById(R.id.buttonPreviousMonth);
        calendarLinearLayout=findViewById(R.id.layoutCalendar);
        daysOfWeekLinearLayout=findViewById(R.id.calendar_layout_daysOfWeek);
        spinnerAthleteProfiles=findViewById(R.id.strava_activities_page_spinnerAthleteProfiles);
        btnSave=findViewById(R.id.strava_activities_page_saveProfile_button);
        btnDelete=findViewById(R.id.strava_activities_page_removeProfile_button);

        customCalendar=new MyCustomCalendar();
        stravaActivityList=new ArrayList<>();

        btnPreviousMonth.setOnClickListener(this);
        btnNextMonth.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnDelete.setOnClickListener(this);

        spinnerAthleteProfiles.setOnItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.buttonPreviousMonth){
            customCalendar.setSelectedDate(customCalendar.getSelectedDate().minusMonths(1));
            setMonthView();
        }else if(clickedId==R.id.buttonNextMonth){
            customCalendar.setSelectedDate(customCalendar.getSelectedDate().plusMonths(1));
            setMonthView();
        }else if(clickedId==R.id.strava_activities_page_saveProfile_button){
            saveProfile();
        }else if(clickedId==R.id.strava_activities_page_removeProfile_button){
            deleteProfile();
        }
    }

    private void deleteProfile() {
        if(spinnerAthleteProfiles.getSelectedItem().toString().isEmpty()){
            Toast.makeText(this, "Select a profile!", Toast.LENGTH_SHORT).show();
        }else{
            String profileName=spinnerAthleteProfiles.getSelectedItem().toString();
            AppClient.getInstance().deleteAthleteEntry(profileName);
            createSpinnerChoices();
        }
    }

    private void saveProfile() {
        if(spinnerAthleteProfiles.getSelectedItem().toString().isEmpty()){
            Toast.makeText(this, "Select a profile!", Toast.LENGTH_SHORT).show();
        }else{
            String profileName=spinnerAthleteProfiles.getSelectedItem().toString();
            Toast.makeText(this, profileName+" is being uploaded!", Toast.LENGTH_SHORT).show();
            AppClient.getInstance().saveAthleteEntry(profileName);
        }
    }




    public void setMonthView() {
        String month=customCalendar.getMonthFromDate();
        String year=customCalendar.getYearFromDate();
        textViewMonthYear.setText(month+" "+year);


        CalendarViewAdapter calendarViewAdapter=new CalendarViewAdapter(customCalendar,this, stravaActivityList, month,year);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager((Context) this,7);
        recyclerViewCalendar.setLayoutManager(layoutManager);
        recyclerViewCalendar.setAdapter(calendarViewAdapter);
    }

    private void setDefaultEnglish() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }


    private void setUpPage() {
        setDefaultEnglish();
        createSpinnerChoices();
        setMonthView();
    }

    private void createSpinnerChoices() {
        athleteProfiles=new ArrayList<>();
        athleteProfiles.add("");
        if(AppClient.getInstance().getLoggedUser().getAthleteProfiles()!=null && AppClient.getInstance().getLoggedUser().getAthleteProfiles().size()>0){
            for (AthleteEntry entry: AppClient.getInstance().getLoggedUser().getAthleteProfiles()) {
                athleteProfiles.add(entry.getEntryName());
            }
        }
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,athleteProfiles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAthleteProfiles.setAdapter(aa);
    }

    private void getActivitiesToDisplay(String entryName) {
        stravaActivityList.clear();
        stravaActivityList.addAll(AppClient.getInstance().getLoggedUser().getActivitiesForEntry(entryName));
        setMonthView();
    }

    @Override
    public void onItemClicked(List<StravaActivity> activities,int week) {
        Toast.makeText(this,"Week "+Integer.toString(week),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        if(!athleteProfiles.get(position).isEmpty()) {
            getActivitiesToDisplay(athleteProfiles.get(position));
        }
    }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}