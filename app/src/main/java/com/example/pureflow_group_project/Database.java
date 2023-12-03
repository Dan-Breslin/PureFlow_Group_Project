package com.example.pureflow_group_project;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Database extends AppCompatActivity {
    EditText name_Input, lat_Input, lon_Input, lvl_Iput;
    TextView info_Output, res_No;
    Button add;

    DatabaseReference resdbRef;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        // Firebase Database connection
       resdbRef = FirebaseDatabase.getInstance("https://pureflow-project-default-rtdb.europe-west1.firebasedatabase.app/").getReference().child("Reservoirs");
        // Test adding Details to User
//        DatabaseReference myRef = database.getReference("User");
//        //Set Value of User
//        myRef.setValue("This does work");

        info_Output = (TextView) findViewById(R.id.tv_Information);
        res_No = (TextView) findViewById(R.id.tv_ResID);
        name_Input = (EditText) findViewById(R.id.eText_ResName);
        lat_Input = (EditText) findViewById(R.id.eText_Lat);
        lon_Input = (EditText) findViewById(R.id.eText_Long);
        lvl_Iput = (EditText) findViewById(R.id.eText_Levels);

        add = (Button) findViewById(R.id.btn_InputRes);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String data = name_Input.getText().toString();
                insertReserviorData();
            }
        });
    }
    private void insertReserviorData(){
        String name = name_Input.getText().toString();
        double lat = Double.parseDouble(lat_Input.getText().toString());
        double lon = Double.parseDouble(lon_Input.getText().toString());
        double level = Double.parseDouble(lvl_Iput.getText().toString());


        Reservoirs reservoirs = new Reservoirs(name,lat,lon,level);
        resdbRef.push().setValue(reservoirs);

    }
}
