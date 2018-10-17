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

                                //sets shared preferences for last visit
                                SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("pVisitDate", visitDate);
                                editor.apply();

                                getLastVisitDoctor(docID,medReg);
                                getLastVisitSymptoms(symptomID);
                                getLastVisitCondition(condID);
                                getLastVisitMedication(medicineID);
                                getLastVisitNotes(visitID,visitDate);

                                Intent intent = new Intent(PatientMedicalRecord.this, DocPatientLastVisit.class);
                                startActivity(intent);
                                finish();
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(PatientMedicalRecord.this,patientName + " has no visits recorded.",Toast.LENGTH_LONG).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
                    Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);
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


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Medical Record" );
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

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

                    //sets shared preferences for last visit
                    SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pNotes", description);
                    editor.putString("pNoteDateAdded", dateAdded);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientMedicalRecord.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);


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

                    //sets shared preferences for last visit
                    SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pMedication", medicine);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientMedicalRecord.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);
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

                    //sets shared preferences for last visit
                    SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pDiagnosis", diagnosis);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientMedicalRecord.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);
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

                    //sets shared preferences for last visit
                    SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("pSymptomName", symptomName);
                    editor.putString("pSymptomDateAdded", dateAdded);
                    editor.apply();


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientMedicalRecord.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);
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

                    //sets shared preferences for last visit doctor
                    SharedPreferences preferences = getSharedPreferences("LASTVISIT", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    // sets doctor name as initials plus surname (charAt(0))
                    editor.putString("pDoctor", String.format("%s %s", firstName.charAt(0), surname));
                    editor.putString("pDocCell", cellphoneNo);
                    editor.putString("pOccupation", occupation);
                    editor.apply();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PatientMedicalRecord.this,"Doctor retrieve failed.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PatientMedicalRecord.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(PatientMedicalRecord.this).addToRequestQue(stringRequest);
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
