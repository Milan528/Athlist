package com.example.athlist.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;

import androidx.appcompat.app.AppCompatDialog;

import com.example.athlist.R;

public class LoadingDialog  {
    Activity activity;
    AlertDialog dialog;

    public LoadingDialog(Activity activity) {
        this.activity = activity;
        createDialog();
    }

    private void createDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(activity);
        LayoutInflater inflater=activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.progress_dialog,null));
        builder.setCancelable(true);

        dialog=builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
