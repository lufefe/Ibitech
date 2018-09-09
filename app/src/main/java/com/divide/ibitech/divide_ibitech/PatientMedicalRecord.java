package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class PatientMedicalRecord extends AppCompatActivity {

    TextView tvPatientID, tvPatientName, tvPatientDOB, tvPatientGender, tvPatientStatus, tvPatientCell, tvPatientBlood, tvPatientWeight, tvPatientHeight, tvPatientMedAid;

    String patientID = "", patientName = "", patientDOB="", patientGender ="", patientStatus="", patientCell="", patientBlood="", patientWeight="", patientHeight="", patientMedAid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_record);

        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvPatientID = findViewById(R.id.tvIDNumber);
        //tvPatientDOB = findViewById(R.id.tvDOB);
        tvPatientGender = findViewById(R.id.tvSex);
        tvPatientStatus = findViewById(R.id.tvMStatus);
        tvPatientCell = findViewById(R.id.tvCellNo);
        tvPatientBlood = findViewById(R.id.tvBloodType);
        tvPatientWeight = findViewById(R.id.tvWeight);
        tvPatientHeight = findViewById(R.id.tvHeight);
//        tvPatientMedAid = findViewById(R.id.tvMedicalAid);

        patientID = prefs.getString("pID","");
        patientName = prefs.getString("pName","");
        patientDOB = prefs.getString("pDOB","");
        patientGender = prefs.getString("pGender","");
        patientStatus = prefs.getString("pStatus","");
        patientCell = prefs.getString("pCell","");
        patientBlood = prefs.getString("pBloodType","");
        patientWeight = prefs.getString("pWeight","");
        patientHeight = prefs.getString("pHeight","");
        patientMedAid = prefs.getString("pMedicalAid","");

        tvPatientName.setText(patientName);
        tvPatientID.setText(patientID);
        tvPatientCell.setText(patientCell);
        //tvPatientDOB.setText(patientDOB);

        if (patientGender.equals("M"))
            tvPatientGender.setText("Male");
        else if (patientGender.equals("F"))
            tvPatientGender.setText("Female");

        tvPatientBlood.setText(patientBlood);
        tvPatientWeight.setText(patientWeight);
        tvPatientHeight.setText(patientHeight);
        tvPatientStatus.setText(patientStatus);

    }
}
