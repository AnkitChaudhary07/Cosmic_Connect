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
    ImageView transit_remedies_image;
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

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);

        search = view.findViewById(R.id.search);
        transit_remedies_image = view.findViewById(R.id.transit_remedies_image);

        transit_remedies_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TransitRemediesMain.class);
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