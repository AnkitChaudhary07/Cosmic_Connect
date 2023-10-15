package com.example.cosmicinsights;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SignupActivity extends AppCompatActivity {

    EditText user_name, user_email, user_password, confirm_password;
    ProgressBar progress_bar;
    TextView go_to_signin;
    Button signUp_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        user_name = findViewById(R.id.user_name);
        user_email = findViewById(R.id.user_email);
        user_password = findViewById(R.id.user_password);
        signUp_btn = findViewById(R.id.signUp_btn);
        confirm_password = findViewById(R.id.confirm_password);
        go_to_signin = findViewById(R.id.go_to_signin);
        progress_bar = findViewById(R.id.progress_bar);

        signUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show the progress bar
                progress_bar.setVisibility(View.VISIBLE);

                // Get input from EditText fields
                String name = user_name.getText().toString();
                String email = user_email.getText().toString();
                String password = user_password.getText().toString();
                String confirmPassword = confirm_password.getText().toString();

                // Check if the passwords match
                if (password.equals(confirmPassword) && password.length() >= 6) {
                    // Passwords match and are at least 6 characters long
                    // You can proceed with registration or other actions
                    new RegistrationTask().execute(name, email, password);

                } else if (!password.equals(confirmPassword)) {
                    // Passwords do not match, show an error message
                    Toast.makeText(SignupActivity.this, "Passwords do not match", Toast.LENGTH_SHORT).show();

                } else {
                    // Password length is less than 6 characters, show an error message
                    Toast.makeText(SignupActivity.this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show();
                }
            }
        });

        go_to_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class RegistrationTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String name = params[0];
            String email = params[1];
            String password = params[2];

            try {
                // Create a JSON object with the registration data
                JSONObject data = new JSONObject();
                data.put("name", name);
                data.put("email", email);
                data.put("password", password);

                // Define the API endpoint URL
                String apiUrl = "https://registration-sigma-blue.vercel.app/api/user/signup";

                // Create a URL object
                URL url = new URL(apiUrl);

                // Open a connection to the URL
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set the request method to POST
                connection.setRequestMethod("POST");

                // Set request properties
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Write the JSON data to the connection's output stream
                try (OutputStream os = connection.getOutputStream()) {
                    byte[] input = data.toString().getBytes("utf-8");
                    os.write(input, 0, input.length);
                }

                // Get the response from the API
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // If the response code is OK, read the response from the connection
                    Scanner scanner = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\A");
                    String response = scanner.hasNext() ? scanner.next() : "";

                    // You can parse and handle the response as needed
                    return response;
                } else {
                    return null; // Handle API error
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                return null; // Handle exception
            }
        }

        @Override
        protected void onPostExecute(String response) {
            // Hide the progress bar
            progress_bar.setVisibility(View.GONE);
            if (response != null) {
                // Handle the response (e.g., registration success or failure)
                // You can parse the JSON response if needed
                // For example, if the API returns JSON with a "success" field
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        // Registration successful, proceed with further actions
                        Toast.makeText(SignupActivity.this, "Registration Success", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Registration failed, show an error message
                        Toast.makeText(SignupActivity.this, "Registration Failed ", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Handle API request failure
                Toast.makeText(SignupActivity.this, "Either you already have account or Some error has occured...", Toast.LENGTH_SHORT).show();
            }
        }
    }
}