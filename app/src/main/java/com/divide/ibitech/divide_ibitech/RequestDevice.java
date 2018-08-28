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


    private EditText et_PractitionerName;
    AutoCompleteTextView autoDeviceName;
    String[] deviceNames;

    String deviceName, practitionerName, requestDate,idNumber;
    TextView tv_Date, btnRequest,btnViewDevices;
    public String subject, message,to;
    String [] names = {"Request Device"};
    String [] status = {"Device Requested"};

    SessionManager sessionManager;
    String URL_REQUEST = "http://sict-iis.nmmu.ac.za/ibitech/app/requestdevice.php";
    Boolean dValid = false, pValid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_device);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetails();
        idNumber = user.get(sessionManager.ID);

        autoDeviceName = findViewById(R.id.autoDeviceName);
        deviceNames = getResources().getStringArray(R.array.devices);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,deviceNames);
        autoDeviceName.setAdapter(adapt);


        message = "You have Successfully requested medical device \n You can see the status of Device on the Ibitech app";
        subject = "@NoReply";
        to = "slabiti181@gmail.com";

        et_PractitionerName = findViewById(R.id.etPractitioner);
        tv_Date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));

        //btnViewDevices= findViewById(R.id.ViewDevices);

        btnRequest = findViewById(R.id.btnRequest);

        btnRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //everything is filled out
                //send email
                new SimpleMail().sendEmail(to, subject, message);
                initiate();
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

        et_PractitionerName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_PractitionerName.getText().length() > 0){
                    pValid = PractitionerValidate();
                }
            }
        });

    }

    private boolean PractitionerValidate() {
        practitionerName = et_PractitionerName.getText().toString();
        pValid =false;
        if (practitionerName.isEmpty()) {
            et_PractitionerName.setError("Please enter a practitioner");
        }
        else {
            pValid = true;
        }
        return pValid;
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
            practitionerName = et_PractitionerName.getText().toString().trim();
            requestDate = tv_Date.getText().toString().trim();
            requestDevice(idNumber, deviceName,practitionerName,requestDate);
        }
        else {
            Toast.makeText(RequestDevice.this, "Make sure you have entered all details correctly. ", Toast.LENGTH_LONG).show();
        }

    }








}
