package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Diagnosis extends AppCompatActivity {

    String[] conditionNames, medicationNames;
    AutoCompleteTextView act_Medication;
    TextInputEditText etDiagnosis;
    TextView tvLatestSymptoms;
    Button btnViewAllSymptoms, btnSaveDiagnosis;

    String patientEmail = "", patientName = "", symptom = "", date_added = "", severity = "";
    String docEmail = "", medRegNo =  "", diagnosisDate = "";

    String URL_GETLSTSYMPTMS = "http://10.0.2.2/app/getpatientlastsymptomfordiagnosis.php";
    String URL_GETALLSYMPTMS = "http://10.0.2.2/app/getallpatientsymptomsfordiagnosis.php";
    String URL_ADDVISIT = "http://sict-iis.nmmu.ac.za/ibitech/app/insertvisit.php";
    String URL_ADD = "http://sict-iis.nmmu.ac.za/ibitech/app/addsymptom.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagnosis);

        SharedPreferences prefs = getSharedPreferences("DIAGNOSIS",MODE_PRIVATE);
        patientEmail = prefs.getString("pEmail", "");
        patientName = prefs.getString("pName", "");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        etDiagnosis = findViewById(R.id.etDiagnostic);
        tvLatestSymptoms = findViewById(R.id.txtLatestSymptoms);
        btnViewAllSymptoms = findViewById(R.id.btnViewAllSymptoms);
        btnSaveDiagnosis = findViewById(R.id.btnSaveDiagnosis);
        act_Medication = findViewById(R.id.actx_Medication);

      /*  conditionNames = getResources().getStringArray(R.array.conditions);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, conditionNames);
        act_Diagnosis.setAdapter(cAdapter);*/

        medicationNames = getResources().getStringArray(R.array.medication);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicationNames);
        act_Medication.setAdapter(mAdapter);

        //Log.i("tagconvertstr", "["+medRegNo+"]");

        btnViewAllSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetAllPatientSymptoms(patientEmail);
            }
        });

        btnSaveDiagnosis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String medicine = act_Medication.getText().toString();
                //addVisit(addDate, doctorID, medRegNo, symptomID, patientID, condition, medicine);
            }
        });

        GetPatientLastSymptoms(patientEmail);

    }

    private void GetAllPatientSymptoms(final String patientEmail) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETALLSYMPTMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    final JSONArray jsonArray = jsonObject.getJSONArray("server_response");


                    final String[] symptoms = new String[jsonArray.length()];
                    final String[] date_added = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        symptoms[i] = object.getString("symptom");
                        date_added[i] = object.getString("date_added");
                    }

                    // TODO -> SHOW DIALOG BOX WITH ALL PATIENT SYMPTOMS
                    ArrayList<Map<String, Object>> symptomList = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++){
                        Map<String, Object> map = new HashMap<>();
                        map.put("symptom", symptoms[i]);
                        map.put("date_added", date_added[i]);
                        symptomList.add(map);
                    }

                    SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(),symptomList, android.R.layout.simple_list_item_2, new String[]{"symptom", "date_added"},new int[]{android.R.id.text1,android.R.id.text2 });

                    ListView listView = new ListView(getApplicationContext());
                    listView.setAdapter(adapter);

                    AlertDialog.Builder builder = new AlertDialog.Builder(Diagnosis.this);
                    builder.setCancelable(true);
                    builder.setView(listView);
                    final  AlertDialog dialog = builder.create();
                    dialog.show();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "Note: This patient has no symptoms inserted. Add symptoms first.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"Error Listener Error" + error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("email",patientEmail);

                return params;
            }
        };

        Singleton.getInstance(Diagnosis.this).addToRequestQue(stringRequest);
    }


    public void GetPatientLastSymptoms(final String email){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETLSTSYMPTMS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);
                    symptom = object.getString("symptom");
                    date_added = object.getString("date_added");
                    severity = object.getString("severity");

                    tvLatestSymptoms.setText(symptom);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(Diagnosis.this, "Note: This patient has no symptoms inserted. Add symptoms first.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Diagnosis.this,"Error Listener Error" + error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("email",email);

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
