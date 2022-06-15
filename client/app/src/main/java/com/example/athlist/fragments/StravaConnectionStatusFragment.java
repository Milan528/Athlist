package com.example.athlist.fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.athlist.R;
import com.example.athlist.activities.ConnectToStravaActivity;
import com.example.athlist.clients.AppClient;
import com.example.athlist.enums.StravaConnectionStatus;

public class StravaConnectionStatusFragment extends Fragment implements View.OnClickListener {
    TextView connectToStravaTextView,informationTextView;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_not_connected_to_strava,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        connectToStravaTextView=view.findViewById(R.id.profile_page_connectToStrava_textView);
        informationTextView=view.findViewById(R.id.profile_page_strava_connection_status_textView);
        connectToStravaTextView.setPaintFlags(connectToStravaTextView.getPaintFlags()| Paint.UNDERLINE_TEXT_FLAG);
        connectToStravaTextView.setOnClickListener(this);
        if(AppClient.getInstance().getLoggedUser().getConnectionStatus()== StravaConnectionStatus.NOT_CONNECTED){
            connectToStravaTextView.setText(R.string.connect_to_strava);
        }
    }

    @Override
    public void onClick(View view) {
        int clickedId=view.getId();
        if(clickedId==R.id.profile_page_connectToStrava_textView){
           openStravaConnectionPage();
        }
    }

    private void openStravaConnectionPage() {
        Intent intent = new Intent(getActivity(), ConnectToStravaActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
