package com.example.athlist.holders;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;

import java.util.ArrayList;

import de.codecrafters.tableview.TableView;
import de.codecrafters.tableview.toolkit.SimpleTableDataAdapter;
import de.codecrafters.tableview.toolkit.SimpleTableHeaderAdapter;

public class TableViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    TableView tableView;
    Context context;

    public TableViewHolder(@NonNull View itemView, Context context) {
        super(itemView);
        this.context=context;
        tableView=itemView.findViewById(R.id.table_data_view);
    }

    @Override
    public void onClick(View view) {

    }

    public void createTable(ArrayList<String> headers, String[][]rows) {
        tableView.setHeaderAdapter(new SimpleTableHeaderAdapter(context, String.valueOf(headers)));
        tableView.setDataAdapter(new SimpleTableDataAdapter(context,rows));
    }
}
