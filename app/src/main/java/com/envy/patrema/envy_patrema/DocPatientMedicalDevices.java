package com.envy.patrema.envy_patrema;

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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.envy.patrema.envy_patrema.Adapter.MedicalDevicesAdapter;
import com.envy.patrema.envy_patrema.Models.MedicalDevicesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocPatientMedicalDevices extends AppCompatActivity {

    ListView listView;
    List<MedicalDevicesList> deviceList;
    String patientID = "",patientName="";
    String docID="", medRegNo="";

    ImageView ivDevices;
    TextView tvDevices;

    String URLGETDVCS = "http://sict-iis.nmmu.ac.za/ibitech/app/getdevices.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_devices);

        sessionManager = new SessionManager(this);

        HashMap<String,String> doc = sessionManager.getDocDetails();
        docID = doc.get(sessionManager.ID);
        medRegNo = doc.get(sessionManager.MEDREGNO);

        ivDevices = findViewById(R.id.ivDevices);
        tvDevices = findViewById(R.id.tvDevices);

        listView= findViewById(R.id.lvDevices);
        deviceList = new ArrayList<>();

        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Medical Devices");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ShowList(patientID, docID, medRegNo);
    }

    private void ShowList(final String id, final String docID, final String medRegNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETDVCS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");


                            //Parallel arrays
                            final String[] deviceID = new String[jsonArray.length()];
                            final String[] deviceName = new String[jsonArray.length()];
                            final String[] requestDate = new String[jsonArray.length()];
                            final String[] receiveDate = new String[jsonArray.length()];
                            final String[] medicalRegNo = new String[jsonArray.length()];
                            final String[] doctorID = new String[jsonArray.length()];
                            final String[] instruction = new String[jsonArray.length()];
                            final String[] status = new String[jsonArray.length()];

                            for (int x= 0; x < jsonArray.length(); x++){
                                JSONObject object = jsonArray.getJSONObject(x);

                                deviceID[x] = object.getString("medical_device_id");
                                deviceName[x] = object.getString("medical_device_name");
                                requestDate[x] = object.getString("date_requested");
                                receiveDate[x] = object.getString("date_received");
                                medicalRegNo[x] = object.getString("medical_reg_no");
                                doctorID[x] = object.getString("doctor_id");
                                instruction[x] = object.getString("use_instructions");
                                status[x] = object.getString("status");


                                MedicalDevicesList medsDev = new MedicalDevicesList(object.getString("medical_device_name"), object.getString("doctor_id"), object.getString("status"));
                                deviceList.add(medsDev);

                            }
                            listView.setVisibility(View.VISIBLE);
                            MedicalDevicesAdapter adapter= new MedicalDevicesAdapter(deviceList, getApplication());
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                                    for (int i =0; i < deviceID.length; i++){
                                        if (parent.getItemIdAtPosition(position) == i) {
                                            SharedPreferences preferences = getSharedPreferences("PATIENTDEVICES", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pDeviceID",deviceID[i]);
                                            editor.putString("pDeviceName", deviceName[i]);
                                            editor.putString("pRequestDate", requestDate[i]);
                                            editor.putString("pMedicalRegNo", medicalRegNo[i]);
                                            editor.putString("pReceiveDate", receiveDate[i]);
                                            editor.putString("pDoctorID", doctorID[i]);
                                            editor.putString("pInstructions", instruction[i]);
                                            editor.putString("pStatus", status[i]);
                                            editor.apply();
                                            startActivity(new Intent(DocPatientMedicalDevices.this, DocIssueMedicalDevice.class));
                                            finish();
                                        }
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            listView.setVisibility(View.GONE);
                            ivDevices.setVisibility(View.VISIBLE);
                            tvDevices.setText(patientName + " has no medical devices recorded yet.");
                            tvDevices.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DocPatientMedicalDevices.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();

                params.put("id",id);
                params.put("docID",docID);
                params.put("medRegNo",medRegNo);
                return params;
            }

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
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
