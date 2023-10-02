package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class TransitNewsActivity extends AppCompatActivity {

    ImageView back_btn;
    TextView toolbar_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_news);

        back_btn = findViewById(R.id.back_btn);
        toolbar_text = findViewById(R.id.toolbar_text);

        toolbar_text.setText("Transit News");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}