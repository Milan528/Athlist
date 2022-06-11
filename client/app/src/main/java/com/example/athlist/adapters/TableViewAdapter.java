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
import com.example.athlist.holders.PieChartHolder;
import com.example.athlist.holders.TableViewHolder;

import java.util.ArrayList;

public class TableViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<ArrayList<String>> headers;
    ArrayList<String[][]> rows;

    public TableViewAdapter(Context context, ArrayList<ArrayList<String>> headers, ArrayList<String[][]> rows) {
        this.context = context;
        this.headers = headers;
        this.rows = rows;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        View view=inflater.inflate(R.layout.table_view,parent,false);
        viewHolder=new TableViewHolder(view,context);



        ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=(int)(parent.getWidth()*0.8);//velicina jende celije

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((TableViewHolder)holder).createTable(headers.get(position),rows.get(position));

    }

    @Override
    public int getItemCount() {
        return headers.size();
    }
}
