package com.example.athlist.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjuster;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;

public class MyCustomCalendar {
    LocalDate selectedDate;
    public  MyCustomCalendar(){
        selectedDate=LocalDate.now();
    }
    public MyCustomCalendar(LocalDate date){ selectedDate=date; }

    public LocalDate getSelectedDate(){return selectedDate;}
    public void setSelectedDate(LocalDate date){selectedDate=date;}


    public ArrayList<String> getDaysOfMonth() {
        ArrayList<String> daysOfMonth=new ArrayList<>();
        YearMonth yearMonth=YearMonth.from(selectedDate);
        int dayOfMonth=yearMonth.lengthOfMonth(); //broj dana u mesecu
        LocalDate firstOfMonth=selectedDate.withDayOfMonth(1);
        int dayOfWeek=firstOfMonth.getDayOfWeek().getValue();
        for(int i=1;i<=42;i++){
            if(i<=dayOfWeek || i>dayOfMonth+dayOfWeek){
                daysOfMonth.add("");
            }else
                daysOfMonth.add(String.valueOf(i-dayOfWeek));
        }
        return daysOfMonth;
    }

//    public String monthYearFromDate(){
//        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MMMM yyyy");
//        return selectedDate.format(formatter);
//    }

    public String getMonthFromDate() {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("MMMM");
        return selectedDate.format(formatter);
    }

    public String getYearFromDate() {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy");
        return selectedDate.format(formatter);
    }

    public int getWeekOfDay(String day){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MMMM/yyyy");
        String date =day+"/"+getMonthFromDate()+"/"+getYearFromDate();           //  "16/07/2022";
        LocalDate dateToTest = LocalDate.parse(date, formatter);


        YearMonth yearMonth=YearMonth.from(selectedDate);
        LocalDate firstOfMonth=yearMonth.atDay(1);
        TemporalAdjuster ta= TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY);
        LocalDate previousMonday=firstOfMonth.with(ta);
        LocalDate endOfMonth=yearMonth.atEndOfMonth();
        LocalDate weekStart=previousMonday;
        int week=1;
        do{
            LocalDate weekStop=weekStart.plusDays(6);
            if(dateToTest.isEqual(weekStart))
                return week;
            if(dateToTest.isEqual(weekStop))
                return week;
            if(dateToTest.isAfter(weekStart) && dateToTest.isBefore(weekStop))
                return week;

            weekStart=weekStart.plusWeeks(1);
            week++;
        }while(!weekStart.isAfter(endOfMonth));


        return -1;
    }
}
