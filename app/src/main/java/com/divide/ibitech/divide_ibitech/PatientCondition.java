package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientCondition extends AppCompatActivity {

    TextView tvConditionName, tvVisitDate, tvDoctorName, tvSymptoms, tvSymptomDate, tvMedication;
    EditText etNotes;

    String patientID = "", conditionID = "", conditionName = "", visitDate = "", doctorID = "", doctorName = "",medRegNo = "", symptomID = "", symptoms = "", symptomDate = "", medicineID = "", medication = "";

    String URL_GETCONDDETAILS = "http://sict-iis.nmmu.ac.za/ibitech/app/getconditiondetails.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_condition);

        tvConditionName = findViewById(R.id.tvConditionName);
        tvVisitDate = findViewById(R.id.tvVisitDate);
        tvDoctorName = findViewById(R.id.tvDoctor);
        tvSymptoms = findViewById(R.id.tvSymptoms);
        tvSymptomDate = findViewById(R.id.tvSymptomsDate);
        tvMedication = findViewById(R.id.tvMedication);

        etNotes = findViewById(R.id.etNotes);


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Patient Condition");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("PATIENTCONDITION",MODE_PRIVATE);
        SharedPreferences proPrefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        patientID = proPrefs.getString("pID","");

        visitDate = prefs.getString("pVisitDate","");
        doctorID = prefs.getString("pDoctorID","");
        medRegNo = prefs.getString("pMedicalRegNo","");
        symptomID = prefs.getString("pSymptomID","");
        conditionID = prefs.getString("pConditionID","");
        medicineID = prefs.getString("pMedicine","");

        tvVisitDate.setText(visitDate);

        getConditionDetails(doctorID, medRegNo, symptomID, patientID, conditionID, medicineID);


    }

    private void getConditionDetails(final String doctorID,final String medRegNo,final String symptomID,final String patientID,final String conditionID,final String medicineID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETCONDDETAILS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    tvConditionName.setText(object.getString("condition_name"));
                    tvVisitDate.setText(object.getString("visit_date"));
                    tvDoctorName.setText(String.format("Dr %s %s", object.getString("first_name"), object.getString("surname")));
                    tvSymptoms.setText(object.getString("symptom_name"));
                    tvSymptomDate.setText(object.getString("date_added"));
                    tvMedication.setText(object.getString("description"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientCondition.this, "Note: There was a problem with retrieving your data, give us a moment to fix it.", Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientCondition.this,"Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("docID", doctorID);
                params.put("medRegNo",medRegNo);
                params.put("symptomID",symptomID );
                params.put("patID", patientID);
                params.put("condID", conditionID);
                params.put("medID",medicineID);

                return params;
            }
        };

        Singleton.getInstance(PatientCondition.this).addToRequestQue(stringRequest);


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
            startActivity(new Intent(PatientCondition.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
