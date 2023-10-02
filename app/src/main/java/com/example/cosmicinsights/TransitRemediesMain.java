package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TransitRemediesMain extends AppCompatActivity {
    ImageView saturn_aquarious, digital_asto_calender, important_days, back_btn;
    TextView action_bar, toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_remedies_main);

        saturn_aquarious = findViewById(R.id.saturn_aquarious);
        digital_asto_calender = findViewById(R.id.digital_asto_calender);
        important_days = findViewById(R.id.important_days);
        back_btn = findViewById(R.id.back_btn);
        toolbar_text = findViewById(R.id.toolbar_text);

        // Set the text
        toolbar_text.setText("Transit Remedies");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
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