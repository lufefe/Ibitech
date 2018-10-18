package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
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

import java.util.HashMap;
import java.util.Map;

public class DocPatientLastVisit extends AppCompatActivity {

    TextView tv_VisitDate, tv_Doctor, tv_Symptoms, tv_Diagnosis, tv_Medication, tv_Notes, tvNoVisits;
    ImageView ivNoVisits;
    ConstraintLayout clVisits;

    android.support.v7.widget.Toolbar toolbar;
    String patientID="", patientName="";
    String visitDate="",doctorName="", symptoms="",diagnosis="",medication="",notes="";


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

        clVisits = findViewById(R.id.clLastVisit);

        tvNoVisits = findViewById(R.id.tvNoVisits);
        ivNoVisits = findViewById(R.id.ivVisits);

        tv_VisitDate = findViewById(R.id.tvVisitDate);
        tv_Doctor = findViewById(R.id.tvDoctor);
        tv_Symptoms = findViewById(R.id.tvSymptoms);
        tv_Diagnosis = findViewById(R.id.tvDiagnosis);
        tv_Medication = findViewById(R.id.tvMedication);
        tv_Notes = findViewById(R.id.tvNotes);


        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientName = prefs.getString("pName","");
        patientID = prefs.getString("pID","");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Last Visit");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ShowLastVisit();

    }


    private void ShowLastVisit() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTVST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    String visitID="", visitDate="",docID="",medReg="",symptomID="",patID="",condID="",medicineID="";

                    visitID = object.getString("visit_id");
                    visitDate = object.getString("visit_date");
                    docID = object.getString("doctor_id");
                    medReg = object.getString("medical_reg_no");
                    symptomID = object.getString("symptom_id");
                    patID = object.getString("patient_id"); //can be opted out
                    condID = object.getString("condition_id");
                    medicineID = object.getString("medicine_id");

                    clVisits.setVisibility(View.VISIBLE);

                    tv_VisitDate.setText(visitDate);

                    getLastVisitDoctor(docID,medReg);
                    getLastVisitSymptoms(symptomID);
                    getLastVisitCondition(condID);
                    getLastVisitMedication(medicineID);
                    getLastVisitNotes(visitID,visitDate);


                } catch (JSONException e) {
                    e.printStackTrace();
                    clVisits.setVisibility(View.GONE);
                    ivNoVisits.setVisibility(View.VISIBLE);
                    tvNoVisits.setText(patientName + " has no visits recorded yet.");
                    tvNoVisits.setVisibility(View.VISIBLE);
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
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",patientID);

                return params;
            }
        };
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

                    if (description.isEmpty())
                        tv_Notes.setText("No notes available.");
                    else
                        tv_Notes.setText(description);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,patientName + " has no notes recorded yet.",Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {
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

                    medicine = object.getString("description");

                    tv_Medication.setText(medicine);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"Couldn't retrieve medication.",Toast.LENGTH_LONG).show();
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
                    Toast.makeText(DocPatientLastVisit.this,"Couldn't retrieve condition.",Toast.LENGTH_LONG).show();
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
            protected Map<String, String> getParams() {
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

                    tv_Symptoms.setText(symptomName);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"Couldn't retrieve symptoms.",Toast.LENGTH_LONG).show();
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

                    tv_Doctor.setText(String.format("%s %s", firstName.charAt(0), surname));

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(DocPatientLastVisit.this,"Doctor retrieve failed.",Toast.LENGTH_LONG).show();
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
}

