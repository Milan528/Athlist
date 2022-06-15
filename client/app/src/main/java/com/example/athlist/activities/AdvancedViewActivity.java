package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.adapters.TableViewAdapter;
import com.example.athlist.clients.AppClient;
import com.example.athlist.models.AthleteEntry;
import com.example.athlist.models.StravaActivity;
import com.example.athlist.models.StravaMonthlyActivities;
import com.example.athlist.models.StravaWeeklyActivities;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
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
    TableViewAdapter tableViewAdapter;
    RecyclerView dataRecyclerVIew;

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
        dataRecyclerVIew=findViewById(R.id.advanced_view_page_displayData_recyclerView);

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
        filteredActivities.removeIf(weeklyActivity -> weeklyActivity.getWeeklyActivities() == null || weeklyActivity.getWeeklyActivities().size() == 0);
        createTableWeeklyData(filteredActivities);
    }


    private void filterActivitiesByMonth(ArrayList<StravaActivity> activities, String startDate, String endDate) throws ParseException {
        ArrayList<StravaMonthlyActivities> filteredActivities=new ArrayList<>();
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("\nEEEE, MMMM d, yyyy\n");
        LocalDate start,end,testDate;
        start=LocalDate.parse(startDate, formatter1);
        end=LocalDate.parse(endDate,formatter1);
        YearMonth yearMonth;
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
        createTableMonthYearData(filteredActivities,"Month");
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
        createTableMonthYearData(filteredActivities,"Year");
    }
    private void createTableWeeklyData(ArrayList<StravaWeeklyActivities> filteredActivities) {
        String[] headers={"Segment","Distance","MovingTime/\nElapsedTime","Calories","Pace"};
        ArrayList<String[]> rows=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        rows.add(headers);
        String[] rowData={};
        int runs,pace,calories;
        long movingTM,elapsedTM;
        float distance;
        String myPace,tag;
        for(StravaWeeklyActivities weeklyActivity : filteredActivities){
            calories=0;
            runs=0;
            distance=0;
            movingTM=0;
            elapsedTM=0;
            for(StravaActivity activity : weeklyActivity.getWeeklyActivities()){
//                if(!activity.getCalories().isEmpty()) {
//                    runs++;
//                    calories += Integer.parseInt(activity.getCalories().replace("\n", "").replace("\r", "").replace(",",""));
//                    distance += Float.parseFloat(activity.getDistance().replace("km","").replace("\n", "").replace("\r", ""));
//                    movingTM += getTimeInSeconds(activity.getMovingTime().replace("\n", "").replace("\r", ""));
//                    elapsedTM += getTimeInSeconds(activity.getElapsedTime().replace("\n", "").replace("\r", ""));
//                }

                if(!activity.getCalories().isEmpty())
                    calories += Integer.parseInt(activity.getCalories().replace("\n", "").replace("\r", "").replace(",",""));
                if(!activity.getDistance().isEmpty()) {
                    runs++;
                    distance += Float.parseFloat(activity.getDistance().replace("km", "").replace("\n", "").replace("\r", ""));
                }
                if(!activity.getMovingTime().isEmpty())
                    movingTM += getTimeInSeconds(activity.getMovingTime().replace("\n", "").replace("\r", ""));

                if(!activity.getElapsedTime().isEmpty())
                    elapsedTM += getTimeInSeconds(activity.getElapsedTime().replace("\n", "").replace("\r", ""));
                else if(!activity.getMovingTime().isEmpty())
                    elapsedTM += getTimeInSeconds(activity.getMovingTime().replace("\n", "").replace("\r", ""));
            }
            pace=Math.round(movingTM/distance);
            myPace=getTimeStringFromSeconds(pace)+"/km";
            rowData = new String[]{weeklyActivity.getTag()+"\n"+weeklyActivity.getWeekStart().toString()+"\n"+weeklyActivity.getWeekEnd().toString(),
                    df.format(distance),
                    getTimeStringFromSeconds(movingTM)+"\n"+ getTimeStringFromSeconds(elapsedTM),
                    Integer.toString(calories),
                    myPace
            };
            rows.add(rowData);
        }

        createTableAdapter(rows);
    }


    private void createTableMonthYearData(ArrayList<StravaMonthlyActivities> filteredActivities,String type) {
        String[] headers={"Segment","Distance","MovingTime/\nElapsedTime","Calories","Pace"};
        ArrayList<String[]> rows=new ArrayList<>();
        DecimalFormat df = new DecimalFormat("0.00");
        rows.add(headers);
        String[] rowData={};
        int runs,pace,calories;
        long movingTM,elapsedTM;
        float distance;
        String myPace,tag;
        for(StravaMonthlyActivities monthlyActivity : filteredActivities){
            calories=0;
            runs=0;
            distance=0;
            movingTM=0;
            elapsedTM=0;
            for(StravaActivity activity : monthlyActivity.getMonthlyActivities()){
                if(!activity.getCalories().isEmpty())
                    calories += Integer.parseInt(activity.getCalories().replace("\n", "").replace("\r", "").replace(",",""));
                if(!activity.getDistance().isEmpty()) {
                    runs++;
                    distance += Float.parseFloat(activity.getDistance().replace("km", "").replace("\n", "").replace("\r", ""));
                }
                if(!activity.getMovingTime().isEmpty())
                    movingTM += getTimeInSeconds(activity.getMovingTime().replace("\n", "").replace("\r", ""));

                if(!activity.getElapsedTime().isEmpty())
                    elapsedTM += getTimeInSeconds(activity.getElapsedTime().replace("\n", "").replace("\r", ""));
                else if(!activity.getMovingTime().isEmpty())
                    elapsedTM += getTimeInSeconds(activity.getMovingTime().replace("\n", "").replace("\r", ""));

            }
            pace=Math.round(movingTM/distance);
            myPace=getTimeStringFromSeconds(pace)+"/km";
            if(type.equals("Year")) {
                tag = monthlyActivity.getMonthYear();
            }else{
                String monthYear=monthlyActivity.getMonthYear();
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
                YearMonth date = YearMonth.parse(monthYear, dateTimeFormatter);
                tag=date.getMonth().getDisplayName(TextStyle.SHORT,Locale.US)+" "+date.getYear();
            }

            rowData = new String[]{tag,
                    df.format(distance),
                    getTimeStringFromSeconds(movingTM)+"\n"+ getTimeStringFromSeconds(elapsedTM),
                    Integer.toString(calories),
                    myPace
            };
            rows.add(rowData);
        }

        createTableAdapter(rows);
    }

    private void createTableAdapter(ArrayList<String[]> rows) {
        tableViewAdapter=new TableViewAdapter(this,rows);
        RecyclerView.LayoutManager layoutManager=new GridLayoutManager((Context) this,1);
        dataRecyclerVIew.setLayoutManager(layoutManager);
        dataRecyclerVIew.setAdapter(tableViewAdapter);
    }


    private long getTimeInSeconds(String timeString){
        long sumTime=0;
        String[] times=timeString.split(":");
        ArrayList<String> times2=new ArrayList<>();
        Collections.addAll(times2,times);
        if(times2.size()==2){
            times2.add(0,"00");
        }
        sumTime += Integer.parseInt(times2.get(2));
        sumTime += 60 * Integer.parseInt(times2.get(1));
        sumTime += 3600 * Integer.parseInt(times2.get(0));

        return sumTime;
    }

    private String getTimeStringFromSeconds(long seconds){
        long hh = seconds / 3600;
        seconds %= 3600;
        long mm = seconds / 60;
        seconds %= 60;
        long ss = seconds;
        return format(hh)+":"+format(mm)+":"+format(ss);
    }

    private static String format(long s){
        if (s < 10) return "0" + s;
        else return "" + s;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) { }



    @Override
    public void onNothingSelected(AdapterView<?> adapterView) { }
}