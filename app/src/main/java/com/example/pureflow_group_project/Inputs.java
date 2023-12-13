package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Inputs extends AppCompatActivity {

    // This class may not be used in the final project, it was used to test the alternate inputs
    // & as a backup if we fail to get a map or weather API working

    Button getPostcodeLocation;
    private EditText postcodeEditText;
    public static double latitudeRes, longitudeRes;
    RadioGroup radioGroup;
    private RadioButton radioButton, radioButton2, radioButton3, radioButton4;
    int fakeResLvl = 90;
    public static String postCode = "", weather = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inputs);

        postcodeEditText = findViewById(R.id.PstCde);
        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);
        radioButton4 = findViewById(R.id.radioButton4);

        getPostcodeLocation = findViewById(R.id.btn_RunTest);

        postcodeEditText.setText(postCode);
        getPostcodeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postCode = postcodeEditText.getText().toString();
                if (!postCode.isEmpty()) {
                    new GetCoordinatesTask().execute(postCode);
                } else {
                    Toast.makeText(Inputs.this, "Please enter a postcode", Toast.LENGTH_SHORT).show();
                }

                checkButton();
                //open database activity
                Intent intent = new Intent(Inputs.this, Database.class);
                startActivity(intent);
            }
        });
    }

    private class GetCoordinatesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String postcode = params[0];
            String apiKey = "AIzaSyDJ1owNQrpD4T-kgAUPZ0lmcshzISc75cE"; // Replace with your API key
            String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + postcode + "&key=" + apiKey;

            try {
                URL url = new URL(apiUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onPostExecute(String result) {
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray results = jsonObject.getJSONArray("results");

                    if (results.length() > 0) {
                        JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");

                        double latitude = location.getDouble("lat");
                        double longitude = location.getDouble("lng");

                        latitudeRes = latitude;
                        longitudeRes = longitude;

                        // Transfer locational data to database activity
                        Intent i = new Intent(Inputs.this, Database.class);
                        i.putExtra("lat_variable",latitudeRes);
                        i.putExtra("lon_variable",longitudeRes);
                        startActivity(i);

                    } else {
                        Toast.makeText(Inputs.this, "Unable to find location", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Inputs.this, "Unable to find location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Inputs.this, "Error fetching data from the server", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkButton() {
        // Needs fixed
        if (radioButton.isChecked()) {
            weather = "Sunny";
        } else if (radioButton2.isChecked()) {
            weather = "Cloudy";
        } else if (radioButton3.isChecked()) {
            weather = "Rain";
        } else if (radioButton4.isChecked()) {
            weather= "Wind";
        }
    }
}