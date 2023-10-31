package com.example.cosmicinsights;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonthlyPanchang {
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());

    private String monthlyPanchangDate;
    private String currentDay;
    private Context context;

    public MonthlyPanchang(Context context, String date) {
        this.context = context;
        this.monthlyPanchangDate = date;
        //this.monthlyPanchangDate = "29/10/2023";
    }

    private ProgressBar progressBar2;

    public void setProgressBar(ProgressBar progressBar2) {
        this.progressBar2 = progressBar2;
    }

    // Utility method to capitalize the first letter of each word
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
    }

    public void fetchData(String url, TextView textView) {
        String time = sdfTime.format(new Date());

        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        try {
            Date date = sdfInput.parse(monthlyPanchangDate);
            SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE", Locale.getDefault());
            assert date != null;
            currentDay = capitalize(sdfDay.format(date));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray responseDataArray = jsonResponse.getJSONArray("response");

                            // Find data for the current day
                            JSONObject currentDayData = null;
                            for (int i = 0; i < responseDataArray.length(); i++) {
                                JSONObject dayData = responseDataArray.getJSONObject(i);
                                JSONObject dayObject = dayData.getJSONObject("day");
                                String dayName = dayObject.getString("name");

                                // Compare day names case-insensitively
                                if (dayName.equalsIgnoreCase(currentDay)) {
                                    currentDayData = dayData;
                                    break;
                                }
                            }

                            if (currentDayData != null) {
                                // Get specific data for the current day
                                String tithiName = currentDayData.getJSONObject("tithi").getString("name");
                                String tithiStart = currentDayData.getJSONObject("tithi").getString("start");
                                String tithiEnd = currentDayData.getJSONObject("tithi").getString("end");

                                String nakshatraName = currentDayData.getJSONObject("nakshatra").getString("name");
                                String nakshatraStart = currentDayData.getJSONObject("nakshatra").getString("start");
                                String nakshatraEnd = currentDayData.getJSONObject("nakshatra").getString("end");

                                // Display the data in the provided TextView
                                String data = "Date: " + monthlyPanchangDate + "\n" +
                                        "Day: " + currentDay + "\n" +
                                        "Tithi: " + tithiName + "\n" +
                                        "Nakshatra: " + nakshatraName;
                                textView.setText(data);
                                if (progressBar2 != null) {
                                    progressBar2.setVisibility(View.GONE);
                                }
                            } else {
                                Toast.makeText(context, "No data available for the current day.", Toast.LENGTH_SHORT).show();
                                if (progressBar2 != null) {
                                    progressBar2.setVisibility(View.GONE);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "JSON Parsing Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
