package com.example.athlist.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class BarChartHolder extends RecyclerView.ViewHolder{
    BarChart barChart;
    ArrayList<String> xValues;
    ArrayList<Integer> yValues;
    String graphTitle;
    Context context;
    public BarChartHolder(@NonNull View itemView,Context context) {
        super(itemView);
        this.context=context;
        this.graphTitle="";
        barChart= itemView.findViewById(R.id.myBarChart);

    }



    private void createBarChart() {
        ArrayList<BarEntry> barEntries=new ArrayList<>();
        for(int i=0;i<yValues.size();i++){
            barEntries.add(new BarEntry(i,yValues.get(i)));
        }
        BarDataSet barDataSet=new BarDataSet(barEntries,graphTitle);
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        BarData barData=new BarData(barDataSet);
        barChart.setData(barData);

        XAxis xAxis=barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xValues));
        xAxis.setDrawAxisLine(false);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(xValues.size());
        xAxis.setLabelRotationAngle(290);
        barChart.animateY(2000);
        barChart.getDescription().setEnabled(false);
        //barChart.invalidate();
    }

    public void setXYAxis(ArrayList<String> xValues,ArrayList<Integer> yValues){
        this.xValues=xValues;
        this.yValues=yValues;
        createBarChart();
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }
}
