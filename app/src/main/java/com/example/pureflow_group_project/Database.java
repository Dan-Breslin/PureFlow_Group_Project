package com.example.pureflow_group_project;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Database extends AppCompatActivity {
    EditText name_Input, lat_Input, lon_Input, lvl_Input, inpLat, inpLon;
    TextView test;
    Button add, checkDist, edit, delete;
    ListView myListView;
    double distance1 = 0, distance2=0, value=0, value2=0;
    int locTracker=0;
    DatabaseReference resdbRef;
    List<Reservoirs> reservoirsList;

    Boolean isEdit = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
        //Inserts Keyboard view restrict into the activity
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Arrays & Lists
        reservoirsList = new ArrayList<>();
        myListView = findViewById(R.id.listView);

        // Firebase Database connection
       resdbRef = FirebaseDatabase.getInstance("https://pureflow-project-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Reservoirs");

        // Inputs
        name_Input = (EditText) findViewById(R.id.eText_ResName);
        lat_Input = (EditText) findViewById(R.id.eText_Lat);
        lon_Input = (EditText) findViewById(R.id.eText_Long);
        lvl_Input = (EditText) findViewById(R.id.eText_Levels);
        test = (TextView) findViewById(R.id.test);
        inpLat = (EditText) findViewById(R.id.testInpLat);
        inpLon = (EditText) findViewById(R.id.testInpLon);

        // Buttons
        add = (Button) findViewById(R.id.btn_InputRes);
        checkDist = (Button) findViewById(R.id.btn_Search);
        edit = (Button) findViewById(R.id.btn_EditRes);
        delete = (Button) findViewById(R.id.btn_DelRes);

        // Get Intent from Postcode Locations
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getDouble("my_variable");
            inpLat.setText(String.valueOf(value));
            value2 = extras.getDouble("my_variable2");
            inpLon.setText(String.valueOf(value2));
        }

        // Populate Array with Reservoirs from Firebase Database
        populateResData();

        // Add Reservoirs to Firebase Database on button click
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isEdit){
                    Query queryByName = resdbRef.orderByChild("name").equalTo(name_Input.getText().toString());
                    queryByName.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if (task.isSuccessful()) {
                                for (DataSnapshot snapshot : task.getResult().getChildren()) {
                                    // Update found snapshot record(s)
                                    snapshot.getRef().setValue(new Reservoirs(name_Input.getText().toString(),
                                            Double.parseDouble(lat_Input.getText().toString()),
                                            Double.parseDouble(lon_Input.getText().toString()),
                                            Integer.parseInt(lvl_Input.getText().toString())));
                                    Toast.makeText(Database.this, "Record " + snapshot.getKey() + " Updated !", Toast.LENGTH_SHORT).show();
                                    isEdit = false;
                                }
                            } else {
                                Toast.makeText(Database.this, "Record Not Updated !!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else {
                    String name = name_Input.getText().toString();
                    double lat = Double.parseDouble(lat_Input.getText().toString());
                    double lon = Double.parseDouble(lon_Input.getText().toString());
                    int level = Integer.parseInt(lvl_Input.getText().toString());

                    Reservoirs reservoirs = new Reservoirs(name, lat, lon, level);
                    resdbRef.push().setValue(reservoirs);
                    Toast.makeText(Database.this, "New Entry Added !" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Search Reservoirs in Firebase Database on button click
        checkDist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             shortestDistance();
            }

        });

        //Edit Button run Edit Function
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findReservoir();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReservoir();
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
                if (value != 0 && value2 != 0){
                    // Run the shortest distance function
                    shortestDistance();
                }

                // Populate the list view with the data from the database
                ListAdapter adapter = new ListAdapter(Database.this, reservoirsList);
                myListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        value = 0;
        value2 = 0;
    }

    // Search Array list and populate Fields.
    private void findReservoir(){

        //populate the text fields with the data from the database
        Query queryByName = resdbRef.orderByChild("name").equalTo(name_Input.getText().toString());
        queryByName.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.getResult().getValue() == null) {
                    Toast.makeText(Database.this, "Record Not Found ! Edit and try again !", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(Database.this, "Record Found !", Toast.LENGTH_SHORT).show();
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        // Populate found snapshot record(s)
                        lat_Input.setText(String.valueOf(snapshot.child("lat").getValue()));
                        lon_Input.setText(String.valueOf(snapshot.child("lon").getValue()));
                        lvl_Input.setText(String.valueOf(snapshot.child("lvl").getValue()));
                        isEdit = true;
                        //Exit the loop when data entered
                        break;
                    }
                }
            }
        });
    }

    // Calculate the shortest distance from 2 sets of co-ordinates
    private void shortestDistance(){

        Location firstLocation = new Location("");
        Location secondLocation = new Location("");

        // Initially we are using text input boxes to set location - this will be fed on final by map location ( Postcode interface)
        firstLocation.setLatitude(Double.parseDouble(inpLat.getText().toString()));
        firstLocation.setLongitude(Double.parseDouble(inpLon.getText().toString()));

        //Reset the distance and used variables
        distance1 = 0;
        distance2 = 0;
        locTracker = 0;

        // Loop through all database entries and set the shortest distance
        for (int i = 0; i < reservoirsList.size(); i++) {
            secondLocation.setLatitude(reservoirsList.get(i).getLat());
            secondLocation.setLongitude(reservoirsList.get(i).getLon());
            if(distance1 == 0 && distance2 == 0){
                distance1 = firstLocation.distanceTo(secondLocation);
                distance2 = firstLocation.distanceTo(secondLocation);
                locTracker = i;
            }
            else {
                if (firstLocation.distanceTo(secondLocation) < distance1) {
                    distance2 = distance1;
                    distance1 = firstLocation.distanceTo(secondLocation);
                    locTracker = i;
                } else if (firstLocation.distanceTo(secondLocation) < distance2) {
                    distance2 = firstLocation.distanceTo(secondLocation);
                    locTracker = i;
                }
            }
        }
        // Temporary message showing the nearest reservoir name
            Toast.makeText(this, "The nearest Reservoir is: " + reservoirsList.get(locTracker).getName(), Toast.LENGTH_SHORT).show();
            test.setText(reservoirsList.get(locTracker).getName());

    }

    // Delete Reservoir from Firebase Database
    private void deleteReservoir(){
        Query queryByName = resdbRef.orderByChild("name").equalTo(name_Input.getText().toString());
        queryByName.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {

            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (task.isSuccessful()) {
                    for (DataSnapshot snapshot : task.getResult().getChildren()) {
                        // Remove found snapshot record(s)
                        snapshot.getRef().removeValue();
                        Toast.makeText(Database.this, "Record " + snapshot.getKey() + " Deleted !", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Database.this, "Record Not Removed !!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
