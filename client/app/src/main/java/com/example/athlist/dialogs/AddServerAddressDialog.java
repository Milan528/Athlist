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
import com.example.athlist.interfaces.ISaveServerAddressCallback;

public class AddServerAddressDialog extends AppCompatDialogFragment {
    //private ISaveServerAddressCallback listener;
    private EditText editTextServerAddress;
    private Context context;
    private Button save,cancel;

    public AddServerAddressDialog(Context context) {
        //this.listener=listener;
        this.context=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_server_address,null);


        builder.setView(view);
                //.setTitle("Enter server  Password");
        initialiseComponents(view);
        return builder.create();
    }



    public void initialiseComponents(View v) {
        editTextServerAddress=v.findViewById(R.id.dialog_server_address_editText);
        save=v.findViewById(R.id.dialog_saveAddress_save_button);
        cancel=v.findViewById(R.id.dialog_saveAddress_cancel_button);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               saveAddress();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddServerAddressDialog.this.dismiss();
            }
        });

    }

    private void saveAddress() {
        String serverAddress = editTextServerAddress.getText().toString();
        if(!serverAddress.isEmpty()) {
           AppClient.getInstance().createRetrofitClient(serverAddress);
            Toast.makeText(context, "Address added!", Toast.LENGTH_SHORT).show();
           this.dismiss();
        }else{
            editTextServerAddress.setError("Field can not be empty");
        }
    }
}
