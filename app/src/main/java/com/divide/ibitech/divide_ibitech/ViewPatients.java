package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.List;

public class ViewPatients extends AppCompatActivity {

    MaterialSearchView searchView;

    ListView listView;
    List<PatientsList> patientsList;
    String URL_GETPATIENTS = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatients.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_patients);

        listView = findViewById(R.id.lv_viewPatients);
        patientsList = new ArrayList<>();


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("All Patients");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        searchView = findViewById(R.id.search_view);


        GetPatients();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(ViewPatients.this,DocDashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
    private void GetPatients() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPATIENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    //Parallel arrays
                    final String [] patientID = new String[jsonArray.length()];
                    final String [] patientName = new String[jsonArray.length()];
                    final String [] patientSurname = new String[jsonArray.length()];
                    final String [] patientDOB = new String[jsonArray.length()];
                    final String [] patientGender = new String[jsonArray.length()];
                    final String [] patientStatus = new String[jsonArray.length()];
                    final String [] patientCell = new String[jsonArray.length()];
                    final String [] patientBloodType = new String[jsonArray.length()];
                    final String [] patientWeight = new String[jsonArray.length()];
                    final String [] patientHeight = new String[jsonArray.length()];
                    final String [] patientMedicalAid = new String[jsonArray.length()];


                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        patientID[i] = object.getString("patient_id");
                        patientName[i] = object.getString("first_name");
                        patientSurname[i] = object.getString("surname");
                        patientDOB[i] = object.getString("dob");
                        patientGender[i] = object.getString("sex");
                        patientStatus[i] = object.getString("marital_status");
                        patientCell[i] = object.getString("cellphone_number");
                        patientBloodType[i] = object.getString("blood_type");
                        patientWeight[i] = object.getString("weight");
                        patientHeight[i] = object.getString("height");
                        patientMedicalAid[i] = object.getString("medical_aid_id");


                        PatientsList patients = new PatientsList(object.getString("first_name"),object.getString("surname"), object.getString("dob") ,object.getString("cellphone_number"));
                        patientsList.add(patients);
                    }

                    DocPatientsAdapter adapter = new DocPatientsAdapter(getApplication(),patientsList);
                    listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            for (int i =0; i < patientID.length; i++){
                                if (parent.getItemIdAtPosition(position) == i)
                                {
                                    //Toast.makeText(ViewPatients.this, patientName[i], Toast.LENGTH_SHORT).show();
                                    SharedPreferences preferences = getSharedPreferences("PATIENT",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putString("pID", patientID[i]);
                                    editor.putString("pName", patientName[i] + " " + patientSurname[i]);
                                    editor.putString("pDOB", patientDOB[i]);
                                    editor.putString("pGender", patientGender[i]);
                                    editor.putString("pStatus", patientStatus[i]);
                                    editor.putString("pCell", patientCell[i]);
                                    editor.putString("pBloodType", patientBloodType[i]);
                                    editor.putString("pWeight", patientWeight[i]);
                                    editor.putString("pHeight", patientHeight[i]);
                                    editor.putString("pMedicalAid", patientMedicalAid[i]);
                                    editor.apply();
                                    startActivity(new Intent(ViewPatients.this, PatientMedicalRecord.class));
                                    finish();
                                }

                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewPatients.this,"There are no patients in our database yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewPatients.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(ViewPatients.this).addToRequestQue(stringRequest);

    }
}
