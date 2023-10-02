package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TodaysPanchangActivity extends AppCompatActivity {

    TextView date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_panchang);

        date = findViewById(R.id.date);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, EE, yyyy", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);


    }
}