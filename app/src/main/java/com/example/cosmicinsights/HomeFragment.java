package com.example.cosmicinsights;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    GeoSearchApi geoSearchApi;
    Hora horaMuhurta;
    ChoghadiyaMuhurta choghadiyaMuhurta;
    MonthlyPanchang monthlyPanchang;

    Retrogrades retrogrades_obj;
    String selectedLanguage = "en";
    private final String API_KEY = "9100d2f1-d628-5866-bbfe-13507ea0bf38";
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
    private final String time = sdfTime.format(new Date());
    SimpleDateFormat sdfDate2 = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
    private final String date2 = sdfDate2.format(new Date());

    private Spinner languageSpinner;
    ScrollView scrollView2;
    EditText city_name;
    ImageView search_location;
    ProgressBar progressBar;
    TextView date, nakshatra, hora, choghadiya, monthly_panchang, retrogrades,lattitude, longitude, timeZone;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setTitle("Please enter your city...");
        alertDialog.setIcon(R.drawable.search);

        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        scrollView2 = view.findViewById(R.id.scrollView2);
        date = view.findViewById(R.id.date);
        search_location = view.findViewById(R.id.search_location);
        city_name = view.findViewById(R.id.city_name);
        lattitude = view.findViewById(R.id.lattitude);
        longitude = view.findViewById(R.id.longitude);
        timeZone = view.findViewById(R.id.timeZone);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        progressBar = view.findViewById(R.id.progressBar);

        nakshatra = view.findViewById(R.id.nakshatra);
        hora = view.findViewById(R.id.hora);
        choghadiya = view.findViewById(R.id.choghadiya);
        monthly_panchang = view.findViewById(R.id.monthly_panchang);
        retrogrades = view.findViewById(R.id.retrogrades);

        // Disable or hide the UI elements you want to show after entering a city
        scrollView2.setVisibility(view.GONE);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);

        //Time
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        final String time = sdfTime.format(new Date());

        //Year
        SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
        final String year = sdfYear.format(new Date());

        //Language Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        selectedLanguage = languageSpinner.getSelectedItem().toString();

        String str = city_name.getText().toString().trim();
        if (str.isEmpty()) {
            // Show an alert to enter a city
            alertDialog.show();
        }
        search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_name.clearFocus();
                String city = city_name.getText().toString().trim();
                    // Initialize and execute functions/classes here
                    geoSearchApi = new GeoSearchApi(requireContext());
                    //String apiGeoSearch = "https://api.vedicastroapi.com/v3-json/utilities/geo-search?city=delhi&api_key=" + API_KEY;
                    String apiGeoSearch = "https://api.vedicastroapi.com/v3-json/utilities/geo-search?city=" + city + "&api_key=" + API_KEY;
                    geoSearchApi.fetchData(apiGeoSearch, lattitude, longitude, timeZone);

                    final String lat = lattitude.getText().toString();
                    final String lon = longitude.getText().toString();
                    final String tzone = timeZone.getText().toString();

                    // For Nakshatra
                    ApiRequestTask apiRequestTask = new ApiRequestTask();
                    apiRequestTask.execute();

                    // Initialize and execute HoraApiRequestTask
                    horaMuhurta = new Hora((requireContext()));
                    String apiHora = "https://api.vedicastroapi.com/v3-json/panchang/hora-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=5.5&lat=11.22&lon=77.00&time=05:20&lang=en";
                    horaMuhurta.fetchData(apiHora, hora);

                    // Initialize and execute ChoghadiyaApiRequestTask
                    choghadiyaMuhurta = new ChoghadiyaMuhurta(requireContext());
                    String apiChoghadiyaMuhurta = "https://api.vedicastroapi.com/v3-json/panchang/choghadiya-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=5.5&lat=11.22&lon=77.00&time=05:20&lang=en";
                    choghadiyaMuhurta.fetchData(apiChoghadiyaMuhurta, choghadiya);

                    // Initialize MonthlyPanchang and set the click listener
                    monthlyPanchang = new MonthlyPanchang(requireContext());
                    //Initialize Monthly Panchang
                    monthly_panchang.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String apiMon_Pan = "https://api.vedicastroapi.com/v3-json/panchang/monthly-panchang?api_key=" + API_KEY + "&date=" + date2 + "&tz=" + tzone + "&lat=" + lat + "&lon=" + lon + "&time=" + time + "&lang=en";
                            monthlyPanchang.fetchData(apiMon_Pan, monthly_panchang);
                        }
                    });

                    String planet = horaMuhurta.getPlanet();
                    // Initialize Retrogrades and set the click listener
                    retrogrades_obj = new Retrogrades(requireContext());
                    //Initialize Retrogrades
                    retrogrades.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String apiRetrogrades = "https://api.vedicastroapi.com/v3-json/panchang/retrogrades?api_key=" + API_KEY +  "&year=" + year + "&planet=Saturn&lang=en";
                            retrogrades_obj.fetchRetrogradesData(apiRetrogrades, retrogrades);
                        }
                    });

                    // Show the UI elements after successful retrieval
                    scrollView2.setVisibility(View.VISIBLE);
            }
        });
        lattitude.setText(str);
        longitude.setText(str);
        timeZone.setText(str);

        retrogrades.setText(str);


        return view;
    }

    public class ApiRequestTask extends AsyncTask<Void, Void, JSONObject> {
        private final String language = "en";
        private final String lat = lattitude.getText().toString();
        private final String lon = longitude.getText().toString();
        private final String tzone = timeZone.getText().toString();
        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                String urlString = "https://api.vedicastroapi.com/v3-json/panchang/panchang?api_key=" + API_KEY + "&date=" + date2 + "&tz=" + tzone + "&lat=" + lat + "&lon=" + lon + "&time=" + time + "&lang=en";

                URL url = new URL(urlString);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Accept-Language", language);

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }
                    reader.close();
                    return new JSONObject(response.toString());
                } else {
                    Toast.makeText(requireContext(), "Error...", Toast.LENGTH_SHORT).show();
                }

                connection.disconnect();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject response) {
            if (response != null) {
                try {
                    JSONObject responseData = response.getJSONObject("response");

                    // Extract and add relevant information to the dataList
                    String allData =
                            "Nakshatra:- \n\n" +
                            "Name: " + responseData.getJSONObject("nakshatra").getString("name") + "\n" +
                            "Lord: " + responseData.getJSONObject("nakshatra").getString("lord") + "\n" +
                            "Diety: " + responseData.getJSONObject("nakshatra").getString("diety");


                    nakshatra.setText(allData);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(requireActivity(), "Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}