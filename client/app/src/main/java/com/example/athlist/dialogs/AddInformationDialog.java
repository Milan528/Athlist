package com.example.athlist.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.athlist.R;
import com.example.athlist.interfaces.IAddInformationListener;

public class AddInformationDialog extends AppCompatDialogFragment {
    private EditText informationEditText,titleEditText;
    private IAddInformationListener addInformationListener;

    public AddInformationDialog(IAddInformationListener addInformationListener) {
        this.addInformationListener = addInformationListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.dialog_add_information,null);


        builder.setView(view);
        builder.setTitle("Tell us about yourself");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String title=titleEditText.getText().toString();
                String information=informationEditText.getText().toString();
                addInformationListener.saveChanges(title,information);

            }
        });
        informationEditText=view.findViewById(R.id.dialog_add_information_information_editText);
        titleEditText=view.findViewById(R.id.dialog_add_information_title_editText);
    return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
