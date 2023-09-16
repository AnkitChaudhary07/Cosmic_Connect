package com.example.cosmicinsights;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

TextView date;
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        date = view.findViewById(R.id.date);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);
        return view;
    }
}