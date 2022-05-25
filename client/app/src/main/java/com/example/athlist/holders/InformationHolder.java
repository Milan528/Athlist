package com.example.athlist.holders;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.interfaces.IAddInformationClickedListener;
import com.example.athlist.models.ProfileInformationElements;


public class InformationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView titleImageView,addImageView;
    TextView titleTextView,informationTextView,addTextView;
    IAddInformationClickedListener listener;
    int position;
    ProfileInformationElements element;


    public InformationHolder(@NonNull View itemView, IAddInformationClickedListener callback) {
        super(itemView);
        titleImageView=itemView.findViewById(R.id.informationView_titleImage_holder_imageView);
        addImageView=itemView.findViewById(R.id.informationView_add_holder_imageView);
        titleTextView=itemView.findViewById(R.id.informationView_title_holder_textView);
        informationTextView=itemView.findViewById(R.id.informationView_information_textView);
        addTextView=itemView.findViewById(R.id.informationView_addInformation_textView);


        listener=callback;
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.informationView_add_holder_imageView || clickedId==R.id.informationView_addInformation_textView){
            listener.onAddInformationClicked();
        }
    }


    public void setPosition(int position) {
        this.position = position;
        if(position==0){
            addImageView.setOnClickListener(this);
            addTextView.setOnClickListener(this);
        }else{
            addImageView.setVisibility(View.INVISIBLE);
            addTextView.setVisibility(View.INVISIBLE);
        }
    }

    public void setElement(ProfileInformationElements element) {
        this.element = element;
        displayElement();
    }

    private void displayElement() {
        titleImageView.setImageResource(element.getImageID());
        titleTextView.setText(element.getTitle());
        informationTextView.setText(element.getData());
    }
}
