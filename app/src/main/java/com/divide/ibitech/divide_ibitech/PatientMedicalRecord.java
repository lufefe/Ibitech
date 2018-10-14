package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.divide.ibitech.divide_ibitech.Adapter.MedInfoAdapter;

public class PatientMedicalRecord extends AppCompatActivity {

    TextView tvPatientID, tvPatientName, tvPatientDOB, tvPatientGender, tvPatientStatus, tvPatientCell, tvPatientBlood, tvPatientWeight, tvPatientHeight, tvPatientMedAid;

    String patientID = "", patientName = "", patientDOB="", patientGender ="", patientStatus="", patientCell="", patientBlood="", patientWeight="", patientHeight="", patientMedAid="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_record);

        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName);
        setSupportActionBar(toolbar);

        String items []= new String[]{"Last Visit","Allergies","Medication", "Conditions","Test Results"};
        ListAdapter medAdapter = new MedInfoAdapter(this,items);
        ListView listview = findViewById(R.id.lv_medInfo);
        listview.setAdapter(medAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String cat = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(PatientMedicalRecord.this, cat, Toast.LENGTH_LONG).show();
            }
        });


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
        tvPatientWeight.setText(String.format("%s kg", patientWeight));
        tvPatientHeight.setText(String.format("%s cm", patientHeight));
        tvPatientStatus.setText(patientStatus);

    }
}
