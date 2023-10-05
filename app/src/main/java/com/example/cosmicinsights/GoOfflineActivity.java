package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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
import java.util.ArrayList;
import java.util.List;

public class GoOfflineActivity extends AppCompatActivity {

    ImageView back_btn;
    TextView toolbar_text;
    ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> dataList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_go_offline);

        back_btn = findViewById(R.id.back_btn);
        toolbar_text = findViewById(R.id.toolbar_text);
        listView = findViewById(R.id.listView);

        toolbar_text.setText("Go Offline");

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        listView.setAdapter(adapter);

        //Show data in ListView...
        new ApiRequestTask().execute();

    }

    public class ApiRequestTask extends AsyncTask<Void, Void, JSONObject> {
        private final String apiEndPoint = "basic_panchang";
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
                    dataList.add("Day: " + response.getJSONObject("data").getString("day"));
                    dataList.add("Tithi: " + response.getJSONObject("data").getString("tithi"));
                    dataList.add("Yog: " + response.getJSONObject("data").getString("yog"));
                    dataList.add("Nakshatra: " + response.getJSONObject("data").getString("nakshatra"));
                    dataList.add("Karan: " + response.getJSONObject("data").getString("karan"));
                    dataList.add("Sunrise: " + response.getJSONObject("data").getString("sunrise"));
                    dataList.add("Sunset: " + response.getJSONObject("data").getString("sunset"));
                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(GoOfflineActivity.this, "Error parsing JSON", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(GoOfflineActivity.this, "Error...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
