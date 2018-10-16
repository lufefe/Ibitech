package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.DocPatientsAdapter;
import com.divide.ibitech.divide_ibitech.Models.PatientsList;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DocPatientLastVisit extends AppCompatActivity {

    TextView tv_VisitDate, tv_Doctor, tv_Symptoms, tv_Diagnosis, tv_Medication, tv_Notes;

    android.support.v7.widget.Toolbar toolbar;
    String patientID="", patientName="";

    //gets script for last visit of selected patient
    String URL_GETLSTVST = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientlastvisit.php";

    //gets script for doctor of last visit
    String URL_GETLSTVSTDOC = "http://sict-iis.nmmu.ac.za/ibitech/app/getlastvisitdoctor.php";

    //gets script for symptom of last visit
    String URL_GETLSTVSTSYMPTM = "http://sict-iis.nmmu.ac.za/ibitech/app/getlastvisitsymptoms.php";

    //gets script for conditionds/diagnosis of last visit
    String URL_GETLSTVSTCONDS = "http://sict-iis.nmmu.ac.za/ibitech/app/getlastvisitcondition.php";

    //gets script for medication of last visit
    String URL_GETLSTVSTMEDS = "http://sict-iis.nmmu.ac.za/ibitech/app/getlastvisitmedication.php";

    //gets script for patient notes of last visit
    String URL_GETLSTVSTNOTES = "http://sict-iis.nmmu.ac.za/ibitech/app/getlastvisitnotes.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_last_visit);

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientName = prefs.getString("pName","");
        patientID = prefs.getString("pID","");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Last Visit");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        GetLastVisit(patientID);
    }

    private void GetLastVisit(final String patientID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String visitID="", visitDate="",docID="",medReg="",symptomID="",patID="",condID="",medicineID="";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    visitID = object.getString("visitID");
                    visitDate = object.getString("visitDate");
                    docID = object.getString("doctor_id");
                    medReg = object.getString("med_reg_no");
                    symptomID = object.getString("symptom_id");
                    patID = object.getString("patient_id"); //can be opted out
                    condID = object.getString("condition_id");
                    medicineID = object.getString("medicine_id");

                    tv_VisitDate.setText(visitDate);

                    getLastVisitDoctor(docID,medReg);
                    getLastVisitSymptoms(symptomID);
                    getLastVisitCondition(condID);
                    getLastVisitMedication(medicineID);
                    getLastVisitNotes(visitID,visitDate);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("id",patientID);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);

    }

    private void getLastVisitNotes(final String visitID, final String visitDate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVSTNOTES, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String description, dateAdded="";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    description = object.getString("description");
                    dateAdded = object.getString("date_added");

                    tv_Notes.setText(description);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("visitID",visitID);
                params.put("visitDate",visitDate);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);


    }

    private void getLastVisitMedication(final String medicineID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVSTMEDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String medicine;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    medicine = object.getString("medicine_name");


                    tv_Medication.setText(medicine);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("medID",medicineID);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);
    }

    private void getLastVisitCondition(final String condID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVSTCONDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String diagnosis="";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    diagnosis = object.getString("condition_name");

                    tv_Diagnosis.setText(diagnosis);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("condID",condID);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);
    }

    private void getLastVisitSymptoms(final String symptomID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVSTSYMPTM, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String symptomName, dateAdded="";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    symptomName = object.getString("symptom_name");
                    dateAdded = object.getString("date_added");

                    // sets symptoms oo text view
                    tv_Symptoms.setText(symptomName);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("symptomID",symptomID);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);
    }

    private void getLastVisitDoctor(final String docID,final String medReg) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVSTDOC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String firstName="", surname="",cellphoneNo="",occupation="";

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    firstName = object.getString("first_name");
                    surname = object.getString("surname");
                    cellphoneNo = object.getString("cellphone_number");
                    occupation = object.getString("occupation");

                    // sets doctor name as initials plus surname (charAt(0))
                    tv_Doctor.setText(String.format("%s %s", firstName.charAt(0), surname));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientLastVisit.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("docID",docID);
                params.put("medReg",medReg);

                return params;
            }
        }
                ;
        Singleton.getInstance(DocPatientLastVisit.this).addToRequestQue(stringRequest);
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

