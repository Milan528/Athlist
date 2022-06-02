package com.example.athlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.enums.ChartTypes;
import com.example.athlist.holders.BarChartHolder;
import com.example.athlist.holders.CalendarViewHolder;
import com.example.athlist.holders.PieChartHolder;

import java.util.ArrayList;


public class ChartViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<ChartTypes> chartTypes;
    Context context;
    ArrayList<ArrayList<String>> xValues;
    ArrayList<ArrayList<Integer>> yValues;
    ArrayList<String> xAxisTitles;


    public ChartViewAdapter(ArrayList<ChartTypes> chartTypes, Context context, ArrayList<ArrayList<String>> xValues, ArrayList<ArrayList<Integer>> yValues,ArrayList<String> xAxisTitles){
        this.context=context;
        this.chartTypes=chartTypes;
        this.xValues=xValues;
        this.yValues=yValues;
        this.xAxisTitles=xAxisTitles;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;
        ChartTypes type=chartTypes.get(viewType);

        View view;
        if(type==ChartTypes.BAR_CHART){
            view=inflater.inflate(R.layout.bar_chart_view,parent,false);
            viewHolder=new BarChartHolder(view,context);
        }else{
            view=inflater.inflate(R.layout.pie_chart_view,parent,false);
            viewHolder=new PieChartHolder(view,context);
        }


        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=(int)(parent.getWidth()*0.8);//velicina jende celije

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(chartTypes.get(position)==ChartTypes.BAR_CHART){
            ((BarChartHolder)holder).setGraphTitle(xAxisTitles.get(position));
            ((BarChartHolder)holder).setXYAxis(xValues.get(position),yValues.get(position));
        }else if(chartTypes.get(position)==ChartTypes.PIE_CHART){
            ((PieChartHolder)holder).setGraphTitle(xAxisTitles.get(position));
            ((PieChartHolder)holder).setGraphParams(xValues.get(position),yValues.get(position));
        }
    }

//    @Override
//    public void onBindViewHolder(@NonNull BarChartHolder holder, int position) {
//            holder.setGraphTitle(xAxisTitles.get(position));
//            holder.setXYAxis(xValues.get(position),yValues.get(position));
//    }

    @Override
    public int getItemCount() {
        return chartTypes.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
