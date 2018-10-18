package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PrescribeAllergyTreatment extends AppCompatActivity {

    TextView tv_AllergyName, tv_Species, tv_AllergyType, tv_DateAdded;
    EditText et_Treatment;
    String allergyID="", allergyName="", allergyType="", species="",date_added="", treatment="";
    String patientID = "",patientName="";
    android.support.v7.widget.Toolbar toolbar;
    Button btnPrescribe;

    String URLPRSCRBE = "http://sict-iis.nmmu.ac.za/ibitech/app/prescribeallergy.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescribe_allergy_treatment);

        tv_AllergyName = findViewById(R.id.tvAllergyName);
        tv_AllergyType = findViewById(R.id.tvAllergyType);
        tv_Species = findViewById(R.id.tvSpecies);
        tv_DateAdded = findViewById(R.id.tvDateAdded);

        btnPrescribe = findViewById(R.id.btnPrescribe);

        et_Treatment = findViewById(R.id.etAllergyTreatment);

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        SharedPreferences preferences = getSharedPreferences("PATIENTALLERGY", MODE_PRIVATE);


        allergyID = preferences.getString("pAllergyID", "");
        allergyName = preferences.getString("pAllergyName", "");
        allergyType = preferences.getString("pAllergyType", "");
        species = preferences.getString("pSpecies", "");
        date_added = preferences.getString("pDateAdded", "");
        treatment = preferences.getString("pTreatment", "");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(allergyName);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        tv_AllergyName.setText(allergyName);
        tv_AllergyType.setText(allergyType);
        tv_Species.setText(species);
        tv_DateAdded.setText(date_added);

        if (treatment.isEmpty()){
            btnPrescribe.setText("Prescribe");
        }
        else {
            et_Treatment.setText(treatment);
            btnPrescribe.setText("Update Treatment");

        }


        btnPrescribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 treatment = et_Treatment.getText().toString().trim();
                 if (treatment.isEmpty()){
                     et_Treatment.setError("Don't leave field blank.");
                 }
                 else {
                     StringRequest stringRequest = new StringRequest(Request.Method.POST, URLPRSCRBE, new Response.Listener<String>() {
                         @Override
                         public void onResponse(String response) {
                             try {
                                 JSONObject jsonObject = new JSONObject(response);
                                 String success = jsonObject.getString("success");

                                 if (success.equals("1")) {
                                     Toast.makeText(PrescribeAllergyTreatment.this, "Prescription successful.", Toast.LENGTH_LONG).show();
                                     startActivity(new Intent(getApplicationContext(), PatientMedicalRecord.class));
                                     finish();
                                 }
                                 else {
                                     Toast.makeText(PrescribeAllergyTreatment.this, "There was an error in adding your symptoms, try again later.", Toast.LENGTH_LONG).show();
                                 }

                             } catch (JSONException e) {
                                 e.printStackTrace();
                                 Toast.makeText(PrescribeAllergyTreatment.this, "JSON Error" + e.toString(), Toast.LENGTH_LONG).show();
                             }
                         }
                     }, new Response.ErrorListener() {
                         @Override
                         public void onErrorResponse(VolleyError error) {
                             Toast.makeText(PrescribeAllergyTreatment.this,"Volley Error"+error.toString(),Toast.LENGTH_LONG).show();

                         }
                     })
                     {
                         @Override
                         protected Map<String, String> getParams() {
                             Map<String,String> params = new HashMap<>();

                             params.put("allergyID",allergyID);
                             params.put("patID", patientID);
                             params.put("treatment",treatment);

                             return params;
                         }
                     };

                     Singleton.getInstance(PrescribeAllergyTreatment.this).addToRequestQue(stringRequest);
                 }
            }
        });



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
