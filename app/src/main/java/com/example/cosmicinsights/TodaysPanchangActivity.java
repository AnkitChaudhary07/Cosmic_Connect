package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TodaysPanchangActivity extends AppCompatActivity {

    TextView date;
    ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todays_panchang);

        date = findViewById(R.id.date);
        listView = findViewById(R.id.listView);

        // Update the text of the TextView with the current date and time
        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, EE, yyyy", Locale.getDefault());
        String currentDateAndTime = sdf.format(new Date());
        date.setText(currentDateAndTime);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        //Show data in ListView...
        new ApiRequestTask().execute();

    }

        public class ApiRequestTask extends AsyncTask<Void, Void, JSONObject> {
            private final String apiEndPoint = "advanced_panchang";
            private final String userId = "666";
            private final String apiKey = "69908a3ae9144cf4918985a785114a46";
            private final String language = "en";
            private final int day = 15;
            private final int month = 9;
            private final int year = 1994;
            private final int hour = 12;
            private final int min = 30;
            private final double lat = 28.6139;
            private final double lon = 77.1025;
            private final double tzone = 5.5;

            @Override
            protected JSONObject doInBackground(Void... voids) {
                try {
                    URL url = new URL("https://json.apireports.com/v1/" + apiEndPoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "Basic " + Base64.encodeToString((userId + ":" + apiKey).getBytes(), Base64.NO_WRAP));
                    connection.setRequestProperty("Accept-Language", language);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setDoOutput(true);

                    JSONObject data = new JSONObject();
                    data.put("day", day);
                    data.put("month", month);
                    data.put("year", year);
                    data.put("hour", hour);
                    data.put("min", min);
                    data.put("lat", lat);
                    data.put("lon", lon);
                    data.put("tzone", tzone);

                    DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream());
                    outputStream.writeBytes(data.toString());
                    outputStream.flush();
                    outputStream.close();

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
                        Log.e("ApiRequest", "HTTP error code: " + responseCode);
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
                        dataList.clear();
                        dataList.add("Date: " + response.getJSONObject("data").getString("date"));
                        dataList.add("Month: " + response.getJSONObject("data").getString("month"));
                        dataList.add("Year: " + response.getJSONObject("data").getString("year"));
                        dataList.add("Day: " + response.getJSONObject("data").getString("day"));
                        dataList.add("Sunrise: " + response.getJSONObject("data").getJSONObject("sun").getString("sunrise"));
                        dataList.add("Sunset: " + response.getJSONObject("data").getJSONObject("sun").getString("sunset"));
                        dataList.add("Ayana Number: " + response.getJSONObject("data").getJSONObject("ayana").getString("ayana_number"));
                        dataList.add("Ayana Name: " + response.getJSONObject("data").getJSONObject("ayana").getString("ayana_name"));
                        dataList.add("Meaning: " + response.getJSONObject("data").getJSONObject("ayana").getString("meaning"));



                        // Notify the adapter that the data has changed
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(TodaysPanchangActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(TodaysPanchangActivity.this, "Error...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
