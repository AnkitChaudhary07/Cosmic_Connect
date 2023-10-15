package com.example.cosmicinsights;

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
import android.widget.SearchView;
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

    Hora horaMuhurta;
    ChoghadiyaMuhurta choghadiyaMuhurta;
    private final String API_KEY = "9100d2f1-d628-5866-bbfe-13507ea0bf38";
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
    private final String time = sdfTime.format(new Date());
    SimpleDateFormat sdfDate2 = new SimpleDateFormat("d/MM/yyyy", Locale.getDefault());
    private final String date2 = sdfDate2.format(new Date());
    EditText city_name;
    ImageView search_location;
    ProgressBar progressBar;
    TextView date, nakshatra, hora, choghadiya;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        date = view.findViewById(R.id.date);
        search_location = view.findViewById(R.id.search_location);
        city_name = view.findViewById(R.id.city_name);
        nakshatra = view.findViewById(R.id.nakshatra);
        hora = view.findViewById(R.id.hora);
        choghadiya = view.findViewById(R.id.choghadiya);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("d MMM yyyy hh:mm", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);

        //Time
        SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm", Locale.getDefault());
        final String time = sdfTime.format(new Date());

        //For Nakshatra
        ApiRequestTask apiRequestTask = new ApiRequestTask();
        apiRequestTask.execute();

        // Initialize and execute HoraApiRequestTask
        horaMuhurta = new Hora((requireContext()));
        String apiHora = "https://api.vedicastroapi.com/v3-json/panchang/hora-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=5.5&lat=11.22&lon=77.00&time=" + time + "&lang=en";
        horaMuhurta.fetchData(apiHora, hora);

        // Initialize and execute ChoghadiyaApiRequestTask
        choghadiyaMuhurta = new ChoghadiyaMuhurta(requireContext());
        String apiChoghadiyaMuhurta = "https://api.vedicastroapi.com/v3-json/panchang/choghadiya-muhurta?api_key=" + API_KEY + "&date=" + date2 + "&tz=5.5&lat=11.22&lon=77.00&time=" + time + "&lang=en";
        choghadiyaMuhurta.fetchData(apiChoghadiyaMuhurta, choghadiya);



        search_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = city_name.getText().toString();
                if (!city.isEmpty()) {
                    requestLatLongFromAPI(city);
                } else {
                    Toast.makeText(requireContext(), "Please enter a city name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void requestLatLongFromAPI(String city) {
        String apiUrl = "https://api.vedicastroapi.com/v3-json/utilities/geo-search?city=" + "dehradun" + "&api_key=" + API_KEY;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(apiUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();

                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response from the API and extract the latitude and longitude
                        InputStream inputStream = connection.getInputStream();
                        InputStreamReader reader = new InputStreamReader(inputStream);
                        BufferedReader bufferedReader = new BufferedReader(reader);
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            response.append(line);
                        }

                        // Parse the JSON response
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        JSONArray responseArray = jsonResponse.getJSONArray("response");

                        if (responseArray.length() > 0) {
                            JSONObject cityData = responseArray.getJSONObject(0);
                            JSONArray coordinates = cityData.getJSONArray("coordinates");
                            double latitude = Double.parseDouble(coordinates.getString(0));
                            double longitude = Double.parseDouble(coordinates.getString(1));
                            Toast.makeText(requireContext(), "success.....", Toast.LENGTH_SHORT).show();

                            // Do something with the latitude and longitude values, for example, display them
                            Log.d("LatLong", "Latitude: " + latitude + ", Longitude: " + longitude);
                        } else {
                            // Handle the case where no location data was found for the city
                            Toast.makeText(requireContext(), "No location data found for the city: \"" + city, Toast.LENGTH_SHORT).show();
                        }
                        // Do something with the latitude and longitude values, for example, display them
                    } else {
                        // Handle the error case
                        Toast.makeText(requireContext(), "Error occured", Toast.LENGTH_SHORT).show();
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public class ApiRequestTask extends AsyncTask<Void, Void, JSONObject> {
        private final String language = "en";
        private final double lat = 28.6139;
        private final double lon = 77.1025;
        private final double tzone = 5.5;

        @Override
        protected JSONObject doInBackground(Void... voids) {
            try {
                String urlString = "https://api.vedicastroapi.com/v3-json/panchang/panchang?api_key=" + API_KEY + "&date=" + date2 + "&tz=5.5&lat=11.2&lon=77.00&time=05:20&lang=en";

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