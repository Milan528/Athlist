package com.example.athlist.holders;

import android.content.Context;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class TableRowViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    Context context;
    TextView column1,column2,column3,column4,column5;
    ConstraintLayout tableLayout;
    boolean isHeader;

    public TableRowViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context=context;
        column1=itemView.findViewById(R.id.table_row_text1);
        column2=itemView.findViewById(R.id.table_row_text2);
        column3=itemView.findViewById(R.id.table_row_text3);
        column4=itemView.findViewById(R.id.table_row_text4);
        column5=itemView.findViewById(R.id.table_row_text5);
        tableLayout=itemView.findViewById(R.id.table_row_layout_tableLayout);
    }

    @Override
    public void onClick(View view) {

    }

    public void createTableRow(String[] rowData,boolean isHeader) {
        this.isHeader=isHeader;
       if(isHeader)
           createRowHeader(rowData);
       else
           createRowData(rowData);
    }

    private void createRowData(String[] rowData) {
        tableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        column1.setTextColor(ContextCompat.getColor(context, R.color.black));
        column2.setTextColor(ContextCompat.getColor(context, R.color.black));
        column3.setTextColor(ContextCompat.getColor(context, R.color.black));
        column4.setTextColor(ContextCompat.getColor(context, R.color.black));
        column5.setTextColor(ContextCompat.getColor(context, R.color.black));
        column1.setText(rowData[0]);
        column2.setText(rowData[1]);
        column3.setText(rowData[2]);
        column4.setText(rowData[3]);
        column5.setText(rowData[4]);

    }

    private void createRowHeader(String[] rowData) {
        tableLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.purple_500));
        column1.setTextColor(ContextCompat.getColor(context, R.color.white));
        column2.setTextColor(ContextCompat.getColor(context, R.color.white));
        column3.setTextColor(ContextCompat.getColor(context, R.color.white));
        column4.setTextColor(ContextCompat.getColor(context, R.color.white));
        column5.setTextColor(ContextCompat.getColor(context, R.color.white));
        column1.setText(rowData[0]);
        column2.setText(rowData[1]);
        column3.setText(rowData[2]);
        column4.setText(rowData[3]);
        column5.setText(rowData[4]);

    }


}
