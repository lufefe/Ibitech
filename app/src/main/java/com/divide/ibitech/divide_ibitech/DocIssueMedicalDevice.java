package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class DocIssueMedicalDevice extends AppCompatActivity {

    TextView tvPatientName, tvDateRequested, tvDateIssued;
    EditText etInstructions;
    Spinner spStatus;
    Button btnIssueDevice;
    String patientID = "",patientName="";
    String deviceName="", requestDate="", issueDate="", instructions="", status="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_issue_medical_device);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvDateRequested = findViewById(R.id.tvDateRequested);
        tvDateIssued = findViewById(R.id.tvDateIssued);
        etInstructions = findViewById(R.id.etInstructions);

        btnIssueDevice = findViewById(R.id.btnIssueDevice);
        spStatus = findViewById(R.id.spStatus);

        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        SharedPreferences preferences = getSharedPreferences("PATIENTDEVICES", MODE_PRIVATE);

        deviceName = preferences.getString("pDeviceName", "");
        requestDate = preferences.getString("pRequestDate", "");
        issueDate = preferences.getString("pReceiveDate", "");
        instructions = preferences.getString("pInstructions", "");
        status = preferences.getString("pStatus", "");

        tvPatientName.setText(patientName);
        tvDateRequested.setText(requestDate);
        
        if (issueDate.equals("null"))
            tvDateIssued.setText(issueDate);
        else
            btnIssueDevice.setEnabled(false);

        etInstructions.setText(instructions);


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(deviceName);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }
}
