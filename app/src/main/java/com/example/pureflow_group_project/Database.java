package com.example.pureflow_group_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database extends AppCompatActivity {
    EditText name_Input, lat_Input, lon_Input, lvl_Input;
    TextView test2, res_No;
    Button add, search;
    ListView myListView;

    DatabaseReference resdbRef;
    List<Reservoirs> reservoirsList;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // Arrays & Lists
        reservoirsList = new ArrayList<>();
        myListView = findViewById(R.id.listView1);

        // Firebase Database connection
       resdbRef = FirebaseDatabase.getInstance("https://pureflow-project-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Reservoirs");

        // Inputs
        name_Input = (EditText) findViewById(R.id.eText_ResName);
        lat_Input = (EditText) findViewById(R.id.eText_Lat);
        lon_Input = (EditText) findViewById(R.id.eText_Long);
        lvl_Input = (EditText) findViewById(R.id.eText_Levels);
        test2 = (TextView) findViewById(R.id.test2);

        // Buttons
        add = (Button) findViewById(R.id.btn_InputRes);
        search = (Button) findViewById(R.id.btn_Search);

        // Populate Array with Reservoirs from Firebase Database
        populateResData();

        // Add Reservoirs to Firebase Database on button click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = name_Input.getText().toString();
                double lat = Double.parseDouble(lat_Input.getText().toString());
                double lon = Double.parseDouble(lon_Input.getText().toString());
                double level = Double.parseDouble(lvl_Input.getText().toString());

                Reservoirs reservoirs = new Reservoirs(name,lat,lon,level);
                resdbRef.push().setValue(reservoirs);

                populateResData();
            }
        });

        // Search Reservoirs in Firebase Database on button click
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Find instance of Reservoirs with ID and display its level
                test2.setText(String.valueOf(reservoirsList.get(1).getLvl()));
            }
        });

    }
    // Insert Reservoirs into Firebase Database
    private void populateResData(){
        resdbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                reservoirsList.clear();
                for (DataSnapshot reservoirsSnapshot : snapshot.getChildren()) {
                    Reservoirs reservoirs = reservoirsSnapshot.getValue(Reservoirs.class);
                    reservoirsList.add(reservoirs);
                }
                ListAdapter adapter = new ListAdapter(Database.this, reservoirsList);
                myListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

    }

}
