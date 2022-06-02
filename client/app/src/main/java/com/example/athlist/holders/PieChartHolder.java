package com.example.athlist.holders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class PieChartHolder extends RecyclerView.ViewHolder {
    PieChart pieChart;
    ArrayList<String> labels;
    ArrayList<Integer> values;
    String graphTitle;
    Context context;

    public PieChartHolder(@NonNull View itemView,Context context ) {
        super(itemView);
        String graphTitle;
        this.context=context;
        pieChart=itemView.findViewById(R.id.myPieChart);
    }

    private void createBarChart() {
        ArrayList<PieEntry> pieEntries=new ArrayList<>();
        for(int i=0;i<values.size();i++){
            pieEntries.add(new PieEntry(values.get(i),labels.get(i)));
        }

        final int[] MY_COLORS = {Color.rgb(0,0,255), Color.rgb(255,0,0), Color.rgb(0,255,0),Color.rgb(153,0,153),Color.rgb(255,102,0),Color.rgb(0,255,255) };

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for(int c: MY_COLORS) colors.add(c);

        PieDataSet pieDataSet=new PieDataSet(pieEntries,"");
        pieDataSet.setColors(colors);
        PieData pieData=new PieData(pieDataSet);
        pieData.setValueTextSize(10f);
        pieChart.setData(pieData);


        pieChart.animate();
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText(graphTitle);
        //pieChart.invalidate();
    }

    public void setGraphParams(ArrayList<String> labels,ArrayList<Integer> values){
        this.values=values;
        this.labels=labels;
        createBarChart();
    }

    public String getGraphTitle() {
        return graphTitle;
    }

    public void setGraphTitle(String graphTitle) {
        this.graphTitle = graphTitle;
    }
}
