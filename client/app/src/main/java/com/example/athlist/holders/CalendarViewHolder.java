package com.example.athlist.holders;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Build;
import android.view.View;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.adapters.CalendarViewAdapter;
import com.example.athlist.models.StravaActivity;

import java.util.ArrayList;
import java.util.List;

public class CalendarViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private final TextView dayOfMonth;
    private List<StravaActivity> stravaActivities;
    private final Context itemListener;
    private int weekOfMonth;
    public CalendarViewHolder(@NonNull View itemView, Context itemListener) {
        super(itemView);
        dayOfMonth=itemView.findViewById(R.id.textViewCellDay);
        this.itemListener=itemListener;
        itemView.setOnClickListener(this);
    }


    public TextView getDayOfMonth(){return dayOfMonth;}


    @Override
    public void onClick(View v) {
        ((CalendarViewAdapter.OnItemListener)itemListener).onItemClicked(stravaActivities,weekOfMonth);
    }

    public void addStravaActivity(StravaActivity activity) {
       if(stravaActivities==null && activity!=null)
           this.stravaActivities=new ArrayList<>();

       if(activity!=null)
         dayOfMonth.setBackgroundTintList(ColorStateList.valueOf( (ContextCompat.getColor(itemListener, R.color.orange))));
    }

    public int getWeekOfMonth() {
        return weekOfMonth;
    }

    public void setWeekOfMonth(int weekOfMonth) {
        this.weekOfMonth = weekOfMonth;
    }
}
