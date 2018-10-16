package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class DocPatientLastVisit extends AppCompatActivity {

    TextView tv_VisitDate, tv_Doctor, tv_Symptoms, tv_Diagnosis, tv_Medication, tv_Notes;

    android.support.v7.widget.Toolbar toolbar;
    String patientID="", patientName="";
    String visitDate="",doctorName="", symptoms="",diagnosis="",medication="",notes="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_last_visit);

        tv_VisitDate = findViewById(R.id.tvVisitDate);
        tv_Doctor = findViewById(R.id.tvDoctor);
        tv_Symptoms = findViewById(R.id.tvSymptoms);
        tv_Diagnosis = findViewById(R.id.tvDiagnosis);
        tv_Medication = findViewById(R.id.tvMedication);
        tv_Notes = findViewById(R.id.tvNotes);

        //shared prefs for patient last visit
        SharedPreferences preferences = getSharedPreferences("LASTVISIT",MODE_PRIVATE);
        visitDate = preferences.getString("pVisitDate", "");
        doctorName = preferences.getString("pDoctor", "");
        symptoms = preferences.getString("pSymptomName", "");
        diagnosis = preferences.getString("pDiagnosis", "");
        medication = preferences.getString("pMedication", "");
        notes = preferences.getString("pNotes", "");


        tv_VisitDate.setText(visitDate);
        tv_Doctor.setText(doctorName);
        tv_Symptoms.setText(symptoms);
        tv_Diagnosis.setText(diagnosis);
        tv_Medication.setText(medication);
        tv_Notes.setText(notes);

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientName = prefs.getString("pName","");
        patientID = prefs.getString("pID","");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Last Visit");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }


    @Override
    public void finish() {
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

