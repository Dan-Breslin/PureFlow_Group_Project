package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

public class MainHome extends AppCompatActivity {
    Button btn;
    ImageView img;
    TextView postView, resView, weatherView, lvlView;
    String post, res, weather;
    int lvl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        btn = findViewById(R.id.menu_btn);
        img = findViewById(R.id.imageView6);
        postView = findViewById(R.id.location_txt);
        resView = findViewById(R.id.reservoir_txt);
        weatherView = findViewById(R.id.currentweath_txt);
        lvlView = findViewById(R.id.currentlvl_txt);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            res = extras.getString("Res_Name");
            lvl = extras.getInt("Res_Level");
            post = extras.getString("PostCode");
            weather = extras.getString("Weather");
        }

        postView.setText(post);
        resView.setText(res);
        weatherView.setText(weather);

        lvlView.setText(String.valueOf(lvl));

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainHome.this, Navigation.class);
                startActivity(intent);
            }
        });

        if (Objects.equals(weather, "Sunny") && lvl < 30) {
            img.setImageResource(R.drawable.conserve);
        } else if (Objects.equals(weather, "Rain") && lvl > 80) {
            img.setImageResource(R.drawable.flood);
        } else img.setImageResource(R.drawable.normal);
    }
}