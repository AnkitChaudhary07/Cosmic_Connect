package com.example.cosmicinsights;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment {

    LocationHelper locationHelper;
    GeoSearchApi geoSearchApi;

    Retrogrades retrogrades_obj;
    String selectedLanguage = "en";
    String planet;

    //Used in location search bar...
    int flag = 0;
    String lat = "";
    String lon = "";
    String tzone = "5.5";
    private final String API_KEY = "9100d2f1-d628-5866-bbfe-13507ea0bf38";
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
    private final String time = sdfTime.format(new Date());
    SimpleDateFormat sdfDate2 = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
    private final String date2 = sdfDate2.format(new Date());

    private Spinner languageSpinner;
    ScrollView scrollView2;
    EditText city_name;
    ImageView live_location, search_location, calendar;
    ProgressBar progressBar, progressBar2;
    TextView date, nakshatra, hora, choghadiya, monthly_panchang, retrogrades,lattitude, longitude, timeZone;

    public HomeFragment() {
        // Required empty public constructor
    }
    //Year
    SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    final String year = sdfYear.format(new Date());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        AlertDialog alertDialog = new AlertDialog.Builder(requireContext()).create();
        alertDialog.setIcon(R.drawable.live_location);
        alertDialog.setTitle("Live Location");
        alertDialog.setMessage("Please tap this icon \nto see live location.");
        alertDialog.setButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
        //Paint the search icon black...

        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        scrollView2 = view.findViewById(R.id.scrollView2);
        date = view.findViewById(R.id.date);
        calendar = view.findViewById(R.id.calendar);
        live_location = view.findViewById(R.id.live_location);
        search_location = view.findViewById(R.id.search_location);
        city_name = view.findViewById(R.id.city_name);
        lattitude = view.findViewById(R.id.lattitude);
        longitude = view.findViewById(R.id.longitude);
        timeZone = view.findViewById(R.id.timeZone);
        languageSpinner = view.findViewById(R.id.languageSpinner);
        progressBar = view.findViewById(R.id.progressBar);
        progressBar2 = view.findViewById(R.id.progressBar2);

        nakshatra = view.findViewById(R.id.nakshatra);
        hora = view.findViewById(R.id.hora);
        choghadiya = view.findViewById(R.id.choghadiya);
        monthly_panchang = view.findViewById(R.id.monthly_panchang);
        retrogrades = view.findViewById(R.id.retrogrades);

        // Disable or hide the UI elements you want to show after entering a city
        scrollView2.setVisibility(view.GONE);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);

        //Time
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        final String time = sdfTime.format(new Date());

        //When Clicked on Calendar Icon...
        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showCalendar();
            }
        });

        //Language Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(),
                R.array.languages, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);
        selectedLanguage = languageSpinner.getSelectedItem().toString();

        //Live Location...
        live_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Live Location...
                locationHelper = new LocationHelper(getActivity(), lattitude, longitude, progressBar, city_name);
                locationHelper.checkAndRequestLocationPermission();

                lattitude.setText(" ");

                // Check if the city name has been successfully retrieved
                if (!lattitude.getText().toString().isEmpty()) {
                    // City name retrieved, so makeScrollViewVisible
                    makeScrollViewVisible();
                }
            }
        });

        search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                flag = 1;
                city_name.clearFocus();
                String city = city_name.getText().toString().trim();
                    // Initialize and execute functions/classes here
                    geoSearchApi = new GeoSearchApi(requireContext());
                    //String apiGeoSearch = "https://api.vedicastroapi.com/v3-json/utilities/geo-search?city=delhi&api_key=" + API_KEY;
                    String apiGeoSearch = "https://api.vedicastroapi.com/v3-json/utilities/geo-search?city=" + city + "&api_key=" + API_KEY;
                    geoSearchApi.setProgressBar(progressBar);
                    geoSearchApi.fetchData(apiGeoSearch, lattitude, longitude, timeZone);

                // Introduce a 3-second delay using a Handler
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Call the function to make scrollView2 visible after the delay
//                        lat = lattitude.getText().toString();
//                        lon = longitude.getText().toString();
//                        tzone = timeZone.getText().toString();
                        makeScrollViewVisible();
                    }
                }, 3000);
            }
        });

//        nakshatra.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // Ensure that the location data is available before triggering the API call
//                if (lat.isEmpty() || lon.isEmpty() || tzone.isEmpty()) {
//                    // Handle the case where location data is not available
//                    Toast.makeText(requireContext(), "Please select a city first.", Toast.LENGTH_SHORT).show();
//                } else {
//                    progressBar.setVisibility(View.VISIBLE);
//                    // Trigger the API request
//                    ApiRequestTask apiRequestTask = new ApiRequestTask();
//                    apiRequestTask.execute();
//                }
//            }
//        });


        return view;
    }

    //Show Calendar...
    private void showCalendar() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
                calendar.set(selectedYear, selectedMonth, selectedDay);
                String selectedDate =  selectedDay + " " + monthFormat.format(calendar.getTime()) + " " + selectedYear;
                date.setText(selectedDate);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    //Show Monthly Panchang Calendar...
    private String monthlyPanchangDate = "";
    private void showMonthlyPanchangCalendar() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                selectedMonth = selectedMonth + 1;
                monthlyPanchangDate =  selectedDay + "/" + selectedMonth + "/" + selectedYear;

                // Show the ProgressBar
                progressBar2.setVisibility(View.VISIBLE);

                String apiMon_Pan = "https://api.vedicastroapi.com/v3-json/panchang/monthly-panchang?api_key=" + API_KEY + "&date=" + monthlyPanchangDate + "&tz=" + tzone + "&lat=" + lat + "&lon=" + lon + "&time=" + time + "&lang=en";

                // Create an instance of MonthlyPanchang and pass the selected date
                MonthlyPanchang monthlyPanchangInstance = new MonthlyPanchang(requireContext(), monthlyPanchangDate);
                monthlyPanchangInstance.setProgressBar(progressBar2);
                monthlyPanchangInstance.fetchData(apiMon_Pan, monthly_panchang);
            }
        }, year, month, day);
        datePickerDialog.show();
    }


    private void makeScrollViewVisible() {

        // For Nakshatra
        nakshatra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the ProgressBar
                progressBar2.setVisibility(View.VISIBLE);
                lat = lattitude.getText().toString();
                lon = longitude.getText().toString();
                tzone = timeZone.getText().toString();
                ApiRequestTask apiRequestTask = new ApiRequestTask();
                apiRequestTask.execute();
            }
        });

        // For Hora
        hora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar2.setVisibility(View.VISIBLE);
                lat = lattitude.getText().toString();
                lon = longitude.getText().toString();
                tzone = timeZone.getText().toString();
               // String apiHora = "https://api.vedicastroapi.com/v3-json/panchang/hora-muhurta?api_key=9100d2f1-d628-5866-bbfe-13507ea0bf38&date=11/03/1994&tz=5.5&lat=11.22&lon=77.00&time=05:20&lang=en";
                String apiHora = "https://api.vedicastroapi.com/v3-json/panchang/hora-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=" + tzone + "&lat=" + lat + "&lon=" + lon + "&time=" + time + "&lang=en";
                Hora horaMuhurta = new Hora(requireContext(), apiHora);
                horaMuhurta.setProgressBar(progressBar2);
                horaMuhurta.fetchDataAndDisplay(hora);
                planet = horaMuhurta.getPlanet();
            }
        });

        // For Choghadiya

        choghadiya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar2.setVisibility(View.VISIBLE);
                lat = lattitude.getText().toString();
                lon = longitude.getText().toString();
                tzone = timeZone.getText().toString();

            //for testing...   //String apidemo = "https://api.vedicastroapi.com/v3-json/panchang/choghadiya-muhurta?api_key=9100d2f1-d628-5866-bbfe-13507ea0bf38&date=23/10/2023&tz=5.5&lat=30.26&lon=78.10&time=05:20&lang=en";
                String apiChoghadiyaMuhurta = "https://api.vedicastroapi.com/v3-json/panchang/choghadiya-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=" + tzone + "&lat=" + lat + "&lon=" + lon + "&time=" + time + "&lang=en";
                ChoghadiyaMuhurta choghadiyaMuhurta = new ChoghadiyaMuhurta(requireContext(), apiChoghadiyaMuhurta);
                choghadiyaMuhurta.setProgressBar(progressBar2);
                choghadiyaMuhurta.fetchDataAndDisplay(choghadiya);
            }
        });

        //For Monthly Panchang...
        monthly_panchang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonthlyPanchangCalendar();
            }
        });

        //For Retrogrades
        retrogrades_obj = new Retrogrades(requireContext());
        retrogrades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String apiRetrogrades = "https://api.vedicastroapi.com/v3-json/panchang/retrogrades?api_key=" + API_KEY +  "&year=" + year + "&planet=saturn" + "&lang=en";
                retrogrades_obj.fetchRetrogradesData(apiRetrogrades, retrogrades);
                progressBar.setVisibility(View.GONE);
            }
        });

        // Show the UI elements after successful retrieval
        scrollView2.setVisibility(View.VISIBLE);
    }


    public class ApiRequestTask extends AsyncTask<Void, Void, JSONObject> {
        private final String language = "en";

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
                    if (progressBar2 != null) {
                        progressBar2.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(requireActivity(), "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    if (progressBar2 != null) {
                        progressBar2.setVisibility(View.GONE);
                    }
                }
            } else {
                Toast.makeText(requireActivity(), "Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}