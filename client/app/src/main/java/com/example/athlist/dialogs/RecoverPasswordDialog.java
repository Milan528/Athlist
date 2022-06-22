package com.example.athlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.athlist.R;
import com.example.athlist.clients.AppClient;
import com.example.athlist.interfaces.IRecoverPasswordCallback;

public class RecoverPasswordDialog extends AppCompatDialogFragment {

    private IRecoverPasswordCallback listener;
    private EditText editTextRecoverEmail;
    private Context context;
    private Button recover,cancel;

    public RecoverPasswordDialog(Context context, IRecoverPasswordCallback callback) {
        this.listener=callback;
        this.context=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_recover_password,null);


        builder.setView(view)
        .setTitle("Enter email address");
        initialiseComponents(view);
        return builder.create();
    }



    public void initialiseComponents(View v) {
        editTextRecoverEmail=v.findViewById(R.id.dialog_change_password_using_email_recoverEmail_editText);
        recover=v.findViewById(R.id.dialog_change_password_using_email_recover_button);
        cancel=v.findViewById(R.id.dialog_change_password_using_email_cancel_button);

        recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recoverPassword();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecoverPasswordDialog.this.dismiss();
            }
        });

    }

    private void recoverPassword() {
        String emailRecoverText = editTextRecoverEmail.getText().toString().trim();
        if(!emailRecoverText.isEmpty()) {
            AppClient.getInstance().recoverPassword(emailRecoverText,listener);
            this.dismiss();
        }else{
            editTextRecoverEmail.setError("Field can not be empty");
        }
    }
}
