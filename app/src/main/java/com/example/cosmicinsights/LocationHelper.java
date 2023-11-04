package com.example.cosmicinsights;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationHelper {
    private final static int REQUEST_CODE = 100;

    private Activity activity;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private TextView lattitude, longitude;
    private EditText city_name;
    private ProgressBar progressBar;

    public LocationHelper(Activity activity, TextView lattitude, TextView longitude, ProgressBar progressBar, EditText city_name) {
        this.activity = activity;
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.progressBar = progressBar;
        this.city_name = city_name;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void checkAndRequestLocationPermission() {
        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (isLocationEnabled()) {
                getLastLocation();
            } else {
                showEnableLocationServicesDialog();
            }
        } else {
            askPermission();
        }
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private void showEnableLocationServicesDialog() {
        new AlertDialog.Builder(activity)
                .setTitle("Location Services Required")
                .setMessage("To use this app, please enable location services.")
                .setPositiveButton("Enable", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        activity.startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Handle user cancellation as needed.
                    }
                })
                .show();
    }

    private void getLastLocation() {
        progressBar.setVisibility(View.VISIBLE); // Show the ProgressBar

        if (ContextCompat.checkSelfPermission(activity, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                           // progressBar.setVisibility(View.GONE); // Hide the ProgressBar regardless of success or failure
                            if (location != null) {
                                updateLocationData(location);
                            } else {
                                handleNoLocationAvailable();
                            }
                        }
                    });
        } else {
            askPermission();
        }
    }

    private void updateLocationData(Location location) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if (!addresses.isEmpty()) {
                Address firstAddress = addresses.get(0);
                double latitudeValue = location.getLatitude();
                double longitudeValue = location.getLongitude();

                String latitudeText = String.format("%.2f", latitudeValue);
                String longitudeText = String.format("%.2f", longitudeValue);

                lattitude.setText(latitudeText);
                longitude.setText(longitudeText);
                city_name.setText(addresses.get(0).getLocality());
                if (progressBar != null) {
                    progressBar.setVisibility(View.GONE);
                }
            } else {
                handleNoLocationAvailable();
            }
        } catch (IOException e) {
            e.printStackTrace();
            handleNoLocationAvailable();
        }
    }

    private void handleNoLocationAvailable() {
        longitude.setText("Please enter city manually...");
        Toast.makeText(activity, "Unable to fetch location data", Toast.LENGTH_SHORT).show();
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(activity, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
    }
}
