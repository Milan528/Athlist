package com.example.athlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.holders.TableRowViewHolder;

import java.util.ArrayList;

public class TableViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    Context context;
    ArrayList<String[]> rows;

    public TableViewAdapter(Context context, ArrayList<String[]> rows) {
        this.context = context;
        this.rows = rows;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        RecyclerView.ViewHolder viewHolder;

        View view=inflater.inflate(R.layout.table_row_view,parent,false);
        viewHolder=new TableRowViewHolder(view,context);



       /* ViewGroup.LayoutParams layoutParams=view.getLayoutParams();
        layoutParams.height=(int)(parent.getWidth()*0.8);//velicina jende celije*/

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position==0){
            ((TableRowViewHolder)holder).createTableRow(rows.get(position),true);
        }else{
            ((TableRowViewHolder)holder).createTableRow(rows.get(position),false);
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
       // ((TableRowViewHolder)holder).checkIfHeader();
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }
}
