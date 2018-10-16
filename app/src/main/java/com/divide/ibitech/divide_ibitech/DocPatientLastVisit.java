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
    String visitDate="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_last_visit);

        //shared prefs for patient last visit
        SharedPreferences preferences = getSharedPreferences("LASTVISIT",MODE_PRIVATE);
        visitDate = preferences.getString("pVisitDate", "");
        tv_VisitDate.setText(visitDate);

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

