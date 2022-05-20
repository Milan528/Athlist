package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.athlist.R;

public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    CardView profileCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeComponents();
    }

    private void initializeComponents() {
        profileCardView=findViewById(R.id.home_page_profile_cardView);

        profileCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.home_page_profile_cardView){
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        }
    }
}