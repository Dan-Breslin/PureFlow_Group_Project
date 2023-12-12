package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class Home extends AppCompatActivity {

    // This class may not be used in the final project, it was used to test the alternate inputs
    // & as a backup if we fail to get a map or weather API working
    // Code taken from the internet was used and altered to make this work and to cut down on time as its not the main focus of the project
    Button getPostcodeLocation;
    private EditText postcodeEditText;
    private TextView resultLat, resultLng;
    double latitudeRes, longitudeRes;
    RadioGroup radioGroup;
    private RadioButton radioButton, radioButton2, radioButton3;
    ImageView warningPic;
    int fakeResLvl = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        postcodeEditText = findViewById(R.id.PstCde);
        resultLat = findViewById(R.id.PstCdelatRes);
        resultLng = findViewById(R.id.PstCdelonRes);

        radioGroup = findViewById(R.id.radioGroup);
        radioButton = findViewById(R.id.radioButton);
        radioButton2 = findViewById(R.id.radioButton2);
        radioButton3 = findViewById(R.id.radioButton3);

        getPostcodeLocation = findViewById(R.id.btn_RunTest);
        getPostcodeLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This is where the code will go to get the location from the postcode
                // & then use that location to search database for the nearest water source
                String postcode = postcodeEditText.getText().toString();
                if (!postcode.isEmpty()) {
                    new GetCoordinatesTask().execute(postcode);
                } else {
                    Toast.makeText(Home.this, "Please enter a postcode", Toast.LENGTH_SHORT).show();
                }

                checkButton();
                //open database activity
                Intent intent = new Intent(Home.this, Database.class);
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

                        resultLat.setText(String.valueOf(latitude));
                        resultLng.setText(String.valueOf(longitude));

                        // Transfer locational data to database activity
                        Intent i = new Intent(Home.this, Database.class);
                        i.putExtra("lat_variable", latitudeRes);
                        i.putExtra("lon_variable", longitudeRes);
                        startActivity(i);

                    } else {
                        Toast.makeText(Home.this, "Unable to find location", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Home.this, "Unable to find location", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(Home.this, "Error fetching data from the server", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void checkButton() {
        if (radioButton.isChecked() && fakeResLvl < 30) {
            warningPic.setImageResource(R.drawable.conserve);
        } else if (radioButton2.isChecked() && fakeResLvl > 70) {
            warningPic.setImageResource(R.drawable.flood);
        } else if (radioButton3.isChecked()) {
            warningPic.setImageResource(R.drawable.flood);
        } else {
            warningPic.setImageResource(R.drawable.normal);
        }
    }
}