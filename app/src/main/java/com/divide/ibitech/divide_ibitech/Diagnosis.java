package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Diagnosis extends AppCompatActivity {

    AutoCompleteTextView act_Diagnosis;
    String[] conditionNames, medicationNames;
    AutoCompleteTextView act_Medication;
    TextView tvPatientName, tvPatientSymptoms, tvDate;
    Button btnCancel, btnSave;
    String patientID = "", patientName = "", symptomID = "", doctorID = "", medRegNo =  "", date = "";

    String URL_GETSYMPTMS = "http://sict-iis.nmmu.ac.za/ibitech/app-test/getpatientsymptomsfordiagnosis.php";
    String URL_ADDVISIT = "http://sict-iis.nmmu.ac.za/ibitech/app-test/insertvisit.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);


        SharedPreferences prefs = getSharedPreferences("DIAGNOSIS",MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("DOCPREFS", MODE_PRIVATE);

        tvPatientName = findViewById(R.id.txtPatientName);
        tvPatientSymptoms = findViewById(R.id.txtSymptoms);
        tvDate = findViewById(R.id.txtDate);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        tvDate.setText(dateFormat.format(date));

        act_Diagnosis = findViewById(R.id.actx_Diagnosis);
        act_Medication = findViewById(R.id.actx_Medication);

        conditionNames = getResources().getStringArray(R.array.conditions);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conditionNames);
        act_Diagnosis.setAdapter(cAdapter);

        medicationNames = getResources().getStringArray(R.array.medication);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,medicationNames);
        act_Medication.setAdapter(mAdapter);

        patientID = prefs.getString("pID","");
        patientName = prefs.getString("pName","");

        doctorID = preferences.getString("pID","");
        medRegNo = preferences.getString("pRegNo","");

        GetPatientSymptoms(patientID);

        tvPatientName.setText(patientName);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSave);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Diagnosis.this, ViewAppointments.class));
                finish();
            }
        });
        final String diagnosis = act_Diagnosis.getText().toString();
        final String medication = act_Medication.getText().toString();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(diagnosis.isEmpty() || medication.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Please ensure all fields are entered.",Toast.LENGTH_SHORT).show();
                }
                else {
                    addVisit(date.toString(), doctorID, medRegNo, symptomID, patientID);
                }
            }
        });



    }

    public void GetPatientSymptoms(final String userID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETSYMPTMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    tvPatientSymptoms.setText(object.getString("symptom_name"));
                    symptomID = object.getString("symptom_id");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "1Register Error" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"2Register Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",userID);

                return params;
            }
        };

        Singleton.getInstance(Diagnosis.this).addToRequestQue(stringRequest);
    }

    public void addVisit(final String date, final String docID, final String medRegNo, final String symptomID, final String patientID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDVISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(Diagnosis.this, "Diagnosis added successfully.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Diagnosis.this, ViewAppointments.class));
                    }
                    else {
                        Toast.makeText(Diagnosis.this, "Sorry, diagnosis cannot be added at the moment.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "Error : There was an internal error in adding the diagnosis.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"Error : There was an internal error in adding the diagnosis.",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("date",date);
                params.put("docID",docID);
                params.put("medRegNo",medRegNo);
                params.put("symptomID",symptomID);
                params.put("patID",patientID);
                return params;
            }
        };

        Singleton.getInstance(Diagnosis.this).addToRequestQue(stringRequest);
    }
}
