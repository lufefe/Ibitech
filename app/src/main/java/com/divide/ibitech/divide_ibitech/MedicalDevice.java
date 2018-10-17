package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MedicalDevice extends AppCompatActivity {

    TextView tvDeviceName, tvInstructions;
    String deviceName="",instructions="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_device);

        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvInstructions = findViewById(R.id.tvInstructions);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Device Information");
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("PATIENTDEVICES", MODE_PRIVATE);

        deviceName = preferences.getString("pDeviceName", "");
        instructions = preferences.getString("pInstructions", "");

        tvDeviceName.setText(deviceName);;
        tvInstructions.setText(instructions);


    }
}
