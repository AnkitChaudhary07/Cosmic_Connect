package com.example.cosmicinsights;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Hora {
    private RequestQueue requestQueue;
    private String apiUrl;
    private String planet;
    private Context context;

    public Hora(Context context, String apiUrl) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
        this.apiUrl = apiUrl;
    }

    private ProgressBar progressBar2;

    public void setProgressBar(ProgressBar progressBar2) {
        this.progressBar2 = progressBar2;
    }

    public String getPlanet() {
        return planet;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss: a", Locale.getDefault());
    String currentTime = sdf.format(new Date());
    Date targetTime;


    public void fetchDataAndDisplay(final TextView textView) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, apiUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getInt("status") == 200) {
                                JSONObject responseObject = response.getJSONObject("response");
                                JSONArray horas = responseObject.getJSONArray("horas");

                                StringBuilder horaData = new StringBuilder();

                                for (int i = 0; i < horas.length(); i++) {
                                    JSONObject hora = horas.getJSONObject(i);
                                    String start = hora.getString("start");
                                    String end = hora.getString("end");

                                    //Check if the hora falls within the target time
                                    if (compareTime(start, end)) {
                                        planet = hora.getString("hora");
                                        horaData.append("Hora: ").append(hora.getString("hora")).append("\n\n");
                                        horaData.append("Start: ").append(hora.getString("start")).append("\n");
                                        horaData.append("End: ").append(hora.getString("end")).append("\n");
                                        horaData.append("Benefits: ").append(hora.getString("benefits")).append("\n\n");
                                        horaData.append("Lucky Gem: ").append(hora.getString("lucky_gem"));

                                        textView.setText(horaData.toString());
                                        if (progressBar2 != null) {
                                            progressBar2.setVisibility(View.GONE);
                                        }
                                        break;
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error here
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    private Boolean compareTime(String start, String end) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd yyyy h:mm:ss a", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());

        try {
            // Parse the start and end times from the original format
            Date startDate = inputFormat.parse(start);
            Date endDate = inputFormat.parse(end);

            // Format the parsed dates into the desired format
            String formattedStartTime = outputFormat.format(startDate);
            String formattedEndTime = outputFormat.format(endDate);

            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
            String currentTime = sdf.format(new Date());

            Date targetTime = sdf.parse(currentTime);
            Date horaStart = sdf.parse(formattedStartTime);
            Date horaEnd = sdf.parse(formattedEndTime);

            if(targetTime.after(horaStart) && targetTime.before(horaEnd)) {
                return true;
            } else {
                return false;
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
}
