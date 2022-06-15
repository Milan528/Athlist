package com.example.athlist.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.athlist.R;

import com.example.athlist.clients.AppClient;
import com.example.athlist.dialogs.ChangePasswordDialog;
import com.example.athlist.enums.StravaConnectionStatus;
import com.example.athlist.fragments.StravaConnectionStatusFragment;
import com.example.athlist.interfaces.IChangePasswordCallback;
import com.makeramen.roundedimageview.RoundedImageView;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.InputStream;



public class MyProfile extends AppCompatActivity implements View.OnClickListener {

    TextView usernameTextView,profilesCountTextView,activitiesCountTextView,emailTextView;
    ImageView backgroundImageView;
    RoundedImageView profileImageView;
    Button btnViewActivities,btnChangePassword;
    boolean profileImageSelection;
    IChangePasswordCallback changePasswordListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        initializeComponents();
        setUpPage();

    }

    private void setUpPage() {
        emailTextView.setText(AppClient.getInstance().getLoggedUser().getEmail());
        usernameTextView.setText(AppClient.getInstance().getLoggedUser().getUsername());
        if(AppClient.getInstance().getLoggedUser().getAthleteProfiles()!=null && AppClient.getInstance().getLoggedUser().getAthleteProfiles().size()>0){
            profilesCountTextView.setText(String.valueOf(AppClient.getInstance().getLoggedUser().getAthleteProfiles().size()));
            activitiesCountTextView.setText(String.valueOf(AppClient.getInstance().getLoggedUser().getAllActivities().size()));
        }
    }


    private void initializeComponents() {

        usernameTextView=findViewById(R.id.profile_page_username_textView);
        profilesCountTextView=findViewById(R.id.profile_page_profilesCount_textView);
        activitiesCountTextView=findViewById(R.id.profile_page_activitiesCount_textView);
        backgroundImageView=findViewById(R.id.profile_page_background_imageView);
        profileImageView=findViewById(R.id.profile_page_profile_imageView);
        btnViewActivities=findViewById(R.id.profile_page_viewActivities_button);
        btnChangePassword=findViewById(R.id.profile_page_changePassword_button);
        emailTextView=findViewById(R.id.profile_page_email_textView);

        backgroundImageView.setImageBitmap(AppClient.getInstance().getLoggedUser().getBackgroundPhoto());
        profileImageView.setImageBitmap(AppClient.getInstance().getLoggedUser().getProfilePhoto());
        changePasswordListener=new ChangePasswordListener();

        profileImageView.setOnClickListener(this);
        backgroundImageView.setOnClickListener(this);
        btnViewActivities.setOnClickListener(this);
        btnChangePassword.setOnClickListener(this);
    }




    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.profile_page_changePassword_button){
           changePassword();
        }else if(clickedId==R.id.profile_page_viewActivities_button){
            viewActivities();
        }else if(clickedId==R.id.profile_page_background_imageView){
            profileImageSelection=false;
            selectProfileImage();
        }else if(clickedId==R.id.profile_page_profile_imageView){
                profileImageSelection = true;
                selectProfileImage();
        }

    }

    private void viewActivities() {
        Intent intent = new Intent(this, StravaActivitiesActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void changePassword() {
        ChangePasswordDialog dialog=new ChangePasswordDialog(changePasswordListener,this);
        dialog.show(getSupportFragmentManager(),"Change Password");
    }






    private void selectProfileImage(){
            if (!checkStoragePermission()) {
                requestStoragePermission();
            } else {
                pickProfileIMage();
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
        super.onBackPressed();
        this.finish();
    }

    private class ChangePasswordListener implements IChangePasswordCallback{

        @Override
        public void onPasswordChangeSuccess(String message) {
            Toast.makeText(MyProfile.this,message,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onPasswordChangeFailed(String message) {
            Toast.makeText(MyProfile.this,message,Toast.LENGTH_LONG).show();
        }
    }
}