package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TransitRemediesMain extends AppCompatActivity {
    ImageView saturn_aquarious, digital_asto_calender, important_days;
    TextView action_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_remedies_main);

        action_bar = findViewById(R.id.action_bar);
        saturn_aquarious = findViewById(R.id.saturn_aquarious);
        digital_asto_calender = findViewById(R.id.digital_asto_calender);
        important_days = findViewById(R.id.important_days);

        action_bar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(TransitRemediesMain.this, HomeFragment.class);
                startActivity(intent1);
            }
        });

        //Clicking on saturn_aquarious
        saturn_aquarious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransitRemediesMain.this, SaturnAquariusActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on digital_asto_calender
        digital_asto_calender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransitRemediesMain.this, DigitalAstrologyCalenderActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on important_days
        important_days.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TransitRemediesMain.this, ImportantDaysActivity.class);
                startActivity(intent);
            }
        });

    }
}