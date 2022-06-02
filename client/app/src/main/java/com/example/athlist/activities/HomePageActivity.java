package com.example.athlist.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.dialogs.LoadingDialog;
import com.example.athlist.interfaces.IFetchLoggedUserDataListener;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


public class HomePageActivity extends AppCompatActivity implements View.OnClickListener {

    CardView profileCardView,connectToStravaCardView,viewActivitiesCardView;
    RoundedImageView profileImage;
    TextView welcomeTextView, todaysDateTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initializeComponents();
        displayDateAndGreetUser();
    }



    private void initializeComponents() {
        profileCardView=findViewById(R.id.home_page_profile_cardView);
        connectToStravaCardView=findViewById(R.id.home_page_connectToStrava_cardView);
        viewActivitiesCardView=findViewById(R.id.home_page_viewActivities_cardView);
        profileImage=findViewById(R.id.home_page_imageViewProfile);
        welcomeTextView=findViewById(R.id.home_page_welcome_textView);
        todaysDateTextView=findViewById(R.id.home_page_todaysDate_textView);

        profileImage.setImageBitmap(AppClient.getInstance().getLoggedUser().getProfilePhoto());
        profileCardView.setOnClickListener(this);
        connectToStravaCardView.setOnClickListener(this);
        viewActivitiesCardView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.home_page_profile_cardView){
            Intent intent = new Intent(this, MyProfile.class);
            startActivity(intent);
        }else if(clickedId==R.id.home_page_connectToStrava_cardView){
            Intent intent = new Intent(this, ConnectToStravaActivity.class);
            startActivity(intent);
        }else if(clickedId==R.id.home_page_viewActivities_cardView) {
            Intent intent = new Intent(this, StravaActivitiesActivity.class);
            startActivity(intent);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void displayDateAndGreetUser() {
        LocalDate todaysDate=LocalDate.now();
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("dd MMMM yyyy");
        //int day=todaysDate.getDayOfMonth();
        String dateToDisplay=todaysDate.format(formatter);
        todaysDateTextView.setText(dateToDisplay);
        greetUser();
        displayProfileImage();
    }

    private void displayProfileImage() {
        Bitmap bitmap=AppClient.getInstance().getLoggedUser().getProfilePhoto();
        if(bitmap!=null){
            profileImage.setImageBitmap(bitmap);
        }else{
            profileImage.setImageResource(R.drawable.profile_default);
        }
    }

    private void greetUser() {
        Date currentDate = new Date();
        String username=AppClient.getInstance().getLoggedUser().getUsername();
        SimpleDateFormat formatter = new SimpleDateFormat("kk");
        String timeIn24Hours = formatter.format(currentDate);
        int currentHour=Integer.parseInt(timeIn24Hours);
        if(currentHour>=5 && currentHour<12){
            displayGreetings("Good Morning "+username);
        }else if(currentHour>=12 && currentHour<17)
            displayGreetings("Good Afternoon "+username);
//        else if(currentHour>=17 || currentHour<5)
//            displayGreetings("Good Evening "+username);
        else
            displayGreetings("Good Evening "+username);
    }

    private void displayGreetings(String greetings) {
        welcomeTextView.setText(greetings);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        profileImage.setImageBitmap(AppClient.getInstance().getLoggedUser().getProfilePhoto());
    }
}