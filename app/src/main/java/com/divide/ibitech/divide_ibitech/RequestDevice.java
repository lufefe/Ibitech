package com.divide.ibitech.divide_ibitech;



import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;import android.content.Intent;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

    String URL_REQUEST = "http://sict-iis.nmmu.ac.za/ibitech/app/requestdevice.php";

    String URL_GETDOCID = "http://sict-iis.nmmu.ac.za/ibitech/app/register.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_device);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetails();
        idNumber = user.get(sessionManager.ID);

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

        //Real-time validation
        autoDeviceName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(autoDeviceName.getText().length() > 0){
                    dValid = DeviceValidate();
                }
            }
        });


    }

    private void getDoctorID(final String practitionerName) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETDOCID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(RequestDevice.this, "Doc ID Found", Toast.LENGTH_LONG).show();
                        btnRequest.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                initiate();
                                requestDevice(idNumber, deviceName, practitionerName, requestDate);
                            }
                        });
                        finish();
                    }
                    else {
                        Toast.makeText(RequestDevice.this, "Doctor not found", Toast.LENGTH_LONG).show();
                    }

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


    private boolean DeviceValidate() {
        deviceName = autoDeviceName.getText().toString();
        dValid = false;
        if (deviceName.isEmpty()) {
            autoDeviceName.setError("Please enter a device name");
        }
        else {
            dValid = true;
        }
        return dValid;
    }

    private void requestDevice(final String idNumber, final String deviceName, final String practitionerName, final String requestDate) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REQUEST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(RequestDevice.this, "Device successfully requested", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RequestDevice.this,Dashboard.class));
                    }
                    else {
                        Toast.makeText(RequestDevice.this, "Request Failed, the seems to be an internal error.", Toast.LENGTH_LONG).show();
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

                params.put("id",idNumber);
                params.put("dName",deviceName);
                params.put("pName",practitionerName);
                params.put("date",requestDate);

                return params;
            }
        };

        Singleton.getInstance(RequestDevice.this).addToRequestQue(stringRequest);
    }

    public void initiate() {

        if(dValid && pValid){
            //*********Passing data to new variables************
            deviceName = autoDeviceName.getText().toString().trim();
            requestDate = tv_Date.getText().toString().trim();
            requestDevice(idNumber, deviceName,practitionerName,requestDate);
        }
        else {
            Toast.makeText(RequestDevice.this, "Make sure you have entered all details correctly. ", Toast.LENGTH_LONG).show();
        }

    }








}
