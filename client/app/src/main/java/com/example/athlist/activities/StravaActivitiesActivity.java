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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.adapters.CalendarViewAdapter;
import com.example.athlist.adapters.ChartViewAdapter;
import com.example.athlist.clients.AppClient;
import com.example.athlist.enums.ChartTypes;
import com.example.athlist.models.MyCustomCalendar;
import com.example.athlist.models.StravaActivity;
import com.makeramen.roundedimageview.RoundedImageView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StravaActivitiesActivity extends AppCompatActivity implements View.OnClickListener,CalendarViewAdapter.OnItemListener {

    List<StravaActivity> stravaActivityList;
    MyCustomCalendar customCalendar;
    Button btnPreviousMonth,btnNextMonth;
    RecyclerView recyclerViewCalendar,recyclerViewCharts;
    TextView textViewMonthYear,textViewFilters;
    RoundedImageView upDownArrowImageView;
    LinearLayout calendarLinearLayout,daysOfWeekLinearLayout;
    ChartViewAdapter chartViewAdapter;
    boolean calendarExpanded;

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
        upDownArrowImageView=findViewById(R.id.strava_activities_page_upDownArrow_imageView);
        calendarLinearLayout=findViewById(R.id.layoutCalendar);
        recyclerViewCharts=findViewById(R.id.strava_activities_page_charts_recyclerView);
        textViewFilters=findViewById(R.id.strava_activities_page_filters_textView);
        daysOfWeekLinearLayout=findViewById(R.id.calendar_layout_daysOfWeek);

        calendarExpanded=true;

//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//        String date = "16/07/2022";
//
//        //convert String to LocalDate
//        LocalDate localDate = LocalDate.parse(date, formatter);
        customCalendar=new MyCustomCalendar();

        btnPreviousMonth.setOnClickListener(this);
        btnNextMonth.setOnClickListener(this);
        upDownArrowImageView.setOnClickListener(this);
        textViewFilters.setOnClickListener(this);
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
        }else if(clickedId==R.id.strava_activities_page_upDownArrow_imageView){
            toggleCalendar();
        }else if(clickedId==R.id.strava_activities_page_filters_textView){
            createSampleCharts();
        }
    }

    private void toggleCalendar() {
        if(calendarExpanded){
            calendarExpanded=false;
            recyclerViewCalendar.setVisibility(View.GONE);
            daysOfWeekLinearLayout.setVisibility(View.GONE);
            upDownArrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_down);

        }else{
            calendarExpanded=true;
            recyclerViewCalendar.setVisibility(View.VISIBLE);
            daysOfWeekLinearLayout.setVisibility(View.VISIBLE);
            upDownArrowImageView.setImageResource(R.drawable.ic_keyboard_arrow_up);
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
        if(AppClient.getInstance().getLoggedUser().getStravaProfile()!=null){
            if(AppClient.getInstance().getLoggedUser().getStravaProfile().getActivities()!=null){
                stravaActivityList=AppClient.getInstance().getLoggedUser().getStravaProfile().getActivities();
            }
         else
            {
                Toast.makeText(this,"There are no activities to be displayed",Toast.LENGTH_LONG).show();
             }
        }else{
            Toast.makeText(this,"Strava Profile has not been downloaded",Toast.LENGTH_LONG).show();
        }
        setMonthView();
    }

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

    @Override
    public void onItemClicked(List<StravaActivity> activities,int week) {
        Toast.makeText(this,"Week "+Integer.toString(week),Toast.LENGTH_LONG).show();
    }
}