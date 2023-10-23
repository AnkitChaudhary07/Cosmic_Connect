package com.example.cosmicinsights;

import android.content.Context;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class GeoSearchApi {
    private Context context;
    private RequestQueue requestQueue;

    public GeoSearchApi(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public void fetchData(String apiUrl, TextView lattitude, TextView longitude, TextView timeZone) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, apiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);

                            if (jsonObject.has("response")) {
                                JSONObject responseData = jsonObject.getJSONArray("response").getJSONObject(0);
                                String latitudeString = responseData.getJSONArray("coordinates").getString(0);
                                String longitudeString = responseData.getJSONArray("coordinates").getString(1);
                                String tz = responseData.getString("tz");

                                // Convert latitude and longitude strings to doubles
                                double lat = Double.parseDouble(latitudeString);
                                double lon = Double.parseDouble(longitudeString);

                                // Format latitude and longitude with 2 decimal places
                                String formattedLatitude = String.format("%.2f", lat);
                                String formattedLongitude = String.format("%.2f", lon);

                                // Set latitude and longitude in TextViews
                                lattitude.setText(formattedLatitude);
                                longitude.setText(formattedLongitude);
                                timeZone.setText(tz);
                            } else {
                                handleApiError("Invalid City");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleApiError("Error...");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleApiError("Network Error: " + error.getMessage());
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void handleApiError(String errorMessage) {
        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
    }
}