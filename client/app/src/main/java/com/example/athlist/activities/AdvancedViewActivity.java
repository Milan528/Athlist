package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.models.AthleteEntry;
import com.example.athlist.models.StravaActivity;
import com.example.athlist.models.StravaMonthlyActivities;
import com.example.athlist.models.StravaWeeklyActivities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AdvancedViewActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    Spinner spinnerAthleteProfiles,spinnerDataSegments;
    EditText editTextStartDate,editTextEndDate;
    EditText clickedDate;
    Button btnDisplay;
    RoundedImageView upDownArrow;
    ConstraintLayout filtersViewConstraintLayout;
    ArrayList<String> athleteProfiles,dataSegments;
    boolean filtersViewExpanded;


    DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String month_name = new DateFormatSymbols().getMonths()[month];
            String date=dayOfMonth+"/"+month_name+"/"+year;
            clickedDate.setText(date);
        }
    };;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced_view);

        initializeComponents();
        createSpinnerChoices();
    }



    private void initializeComponents() {
        spinnerDataSegments=findViewById(R.id.advanced_view_page_spinnerDataSegments);
        spinnerAthleteProfiles=findViewById(R.id.advanced_view_page_spinnerAthleteProfiles);
        upDownArrow=findViewById(R.id.advanced_view_page_upDownArrow_imageView);
        filtersViewConstraintLayout=findViewById(R.id.advanced_view_page_filters_constraintLayout);
        editTextStartDate=findViewById(R.id.advanced_view_page_start_date_editText);
        editTextEndDate=findViewById(R.id.advanced_view_page_end_date_editText);
        btnDisplay=findViewById(R.id.advanced_view_page_display_button);

        filtersViewExpanded=true;

        spinnerDataSegments.setOnItemSelectedListener(this);
        spinnerAthleteProfiles.setOnItemSelectedListener(this);
        upDownArrow.setOnClickListener(this);
        editTextStartDate.setOnClickListener(this);
        editTextEndDate.setOnClickListener(this);
        btnDisplay.setOnClickListener(this);

    }

    private void createSpinnerChoices() {
        athleteProfiles=new ArrayList<>();
        dataSegments=new ArrayList<>();
        athleteProfiles.add("");
        if(AppClient.getInstance().getLoggedUser().getAthleteProfiles()!=null && AppClient.getInstance().getLoggedUser().getAthleteProfiles().size()>0){
            for (AthleteEntry entry: AppClient.getInstance().getLoggedUser().getAthleteProfiles()) {
                athleteProfiles.add(entry.getEntryName());
            }
        }
        ArrayAdapter athleteAdapter = new ArrayAdapter(this,android.R.layout.simple_spinner_item,athleteProfiles);
        athleteAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerAthleteProfiles.setAdapter(athleteAdapter);

        dataSegments.add("Year");
        dataSegments.add("Month");
        dataSegments.add("Week");
        ArrayAdapter segmentsAdapter=new ArrayAdapter(this,android.R.layout.simple_spinner_item,dataSegments);
        segmentsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDataSegments.setAdapter(segmentsAdapter);
    }






    private void toggleFiltersView() {
        if(filtersViewExpanded){
            filtersViewExpanded=false;
            filtersViewConstraintLayout.setVisibility(View.GONE);
            upDownArrow.setImageResource(R.drawable.ic_keyboard_arrow_down);

        }else{
            filtersViewExpanded=true;
            filtersViewConstraintLayout.setVisibility(View.VISIBLE);
            upDownArrow.setImageResource(R.drawable.ic_keyboard_arrow_up);
        }
    }

    private void setDefaultEnglish() {
        Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Resources resources = this.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    private void createDatePicker() {
        setDefaultEnglish();
        Calendar calendar= Calendar.getInstance();
        final int year=calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day=calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog=new DatePickerDialog(AdvancedViewActivity.this,dateSetListener,year,month,day);
        dialog.show();
    }


    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.advanced_view_page_upDownArrow_imageView){
            toggleFiltersView();
        }else if(clickedId==R.id.advanced_view_page_end_date_editText){
            clickedDate=editTextEndDate;
            createDatePicker();
        }else if(clickedId==R.id.advanced_view_page_start_date_editText){
            clickedDate=editTextStartDate;
            createDatePicker();
        }else if(clickedId==R.id.advanced_view_page_display_button){
            displayFilteredData();
        }

    }

    private void displayFilteredData() {
        if(!spinnerAthleteProfiles.getSelectedItem().toString().isEmpty()){
            if(!spinnerDataSegments.getSelectedItem().toString().isEmpty()){
                if(editTextStartDate.getText().toString().isEmpty() || editTextStartDate.getText().toString().equals("Start date")){
                    Toast.makeText(this,"Chose a starting date!",Toast.LENGTH_LONG).show();
                }else{
                    if(editTextEndDate.getText().toString().isEmpty() || editTextEndDate.getText().toString().equals("Start date")) {
                        Toast.makeText(this,"Chose an end date!",Toast.LENGTH_LONG).show();
                    }else{
                        try {
                            filterActivities(spinnerAthleteProfiles.getSelectedItem().toString(),spinnerDataSegments.getSelectedItem().toString(),editTextStartDate.getText().toString(),editTextEndDate.getText().toString());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }else{
                Toast.makeText(this,"Chose a segment to display",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this, "Select athlete profile!", Toast.LENGTH_SHORT).show();
        }
    }

    private void filterActivities(String profileName, String dataSegment, String startDate, String endDate) throws ParseException {
        ArrayList<StravaActivity> activities=new ArrayList<>(AppClient.getInstance().getLoggedUser().getActivitiesForEntry(profileName));
        if(dataSegment.equals("Year")){
            filterActivitiesByYear(activities,startDate,endDate);
        }else if(dataSegment.equals("Month")){
            filterActivitiesByMonth(activities,startDate,endDate);
        }else{
            filterActivitiesByWeek(activities,startDate,endDate);
        }
    }

    private void filterActivitiesByWeek(ArrayList<StravaActivity> activities, String startDate, String endDate) throws ParseException {
        ArrayList<StravaWeeklyActivities> filteredActivities=new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
        LocalDate searchStart = LocalDate.parse(startDate, formatter1);
        LocalDate searchEnd = LocalDate.parse(endDate, formatter1);
        StravaWeeklyActivities newWeeklyActivity;

        TemporalAdjuster ta = TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY);
        LocalDate weekStart = searchStart.with(ta);
        LocalDate weekStop;
        int week=1;
        do{
            weekStop=weekStart.plusDays(6);
            newWeeklyActivity=new StravaWeeklyActivities();
            newWeeklyActivity.setTag("Week "+String.valueOf(week));
            newWeeklyActivity.setWeekStart(weekStart);
            newWeeklyActivity.setWeekEnd(weekStop);
            newWeeklyActivity.setWeeklyActivities(new ArrayList<>());
            weekStart=weekStart.plusWeeks(1);
            filteredActivities.add(newWeeklyActivity);
            week++;
        }while(!weekStart.isAfter(searchEnd));


        LocalDate start,end,testDate;
        start=LocalDate.parse(startDate, formatter1);
        end=LocalDate.parse(endDate,formatter1);
        for(StravaActivity activity : activities){
            testDate=LocalDate.parse(activity.getDate(),formatter2);
            if((testDate.isEqual(end) || testDate.isEqual(start)) || (testDate.isAfter(start) && testDate.isBefore(end))){
                for(StravaWeeklyActivities weeklyActivities : filteredActivities){
                    if((testDate.isEqual(weeklyActivities.getWeekStart()) || testDate.isEqual(weeklyActivities.getWeekEnd())) ||(testDate.isAfter(weeklyActivities.getWeekStart()) && testDate.isBefore(weeklyActivities.getWeekEnd()))){
                        weeklyActivities.getWeeklyActivities().add(activity);
                        break;
                    }
                }
            }

        }

        String isItGood="OK";//crtanje tabele!
    }

    private void filterActivitiesByMonth(ArrayList<StravaActivity> activities, String startDate, String endDate) throws ParseException {
        ArrayList<StravaMonthlyActivities> filteredActivities=new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
        LocalDate start,end,testDate;
        start=LocalDate.parse(startDate, formatter1);
        end=LocalDate.parse(endDate,formatter1);
        YearMonth yearMonth;
        String year;
        StravaMonthlyActivities entry;
        for(StravaActivity activity : activities){
            testDate=LocalDate.parse(activity.getDate(),formatter2);
            if((testDate.isEqual(end) || testDate.isEqual(start)) || (testDate.isAfter(start) && testDate.isBefore(end))){
                yearMonth=YearMonth.from(testDate);
                entry=new StravaMonthlyActivities();
                entry.setMonthYear(yearMonth.toString());
                if(filteredActivities.contains(entry)){
                    for(StravaMonthlyActivities monthlyActivity : filteredActivities){
                        if(monthlyActivity.getMonthYear().equals(yearMonth.toString())){
                            if(monthlyActivity.getMonthlyActivities()==null){
                                monthlyActivity.setMonthlyActivities(new ArrayList<>());
                            }
                            monthlyActivity.getMonthlyActivities().add(activity);
                        }
                    }
                }else{
                    StravaMonthlyActivities newMonthlyActivity=new StravaMonthlyActivities();
                    newMonthlyActivity.setMonthYear(yearMonth.toString());
                    newMonthlyActivity.setMonthlyActivities(new ArrayList<>());
                    newMonthlyActivity.getMonthlyActivities().add(activity);
                    filteredActivities.add(newMonthlyActivity);
                }
            }
        }
        String isItGood="OK";//crtanje tabele!
    }

    private void filterActivitiesByYear(ArrayList<StravaActivity> activities, String startDate, String endDate) throws ParseException {
        ArrayList<StravaMonthlyActivities> filteredActivities=new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
        LocalDate start,end,testDate;
        start=LocalDate.parse(startDate, formatter1);
        end=LocalDate.parse(endDate,formatter1);
        YearMonth yearMonth;
        String year;
        StravaMonthlyActivities entry;
        for(StravaActivity activity : activities){
            testDate=LocalDate.parse(activity.getDate(),formatter2);
            if((testDate.isEqual(end) || testDate.isEqual(start)) || (testDate.isAfter(start) && testDate.isBefore(end))){
                yearMonth=YearMonth.from(testDate);
                year= String.valueOf(yearMonth.getYear());
                entry=new StravaMonthlyActivities();
                entry.setMonthYear(year);
                if(filteredActivities.contains(entry)){
                    for(StravaMonthlyActivities monthlyActivity : filteredActivities){
                        if(monthlyActivity.getMonthYear().equals(year)){
                            if(monthlyActivity.getMonthlyActivities()==null){
                                monthlyActivity.setMonthlyActivities(new ArrayList<>());
                            }
                            monthlyActivity.getMonthlyActivities().add(activity);
                        }
                    }
                }else{
                    StravaMonthlyActivities newMonthlyActivity=new StravaMonthlyActivities();
                    newMonthlyActivity.setMonthYear(year);
                    newMonthlyActivity.setMonthlyActivities(new ArrayList<>());
                    newMonthlyActivity.getMonthlyActivities().add(activity);
                    filteredActivities.add(newMonthlyActivity);
                }
            }
        }
        String isItGood="OK";//crtanje tabele!
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) { }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}