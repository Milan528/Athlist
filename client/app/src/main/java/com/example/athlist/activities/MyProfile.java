package com.example.athlist.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.athlist.R;

import com.example.athlist.clients.AppClient;
import com.example.athlist.fragments.MyProfileInformationFragment;
import com.example.athlist.fragments.StravaConnectionStatusFragment;
import com.example.athlist.fragments.StravaProfileInformationFragment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;


public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    TextView usernameTextView,stravaTextView,myProfileTextView;
    ImageView backgroundImageView;
    RoundedImageView profileImageView;
    MyProfileInformationFragment myProfileInformationFragment;
    StravaProfileInformationFragment stravaProfileInformationFragment;
    StravaConnectionStatusFragment stravaConnectionStatusFragment;
    boolean myProfileSelected,profileImageSelection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initializeComponents();
        setUpPage();

    }

    private void setUpPage() {
        setUpFragments();
        displayMyProfilePage();

    }




    private void initializeComponents() {

        usernameTextView=findViewById(R.id.profile_page_username_textView);
        myProfileTextView=findViewById(R.id.profile_page_my_profile_textView);
        stravaTextView=findViewById(R.id.profile_page_strava_profile_textView);
        backgroundImageView=findViewById(R.id.profile_page_background_imageView);
        profileImageView=findViewById(R.id.profile_page_profile_imageView);

        backgroundImageView.setImageBitmap(AppClient.getInstance().getLoggedUser().getBackgroundPhoto());
        profileImageView.setImageBitmap(AppClient.getInstance().getLoggedUser().getProfilePhoto());

        profileImageView.setOnClickListener(this);
        backgroundImageView.setOnClickListener(this);
        stravaTextView.setOnClickListener(this);
        myProfileTextView.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.profile_page_strava_profile_textView){
           displayStravaProfilePage();
        }else if(clickedId==R.id.profile_page_my_profile_textView){
           displayMyProfilePage();
        }else if(clickedId==R.id.profile_page_background_imageView){
            profileImageSelection=false;
            selectProfileImage();
        }else if(clickedId==R.id.profile_page_profile_imageView){
                profileImageSelection = true;
                selectProfileImage();
        }

    }



    void displayMyProfilePage(){
        usernameTextView.setText(AppClient.getInstance().getLoggedUser().getUsername());
        myProfileTextView.setTypeface(Typeface.DEFAULT_BOLD);
        myProfileTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        stravaTextView.setTextColor(ContextCompat.getColor(this, R.color.gray));
        myProfileTextView.setPaintFlags(myProfileTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        stravaTextView.setPaintFlags(View.INVISIBLE);
        stravaTextView.setTypeface(Typeface.DEFAULT);

        myProfileSelected=true;
        displayFragment(myProfileInformationFragment);

    }

    void displayStravaProfilePage(){
        stravaTextView.setTypeface(Typeface.DEFAULT_BOLD);
        stravaTextView.setPaintFlags(myProfileTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        myProfileTextView.setTypeface(Typeface.DEFAULT);
        stravaTextView.setTextColor(ContextCompat.getColor(this, R.color.black));
        myProfileTextView.setTextColor(ContextCompat.getColor(this, R.color.gray));
        myProfileTextView.setPaintFlags(View.INVISIBLE);

        myProfileSelected=false;
        if(AppClient.getInstance().getLoggedUser().getConnectedToStrava()) {
            displayFragment(stravaProfileInformationFragment);
        }else{
            displayFragment(stravaConnectionStatusFragment);
        }
    }

    private void setUpFragments() {
        myProfileInformationFragment=new MyProfileInformationFragment();
        stravaProfileInformationFragment=new StravaProfileInformationFragment();
        stravaConnectionStatusFragment=new StravaConnectionStatusFragment();
    }

    private void displayFragment(Fragment fragment){

        String backStackName=fragment.getClass().getName();
        FragmentManager manager=getSupportFragmentManager();
        boolean popped=manager.popBackStackImmediate(backStackName,0);
        if(!popped){
            manager.beginTransaction()
                    .replace(R.id.profile_page_fragmentHost_frameLayout,fragment)
                    .addToBackStack(backStackName)
                    .commit();
        }

    }

    private void selectProfileImage(){
        if(myProfileSelected) {
            if (!checkStoragePermission()) {
                requestStoragePermission();
            } else {
                pickProfileIMage();
            }
        }
    }

    private void requestStoragePermission() {
        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
    }

    private boolean checkStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED;
    }

    private void pickProfileIMage() {
        CropImage.activity()
                .start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                try{
                    InputStream stream=getContentResolver().openInputStream(resultUri);
                    Bitmap bitmap=BitmapFactory.decodeStream(stream);
                    displayImage(bitmap);
                }catch (Exception e){
                    Toast.makeText(MyProfile.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void displayImage(Bitmap bitmap) {
        if(profileImageSelection){
            AppClient.getInstance().getLoggedUser().setProfilePhoto(bitmap);
            profileImageView.setImageBitmap(bitmap);
            AppClient.getInstance().writeUserProfileImage(AppClient.getInstance().getLoggedUser().getUserID(),bitmap);
        }else{
            AppClient.getInstance().getLoggedUser().setBackgroundPhoto(bitmap);
            backgroundImageView.setImageBitmap(bitmap);
            AppClient.getInstance().writeUserBackgroundImage(AppClient.getInstance().getLoggedUser().getUserID(),bitmap);
        }
    }


    @Override
    public void onBackPressed() {
       // super.onBackPressed();
        this.finish();
    }
}