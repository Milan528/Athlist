package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
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
import com.example.athlist.adapters.ChartViewAdapter;
import com.example.athlist.clients.AppClient;
import com.example.athlist.enums.ChartTypes;
import com.example.athlist.models.AthleteEntry;
import com.example.athlist.models.MyCustomCalendar;
import com.example.athlist.models.StravaActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
        //createSampleCharts();
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
        //super.onBackPressed();
        Intent intent = new Intent(this, HomePageActivity.class);
        startActivity(intent);
        finish();
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

    /*
        private void createSampleCharts(){
            ArrayList<ChartTypes> chartTypes=new ArrayList<>();
            chartTypes.add(ChartTypes.BAR_CHART);
            chartTypes.add(ChartTypes.PIE_CHART);
            ArrayList<ArrayList<String>> xValues=new ArrayList<>();
            ArrayList<ArrayList<Integer>> yValues=new ArrayList<>();
            ArrayList<String> graph1X=new ArrayList<>();
            graph1X.add("Jan");
            graph1X.add("Feb");
            graph1X.add("Mar");
            graph1X.add("Apr");
            graph1X.add("May");
            graph1X.add("Jun");
            graph1X.add("Jul");
            graph1X.add("Avg");
            graph1X.add("Sep");
            graph1X.add("Oct");
            graph1X.add("Nov");
            graph1X.add("Dec");
            ArrayList<Integer> graph1Y=new ArrayList<>();
            graph1Y.add(10);
            graph1Y.add(11);
            graph1Y.add(10);
            graph1Y.add(9);
            graph1Y.add(8);
            graph1Y.add(7);
            graph1Y.add(6);
            graph1Y.add(1);
            graph1Y.add(2);
            graph1Y.add(3);
            graph1Y.add(4);
            graph1Y.add(5);
            ArrayList<String> graph2X=new ArrayList<>();
            graph2X.add("W1");
            graph2X.add("Week2");
            graph2X.add("Week3");
            graph2X.add("Week4");
            graph2X.add("Week5");

            ArrayList<Integer> graph2Y=new ArrayList<>();
            graph2Y.add(20);
            graph2Y.add(50);
            graph2Y.add(10);
            graph2Y.add(5);
            graph2Y.add(35);

            xValues.add(graph1X);
            xValues.add(graph2X);

            yValues.add(graph1Y);
            yValues.add(graph2Y);


            ArrayList<String> xAxisTitles=new ArrayList<>();
            xAxisTitles.add("Activities");
            xAxisTitles.add("Distance");
            chartViewAdapter=new ChartViewAdapter(chartTypes,this,xValues,yValues,xAxisTitles);
            RecyclerView.LayoutManager layoutManager=new GridLayoutManager((Context) this,1);
            recyclerViewCharts.setLayoutManager(layoutManager);
            recyclerViewCharts.setAdapter(chartViewAdapter);
        }
    */
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