package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tooltip.Tooltip;

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
    TextView tvPatientName, tvDate, txtDiagnosis, txtMedication;
    EditText  etPatientSymptoms;
    Button btnCancel, btnSave;
    ImageView img_Info;
    String patientID = "", patientName = "", symptomID = "",symptomName="", doctorID = "", medRegNo =  "", addDate = "";

    String URL_GETSYMPTMS = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientsymptomsfordiagnosis.php";
    String URL_GETMED = "http://sict-iis.nmmu.ac.za/ibitech/app/getmedicineid.php";
    String URL_GETCOND = "http://sict-iis.nmmu.ac.za/ibitech/app/getconditionid.php";
    String URL_ADDVISIT = "http://sict-iis.nmmu.ac.za/ibitech/app/insertvisit.php";
    String URL_ADD = "http://sict-iis.nmmu.ac.za/ibitech/app/addsymptom.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);


        SharedPreferences prefs = getSharedPreferences("DIAGNOSIS",MODE_PRIVATE);
        SharedPreferences preferences = getSharedPreferences("DOCPREFS", MODE_PRIVATE);

        txtDiagnosis = findViewById(R.id.txtDiagnosis);
        txtMedication = findViewById(R.id.txtMedication);
        etPatientSymptoms = findViewById(R.id.etSymptoms);
        tvDate = findViewById(R.id.txtDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final Date date = new Date();
        addDate = dateFormat.format(date);
        tvDate.setText(dateFormat.format(date));

        img_Info = findViewById(R.id.imgInfo);

        img_Info.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Tooltip tooltip = new Tooltip.Builder(img_Info)
                        .setText("Add more symptoms by separating them using a comma (,).")
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();
            }
        });

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
        //Log.i("tagconvertstr", "["+medRegNo+"]");

        GetPatientSymptoms(patientID);

        //tvPatientName.setText(patientName);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName);
        //toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btnSaveD);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Diagnosis.this, ViewPatientVisits.class));
                finish();
            }
        });


    }

    private void addSymptom(final String symptoms, final String date, final String patientID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {

                        etPatientSymptoms.setText(symptoms);
                        etPatientSymptoms.setEnabled(false);
                        Toast.makeText(Diagnosis.this, "Symptoms added.", Toast.LENGTH_LONG).show();
                        txtDiagnosis.setVisibility(View.VISIBLE);
                        act_Diagnosis.setVisibility(View.VISIBLE);
                        txtMedication.setVisibility(View.VISIBLE);
                        act_Medication.setVisibility(View.VISIBLE);

                        btnSave.setText("Save Diagnosis");
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                final String diagnosis = act_Diagnosis.getText().toString();
                                final String medication = act_Medication.getText().toString();
                                final String symptoms = etPatientSymptoms.getText().toString();
                                final String date = tvDate.getText().toString();
                                if(diagnosis.isEmpty() || medication.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Please ensure all fields are entered.",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    addVisit(addDate, doctorID, medRegNo, symptomID, patientID,diagnosis,medication);
                                }
                            }
                        });
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "Error : There was an internal error in adding the diagnosis, try again later", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"Error : There was an internal error with the response from our server, try again later.",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("symptom",symptoms);
                params.put("date", date);
                params.put("id",patientID);

                return params;
            }
        };

        Singleton.getInstance(Diagnosis.this).addToRequestQue(stringRequest);
    }

    public void GetPatientSymptoms(final String userID){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETSYMPTMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);
                    symptomID = object.getString("symptom_id");
                    symptomName = object.getString("symptom_name");
                    etPatientSymptoms.setText(symptomName);

                } catch (JSONException e) {
                    e.printStackTrace();
                    txtDiagnosis.setVisibility(View.INVISIBLE);
                    act_Diagnosis.setVisibility(View.INVISIBLE);
                    txtMedication.setVisibility(View.INVISIBLE);
                    act_Medication.setVisibility(View.INVISIBLE);
                    btnSave.setText("Save Symptoms");
                    btnSave.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            addSymptom(etPatientSymptoms.getText().toString(), addDate, patientID);
                        }
                    });

                    Toast.makeText(Diagnosis.this, "Note: This patient has no symptoms inserted. Add symptoms first.", Toast.LENGTH_LONG).show();
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

    public void addVisit(final String date, final String docID, final String medRegNo, final String symptomID, final String patientID,  final String condition, final String medicine){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADDVISIT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    //Log.i("tagconvertstr", "["+response+"]");
                    if (success.equals("1")) {
                        Toast.makeText(Diagnosis.this, "Diagnosis added successfully.", Toast.LENGTH_LONG).show();
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "Error : There was an internal error in adding the diagnosis, try again later", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"Error : There was an internal error with the response from our server, try again later.",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();


                params.put("date",date);
                params.put("docID",docID);
                params.put("medRegNo",medRegNo);
                params.put("symptomID",symptomID);
                params.put("patID",patientID);

                params.put("condition", condition);
                params.put("medicine", medicine);
                return params;
            }
        };

        Singleton.getInstance(Diagnosis.this).addToRequestQue(stringRequest);
    }

    @Override
    public void finish() {
        startActivity(new Intent(Diagnosis.this,ViewPatientVisits.class));
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
