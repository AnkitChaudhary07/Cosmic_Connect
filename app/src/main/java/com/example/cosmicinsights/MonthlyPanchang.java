package com.example.cosmicinsights;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MonthlyPanchang {
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private Context context;

    public MonthlyPanchang(Context context) {
        this.context = context;
    }

    public void fetchData(String url, TextView textView) {
        String time = sdfTime.format(new Date());
        SimpleDateFormat sdfDay = new SimpleDateFormat("EEEE", Locale.getDefault());
        String currentDay = sdfDay.format(new Date());

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
                                String data = "Day: " + currentDay + "\n" +
                                        "Tithi: " + tithiName + "\n" +
                                        "Nakshatra: " + nakshatraName;
                                textView.setText(data);
                            } else {
                                Toast.makeText(context, "No data available for the current day.", Toast.LENGTH_SHORT).show();
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
