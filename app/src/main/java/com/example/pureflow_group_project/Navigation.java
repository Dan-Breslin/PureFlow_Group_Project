package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Navigation extends AppCompatActivity {
    Button homebtn;
    Button mapbtn;
    Button weatherbtn;
    Button databasebtn;
    Button linkbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        homebtn = (Button) findViewById(R.id.home_btn);
        mapbtn = (Button) findViewById(R.id.maps_btn);
        weatherbtn = (Button) findViewById(R.id.weather_btn);
        databasebtn = (Button) findViewById(R.id.database_btn);
        linkbtn = (Button) findViewById(R.id.link_btn);
        homebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, MainActivity.class);
                startActivity(intent);
            }
        });
        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, MainActivity.class);
                startActivity(intent);
            }
        });
        weatherbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, MainActivity.class);
                startActivity(intent);
            }
        });
        databasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, Database.class);
                startActivity(intent);
            }
        });
        linkbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Navigation.this, niLinks.class);
                startActivity(intent);
            }
        });
    }
}