package com.example.athlist.dialogs;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.interfaces.IChangePasswordCallback;


public class ChangePasswordDialog extends AppCompatDialogFragment {
    private IChangePasswordCallback listener;
    private EditText editTextPassword,editTextConfirmPassword,editTextOldPassword;
    private Context context;
    private Button save,cancel;

    public ChangePasswordDialog(IChangePasswordCallback listener,Context context) {
        this.listener=listener;
        this.context=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_change_password,null);


        builder.setView(view)
                .setTitle("Change Password");
        initialiseComponents(view);
        return builder.create();
    }



    public void initialiseComponents(View v) {
        editTextPassword=v.findViewById(R.id.dialog_change_password_editTextPassword);
        editTextConfirmPassword=v.findViewById(R.id.dialog_change_password_editTextConfirmPassword);
        editTextOldPassword=v.findViewById(R.id.dialog_change_password_editTextOldPassword);
        save=v.findViewById(R.id.dialog_change_password_save_button);
        cancel=v.findViewById(R.id.dialog_change_password_cancel_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPasswords();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChangePasswordDialog.this.dismiss();
            }
        });

    }

    private void checkPasswords() {
        String passwordText = editTextPassword.getText().toString();
        String confirmPasswordText = editTextConfirmPassword.getText().toString();
        String oldPasswordText = editTextOldPassword.getText().toString();
        if(!oldPasswordText.isEmpty())
        {
            if(validatePasswords(passwordText,confirmPasswordText)) {
                AppClient.getInstance().changePassword(passwordText,oldPasswordText,listener);
                this.dismiss();
            }
        }else{
            editTextOldPassword.setError("Field can not be empty");
        }
    }

    private boolean validatePasswords(String passwordText, String confirmPasswordText) {
        if(passwordText.equals(confirmPasswordText) && passwordText.length()>=6) return true;
        editTextPassword.setText("");
        editTextConfirmPassword.setText("");
        editTextPassword.setError("Passwords do not match!");
        editTextConfirmPassword.setError("Passwords do not match!");
        if(passwordText.length()<6){
            Toast.makeText(context,"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
        }
        return false;
    }
}
