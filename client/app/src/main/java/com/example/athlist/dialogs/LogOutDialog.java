package com.example.athlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.athlist.interfaces.ILogoutListener;


public class LogOutDialog extends AppCompatDialogFragment {
    private ILogoutListener listener;

    public LogOutDialog(ILogoutListener logoutListener) {
        listener=logoutListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

        builder.setTitle("Exit")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) { }
                }).setPositiveButton("Logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.onLogout();
            }
        });
        return builder.create();
    }
}
