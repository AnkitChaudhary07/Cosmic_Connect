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

public class Retrogrades {
    private Context context;

    public Retrogrades(Context context) {
        this.context = context;
    }

    public void fetchRetrogradesData(String url, TextView textView) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject responseData = jsonObject.getJSONObject("response");
                            String data = responseData.getString("bot_response");

                            // Extract and display the Choghadiya Muhurta information
                            textView.setText(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            handleJsonParsingError("Error...");
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

    private void handleJsonParsingError(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}