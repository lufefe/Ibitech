package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class PatientAllergy extends AppCompatActivity {

    TextView tvAllergyName, tvAllergyType, tvAllergySpecies, tvDateAdded, tvTreatment;

    String patientID = "", allergyID = "", allergyName = "", allergyType = "", species = "",dateAdded = "", treatmentID = "", tested = "";

    String URL_GETALLRGYDETAILS = "http://sict-iis.nmmu.ac.za/ibitech/app/getallergydetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_allergy);

        tvAllergyName = findViewById(R.id.tvAllergyName);
        tvAllergyType = findViewById(R.id.tvAllergyType);
        tvAllergySpecies = findViewById(R.id.tvSpecies);
        tvDateAdded = findViewById(R.id.tvDateAdded);
        tvTreatment = findViewById(R.id.tvTreatment);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Allergy");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("PATIENTALLERGY",MODE_PRIVATE);
        SharedPreferences proPrefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        patientID = proPrefs.getString("pID","");

        allergyID = prefs.getString("pAllergyID","");
        allergyName = prefs.getString("pAllergyName","");
        allergyType = prefs.getString("pAllergyType", "");
        species = prefs.getString("pSpecies", "");
        dateAdded = prefs.getString("pDateAdded","");
        treatmentID = prefs.getString("pTreatmentID", "");
        tested = prefs.getString("pTested", "");

        tvAllergyName.setText(allergyName);
        tvAllergyType.setText(allergyType);
        tvAllergySpecies.setText(species);
        tvDateAdded.setText(dateAdded);
        tvTreatment.setText(treatmentID);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(PatientAllergy.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
