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
import android.util.Pair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoginActivity extends AppCompatActivity {

    Button login_btn;
    ProgressBar progress_bar;
    EditText email1, password1;
    TextView go_to_signup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        go_to_signup = findViewById(R.id.go_to_signup);
        login_btn = findViewById(R.id.login_btn);
        email1 = findViewById(R.id.email);
        password1 = findViewById(R.id.password);
        progress_bar = findViewById(R.id.progress_bar);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Show the progress bar
                progress_bar.setVisibility(View.VISIBLE);

                String email = email1.getText().toString();
                String password = password1.getText().toString();
                new LoginTask().execute(email, password);
            }
        });

        go_to_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private class LoginTask extends AsyncTask<String, Void, String> {
        private int responseCode;
        @Override
        protected String doInBackground(String... params) {
            String email = params[0];
            String password = params[1];
            String apiUrl = "https://registration-sigma-blue.vercel.app/api/user/login";

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                // Create JSON data for the login request
                String loginData = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";

                // Write the JSON data to the request body
                OutputStream os = connection.getOutputStream();
                os.write(loginData.getBytes());
                os.flush();
                os.close();

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Login successful
                    return "success";
                } else {
                    // Login failed
                    return "Login failed. Check your credentials.";
                }
            } catch (IOException e) {
                e.printStackTrace();
                responseCode = HttpURLConnection.HTTP_INTERNAL_ERROR;
                return "Login failed. An error occurred.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            // Hide the progress bar
            progress_bar.setVisibility(View.GONE);

            if (result != null) {
                if (result.equals("Login failed. Check your credentials.")) {
                    // Login failed
                    Toast.makeText(LoginActivity.this, result, Toast.LENGTH_SHORT).show();
                } else if (result.equals("success")) {
                    Toast.makeText(LoginActivity.this, "Login Success...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            } else {
                // Handle the case when the response is null or an unexpected format
                Toast.makeText(LoginActivity.this, "Login failed. An error occurred.", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String readResponse(java.io.InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

}