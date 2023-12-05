package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Menu extends AppCompatActivity {

    public static final String webAddress = "";
    String serviceURL = "https://www.niwater.com/current-service-updates/";
    String qualityURL ="https://www.niwater.com/water-quality-results/";
    String projectURL = "https://www.niwater.com/projects/";
    String reservoirMapURL = "https://niwater.maps.arcgis.com/apps/Embed/index.html?webmap=e053d0f7489c4483affb5d324b2e72e3&extent=-9.0006,53.9217," +
            "-4.488,55.3896&zoom=true&previewImage=false&scale=false&disable_scroll=false&theme=light";
    String reservoirInfoURL = "https://www.niwater.com/ni-water-reservoirs/";
    String waterCalcURL = "https://www.niwater.com/why-save-water/";
    String wasteCapacityURL ="https://www.niwater.com/Capacity-Information/";
    String selfServiceURL = "https://digitalservices.niwater.com/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button linksService, linkQuality,linkProject, linkSelfService, linkWasteCapacity,
                linkWaterCalc, linkReservoirInfo, linkReservoirMap;

        linksService = (Button) findViewById(R.id.btn_LinkServices);
        linkQuality = (Button) findViewById(R.id.btn_LinkQuality);
        linkProject = (Button) findViewById(R.id.btn_LinkProjects);
        linkSelfService = (Button) findViewById(R.id.btn_LinkSelService);
        linkWasteCapacity = (Button) findViewById(R.id.btn_LinkCapacity);
        linkWaterCalc = (Button) findViewById(R.id.btn_LinkWaterCalc);
        linkReservoirInfo = (Button) findViewById(R.id.btn_LinkReservoirInfo);
        linkReservoirMap = (Button) findViewById(R.id.btn_LinkResMap);

        linksService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress,serviceURL);
                startActivity(intent);
            }
        });

        linkQuality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, qualityURL);
                startActivity(intent);
            }
        });

        linkProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, projectURL);
                startActivity(intent);
            }
        });

        linkSelfService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, selfServiceURL);
                startActivity(intent);
            }
        });

        linkWasteCapacity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, wasteCapacityURL);
                startActivity(intent);
            }
        });

        linkWaterCalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, waterCalcURL);
                startActivity(intent);
            }
        });

        linkReservoirInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, reservoirInfoURL);
                startActivity(intent);
            }
        });

        linkReservoirMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Menu.this, niLinks.class);
                intent.putExtra(webAddress, reservoirMapURL);
                startActivity(intent);
            }
        });
    }
}