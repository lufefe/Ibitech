package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.ConditionListAdapter;
import com.divide.ibitech.divide_ibitech.Models.ConditionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocPatientConditions extends AppCompatActivity {

    List<ConditionList> condList;
    ListView listView;

    ImageView ivConds;
    TextView tvConds;

    android.support.v7.widget.Toolbar toolbar;
    String patientID = "",patientName="";

    String URLGETCONDS = "http://sict-iis.nmmu.ac.za/ibitech/app/getconditions.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_conditions);

        ivConds = findViewById(R.id.ivconditions);
        tvConds = findViewById(R.id.tvNoConditions);

        condList = new ArrayList<>();
        listView = findViewById(R.id.lvConditions);

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Conditions");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ShowList(patientID);
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

    private  void  ShowList(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETCONDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                            //Parallel arrays
                            final String[] visitID = new String[jsonArray.length()];
                            final String[] visitDate = new String[jsonArray.length()];
                            final String[] doctorID = new String[jsonArray.length()];
                            final String[] medicalRegNo = new String[jsonArray.length()];
                            final String[] symptomID = new String[jsonArray.length()];
                            final String[] conditionID = new String[jsonArray.length()];
                            final String[] medicineID = new String[jsonArray.length()];

                            for (int x= 0; x < jsonArray.length(); x++){
                                JSONObject condOBJ = jsonArray.getJSONObject(x);

                                visitID[x] = condOBJ.getString("visit_id");
                                visitDate[x] = condOBJ.getString("visit_date");
                                doctorID[x] = condOBJ.getString("doctor_id");
                                medicalRegNo[x] = condOBJ.getString("medical_reg_no");
                                symptomID[x] = condOBJ.getString("symptom_id");
                                conditionID[x] = condOBJ.getString("condition_id");
                                medicineID[x] = condOBJ.getString("medicine_id");


                                ConditionList conds = new ConditionList(condOBJ.getString("condition_name"),
                                        condOBJ.getString("visit_date"),
                                        condOBJ.getString("first_name"),
                                        condOBJ.getString("surname"));
                                condList.add(conds);

                            }
                            listView.setVisibility(View.VISIBLE);
                            ConditionListAdapter adapter= new ConditionListAdapter(condList,getApplication());
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                                    for (int i =0; i < conditionID.length; i++){
                                        if (parent.getItemIdAtPosition(position) == i) {
                                            SharedPreferences preferences = getSharedPreferences("PATIENTCONDITION", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pVisitID",visitID[i]);
                                            editor.putString("pVisitDate", visitDate[i]);
                                            editor.putString("pDoctorID", doctorID[i]);
                                            editor.putString("pMedicalRegNo", medicalRegNo[i]);
                                            editor.putString("pSymptomID", symptomID[i]);
                                            editor.putString("pConditionID", conditionID[i]);
                                            editor.putString("pMedicine", medicineID[i]);
                                            editor.apply();
                                            //startActivity(new Intent(DocPatientConditions.this, PatientCondition.class));
                                            //start activity of a class to view more details about condition
                                            //finish();
                                        }
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listView.setVisibility(View.GONE);
                            ivConds.setVisibility(View.VISIBLE);
                            tvConds.setText(patientName + " has no conditions recorded yet.");
                            tvConds.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientConditions.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
}
