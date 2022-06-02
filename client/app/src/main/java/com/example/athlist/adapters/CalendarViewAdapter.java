package com.example.athlist.adapters;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.holders.CalendarViewHolder;
import com.example.athlist.models.MyCustomCalendar;
import com.example.athlist.models.StravaActivity;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CalendarViewAdapter extends RecyclerView.Adapter<CalendarViewHolder> {
    private final MyCustomCalendar calendar;
    private final Context onItemListener;
    private final List<StravaActivity> displayedActivities;
    private final String year;
    private final String month;

    public CalendarViewAdapter(MyCustomCalendar calendar, Context listener, List<StravaActivity> stravaActivityList, String month, String year) {
        this.calendar =  calendar;
        this.onItemListener = listener;
        this.displayedActivities = stravaActivityList;
        this.month = month;
        this.year = year;
    }

    @NonNull
    @Override
    public CalendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view=inflater.inflate(R.layout.calendar_cell,parent,false);
        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=(int)(parent.getHeight()*0.1666666);//velicina jende celije
        /*layoutParams.height=(int)(parent.getWidth()*0.1666666);*/

        return new CalendarViewHolder(view, onItemListener);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull CalendarViewHolder holder, int position) {
            List<String> daysOfMonth=calendar.getDaysOfMonth();
            String holderDay=daysOfMonth.get(position);
            holder.getDayOfMonth().setText(holderDay);
            StravaActivity activity = null;
           if(displayedActivities!=null && displayedActivities.size()!=0 && holderDay!="") {
                try {
                    //activity = getStravaActivity(daysOfMonth.get(position));
                    activity = getStravaActivity(holderDay);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

           if(holderDay==""){
               holder.setWeekOfMonth(-1);
           }else{
               holder.setWeekOfMonth(calendar.getWeekOfDay(holderDay));
           }

           holder.addStravaActivity(activity);

    }

    private StravaActivity getStravaActivity(String day) throws ParseException {
        String holderDate=day+"/"+month+"/"+year;
        for(StravaActivity act : displayedActivities){
            if(compareDates(holderDate,act.getDate())){
                return act;
            }
        }
        return null;
    }


    private boolean compareDates(String holderDate, String activityDate) throws ParseException {
        Date holder=new SimpleDateFormat("dd/MMMM/yyyy").parse(holderDate);
        Date act=new SimpleDateFormat("EEE, MM/dd/yyyy").parse(activityDate);


        if(holder.compareTo(act)==0){
            return true;
        }

        return false;





    }



    @Override
    public int getItemCount() {
        return calendar.getDaysOfMonth().size();
    }



    public interface OnItemListener{
        void onItemClicked(List<StravaActivity> activities,int week);
    }
}
