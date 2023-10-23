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

public class ChoghadiyaMuhurta {
    SimpleDateFormat sdfTime = new SimpleDateFormat("hh:mm a", Locale.getDefault());
    private Context context;

    public ChoghadiyaMuhurta(Context context) {
        this.context = context;
    }

    public void fetchData(String url, TextView textView) {
        String time = sdfTime.format(new Date());

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the current time to determine if it's daytime
                            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.getDefault());
                            Date currentTime = sdf.parse(time);

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject responseData = jsonObject.getJSONObject("response");

                            // Choose the appropriate data based on time
                            JSONArray data = responseData.getJSONArray("day");

                            // Initialize variables to store the closest muhurat and its time
                            String closestMuhurat = "";
                            Date closestStartTime = null;
                            Date closestEndTime = null;

                            // Loop through the data to find the matching time
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject item = data.getJSONObject(i);
                                String muhurat = item.getString("muhurat");
                                String start = item.getString("start");
                                String end = item.getString("end");
                                Date startTime = sdf.parse(start);
                                Date endTime = sdf.parse(end);

                                if (currentTime.after(startTime) && currentTime.before(endTime)) {
                                    closestMuhurat = muhurat;
                                    closestStartTime = startTime;
                                    closestEndTime = endTime;
                                    break; // Stop the loop once the matching time is found
                                }
                            }

                            // Display the closest muhurat and its time in the provided TextView
                            String muhuratData = "Chogadiya:-\n\n" + "Muhurat: " + closestMuhurat;
                            textView.setText(muhuratData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Time Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle network errors
                        Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(context).add(stringRequest);
    }
}
