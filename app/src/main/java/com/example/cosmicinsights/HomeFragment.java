package com.example.cosmicinsights;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {



    SearchView search;
    ImageView transit_remedies_image, transit_news_image, tips_tricks_image, today_panchang_image, today_glance_image, go_offline_image, featured_tools_image;
    ProgressBar progressBar;
    TextView date, textView;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        date = view.findViewById(R.id.date);
        today_panchang_image = view.findViewById(R.id.today_panchang_image);
        today_glance_image = view.findViewById(R.id.today_glance_image);
        search = view.findViewById(R.id.search);
        transit_remedies_image = view.findViewById(R.id.transit_remedies_image);
        transit_news_image = view.findViewById(R.id.transit_news_image);
        tips_tricks_image = view.findViewById(R.id.tips_tricks_image);
        go_offline_image = view.findViewById(R.id.go_offline_image);
        transit_remedies_image = view.findViewById(R.id.transit_remedies_image);
        featured_tools_image = view.findViewById(R.id.featured_tools_image);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);


        //Clicking on Transit Remedies...
        transit_remedies_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransitRemediesMain.class);
                startActivity(intent);
            }
        });

        //Clicking on Transit news...
        transit_news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransitNewsActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on Tips & Tricks...
        tips_tricks_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TipsAndTricksActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on Today At a Glance...
        today_glance_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodayAtGlanceActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on Go Offline
        go_offline_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), GoOfflineActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on Today's Panchang
        today_panchang_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TodaysPanchangActivity.class);
                startActivity(intent);
            }
        });

        //Clicking on Featured Tools...
        featured_tools_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FeaturedToolsActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }



    private Astrology_report parseJsonResponse(JSONObject response) throws JSONException {
        Astrology_report astrologyReport = new Astrology_report();

        // Parse the JSON data and populate the astrologyReport object
        // Replace these lines with the actual parsing logic based on your JSON structure
        // Example: astrologyReport.setAuth(response.getJSONObject("auth"));

        // For nested objects or arrays, you will need to implement the parsing logic accordingly

        return astrologyReport;
    }
}