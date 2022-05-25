package com.example.athlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.holders.InformationHolder;
import com.example.athlist.interfaces.IAddInformationClickedListener;
import com.example.athlist.models.ProfileInformationElements;

import java.util.List;

public class ProfileInformationAdapter extends RecyclerView.Adapter<InformationHolder> {
    List<ProfileInformationElements> mElements;
    IAddInformationClickedListener callback;


    public ProfileInformationAdapter(List<ProfileInformationElements> elements, IAddInformationClickedListener callback) {
        mElements=elements;
        this.callback=callback;
    }
    @NonNull
    @Override
    public InformationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        InformationHolder viewHolder;
        view=inflateView(R.layout.view_information_holder,parent);
        viewHolder=new InformationHolder(view,callback);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull InformationHolder holder, int position) {
        holder.setElement(mElements.get(position));
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mElements.size();
    }

    private View inflateView(int resource,ViewGroup parent){
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        return inflater.inflate(resource,parent,false);
    }

    public List<ProfileInformationElements> getElements() {
        return mElements;
    }

    public void setElements(List<ProfileInformationElements> mElements) {
        this.mElements = mElements;
        notifyDataSetChanged();
    }

    public void addElement(ProfileInformationElements element) {
        int index=mElements.size();
        mElements.add(index,element);
        notifyItemInserted(index);
    }
}
