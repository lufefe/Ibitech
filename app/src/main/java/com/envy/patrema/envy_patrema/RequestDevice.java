package com.envy.patrema.envy_patrema;



import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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


public class RequestDevice extends AppCompatActivity {

    AutoCompleteTextView autoDeviceName;
    String[] deviceNames;

    Spinner sp_Doctors;

    String deviceName, practitionerName, requestDate,idNumber;
    TextView tv_Date;
    Boolean dValid = false, pValid = false;

    Button btnRequest, btnCancel;

    SessionManager sessionManager;

//    String URL_REQUEST = "http://sict-iis.nmmu.ac.za/ibitech/app/requestdevice.php";
    String URL_REQUEST = "http://10.0.2.2/app/requestdevice.php";

    String URL_GETDOCID = "http://sict-iis.nmmu.ac.za/ibitech/app/getdoctorid.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_device);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetails();
        idNumber = user.get(SessionManager.ID);

        //for loading spinner/drop-down
        sp_Doctors = findViewById(R.id.spDoctor);
        ArrayAdapter<CharSequence> docAdapter = ArrayAdapter.createFromResource(this, R.array.doctors, R.layout.support_simple_spinner_dropdown_item);
        docAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Doctors.setAdapter(docAdapter);

        sp_Doctors.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                practitionerName = parent.getItemAtPosition(position).toString();
                // extract doctor name for getting their id and reg no
                String[] splitted = practitionerName.split("\\s+");
                String name = splitted[1] + " " + splitted[2];
                //Log.d("RequestDevice",name );
                getDoctorID(name);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        // for autocomplete text field
        autoDeviceName = findViewById(R.id.autoDeviceName);
        deviceNames = getResources().getStringArray(R.array.devices);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,deviceNames);
        autoDeviceName.setAdapter(adapt);

        tv_Date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));


        btnRequest = findViewById(R.id.btnRequest);


        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private void getDoctorID(final String practitionerName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETDOCID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                        JSONObject object = jsonArray.getJSONObject(0);
                        final String doctorId = object.getString("doctor_id");
                        final String medRegNo = object.getString("medical_reg_no");
                        btnRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deviceName = autoDeviceName.getText().toString().trim();
                                requestDate = tv_Date.getText().toString().trim();
                                if (deviceName.isEmpty()){
                                    Toast.makeText(getApplicationContext(), "Make sure all fields are entered.", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    requestDevice(doctorId, medRegNo, idNumber, requestDate, deviceName);
                                }

                            }
                        });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RequestDevice.this, "1Register Error" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestDevice.this,"2Register Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("name",practitionerName);

                return params;
            }
        };

        Singleton.getInstance(RequestDevice.this).addToRequestQue(stringRequest);


    }


    private void requestDevice(final String docID, final String medRegNo, final String patientID, final String requestDate, final String deviceName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(RequestDevice.this, "Device successfully requested", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(RequestDevice.this, "Request Failed, you have already requested this device.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(RequestDevice.this, "Request Error" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(RequestDevice.this,"2Request Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("docID",docID);
                params.put("medRegNo",medRegNo);
                params.put("patID",patientID);
                params.put("date",requestDate);
                params.put("deviceName", deviceName);

                return params;
            }
        };

        Singleton.getInstance(RequestDevice.this).addToRequestQue(stringRequest);
    }




}
