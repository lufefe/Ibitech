package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.AllergyListAdapter;
import com.divide.ibitech.divide_ibitech.Adapter.MedInfoAdapter;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PatientMedicalRecord extends AppCompatActivity {

    TextView tvPatientID, tvPatientName, tvPatientDOB, tvPatientGender, tvPatientStatus, tvPatientCell, tvPatientBlood, tvPatientWeight, tvPatientHeight, tvPatientMedAid;

    String patientID = "", patientName = "", patientDOB="", patientGender ="", patientStatus="", patientCell="", patientBlood="", patientWeight="", patientHeight="", patientMedAid="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_medical_record);


        //gets shared preferences from patients list
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
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


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Medical Record" );
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String cats []= new String[]{"Last Visit","Allergies","Medication", "Conditions","Test Results", "Medical Devices", "Miscellaneous"};
        Integer imgid [] = new Integer[]{R.drawable.doctor, R.drawable.allergy, R.drawable.pills,R.drawable.health, R.drawable.flask, R.drawable.serum, R.drawable.hands};
        ListAdapter medAdapter = new MedInfoAdapter(this,cats, imgid);
        ListView listview = findViewById(R.id.lv_medInfo);
        listview.setAdapter(medAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                String cat = String.valueOf(parent.getItemAtPosition(position));
                if (cat.equals("Last Visit")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientLastVisit.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Allergies")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientAllergies.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Medication")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientMedication.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Conditions")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientConditions.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Test Results")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientTestResults.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Medical Devices")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientMedicalDevices.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
                if (cat.equals("Miscellaneous")){
                    Intent intent = new Intent(PatientMedicalRecord.this, DocPatientMiscellaneous.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }

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
        //tvPatientMedAid = findViewById(R.id.tvMedicalAid);


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


    @Override
    public void finish() {
        startActivity(new Intent(PatientMedicalRecord.this,ViewPatients.class));
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
            this.finish();

        return super.onOptionsItemSelected(item);
    }

}
