package com.example.athlist.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.athlist.R;
import com.example.athlist.adapters.ProfileInformationAdapter;
import com.example.athlist.clients.AppClient;
import com.example.athlist.dialogs.AddInformationDialog;
import com.example.athlist.interfaces.IAddInformationClickedListener;
import com.example.athlist.interfaces.IAddInformationListener;
import com.example.athlist.models.ProfileInformationElements;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MyProfileInformationFragment extends Fragment implements IAddInformationListener,IAddInformationClickedListener {
    RecyclerView informationRecyclerView;
    ProfileInformationAdapter profileInformationAdapter;
    List<ProfileInformationElements> displayedElements;
    AddInformationDialog addInformationDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_profile_information,container,false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        informationRecyclerView=view.findViewById(R.id.profile_page_information_recyclerView);
        createDisplayElements();
        addInformationDialog=new AddInformationDialog(this);
        refreshInformationAdapter(displayedElements);

    }



    public void createInformationAdapterView(List<ProfileInformationElements> displayedElements) {
        if(profileInformationAdapter==null) {
            profileInformationAdapter = new ProfileInformationAdapter(displayedElements, this);
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
            informationRecyclerView.setLayoutManager(layoutManager);
            informationRecyclerView.setAdapter(profileInformationAdapter);
        }
    }

    public void refreshInformationAdapter(List<ProfileInformationElements> displayedElements){
        if(profileInformationAdapter==null)
            createInformationAdapterView(displayedElements);
        else{
            RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
            informationRecyclerView.setLayoutManager(layoutManager);
            informationRecyclerView.setAdapter(profileInformationAdapter);
        }
    }

    public void addElement(ProfileInformationElements element){
        profileInformationAdapter.addElement(element);
    }

    private void createDisplayElements() {
        List<ProfileInformationElements> elements;
        if(AppClient.getInstance().getLoggedUser().getAdditionalInformation()!=null){
         elements=new ArrayList<>(AppClient.getInstance().getLoggedUser().getAdditionalInformation());
        for (ProfileInformationElements element:elements) {
            element.setImageID(R.drawable.ic_info);
        }
        }else {
            elements= new ArrayList<>();
        }
        ProfileInformationElements emailElement=new ProfileInformationElements("Email",AppClient.getInstance().getLoggedUser().getEmail(),R.drawable.ic_email_2);
        ProfileInformationElements phoneElement=new ProfileInformationElements("Phone",AppClient.getInstance().getLoggedUser().getPhoneNumber(),R.drawable.ic_baseline_phone_android_24);
        elements.add(0,phoneElement);
        elements.add(0,emailElement);
        displayedElements=elements;
    }

    private void createInformationBox(String title, String information) {
        ProfileInformationElements element=new ProfileInformationElements(title,information,R.drawable.ic_info);
        //displayedElements.add(element); //ista referenca na listu pa se 2 puta doda
        AppClient.getInstance().getLoggedUser().getAdditionalInformation().add(element);
        AppClient.getInstance().updateLoggedUserProfileInformation();
        addElement(element);
    }

    @Override
    public void saveChanges(String title, String information) {
        createInformationBox(title,information);
    }

    @Override
    public void onAddInformationClicked() {
        openAddInformationDialog();
    }

    private void openAddInformationDialog(){
        addInformationDialog.show(requireActivity().getSupportFragmentManager(),"information Dialog");
    }


}
