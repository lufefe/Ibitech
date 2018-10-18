package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.ConditionListAdapter;
import com.divide.ibitech.divide_ibitech.Adapter.MedicalDevicesAdapter;
import com.divide.ibitech.divide_ibitech.Models.ConditionList;
import com.divide.ibitech.divide_ibitech.Models.MedicalDevicesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewMedicalDevices extends AppCompatActivity {

    ListView listView;

    String id = "", fullname = "";

    List<MedicalDevicesList> deviceList;
    String URLGETDVCS = "http://sict-iis.nmmu.ac.za/ibitech/app/getdevices.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_devices);

        listView= findViewById(R.id.listDevices);
        deviceList = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        fullname = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","") ;
        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Medical Devices");
        setSupportActionBar(toolbar);
        
        ShowList(id);
    }

    private void ShowList(final String id) {
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


                                MedicalDevicesList medsDev = new MedicalDevicesList(object.getString("medical_device_name"), object.getString("date_requested"), object.getString("status"));
                                deviceList.add(medsDev);

                            }
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
                                            startActivity(new Intent(ViewMedicalDevices.this, MedicalDevice.class));
                                        }
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewMedicalDevices.this,"You have no medical devices inserted yet.", Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewMedicalDevices.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
