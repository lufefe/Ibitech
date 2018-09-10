package com.divide.ibitech.divide_ibitech;



import android.content.SharedPreferences;
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

import org.json.JSONArray;
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
    AutoCompleteTextView autoDeviceName,auto_PractitionerName;
    String[] deviceNames,doctorNames;

    String deviceName, practitionerName, requestDate,idNumber;
    TextView tv_Date, btnRequest,btnViewDevices;
    public String subject, message,to;
    String [] names = {"Request Device"};
    final String status = "Device Requested";
String doctorID="7006295261082";
    String medRegNo="BAA0069221";


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
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();



        String value = preferences.getString("Fullname", "");
     final   String patientID= preferences.getString("pID","");

        autoDeviceName = findViewById(R.id.autoDeviceName);
        deviceNames = getResources().getStringArray(R.array.devices);
        ArrayAdapter<String> adapt = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,deviceNames);
        autoDeviceName.setAdapter(adapt);


        message = "You have Successfully requested medical device \n You can see the status of Device on the Ibitech app";
        subject = "@NoReply";
        to = "slabiti181@gmail.com";

        auto_PractitionerName = findViewById(R.id.etPractitioner);
        doctorNames=getResources().getStringArray(R.array.doctorNames);
        ArrayAdapter<String> adapterD= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,doctorNames);
        auto_PractitionerName.setAdapter(adapterD);

        tv_Date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));

        //btnViewDevices= findViewById(R.id.ViewDevices);

        btnRequest = findViewById(R.id.btnRequest);
btnRequest.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        final  String  device =autoDeviceName.getText().toString();
        final String docname= auto_PractitionerName.getText().toString();


        final String date= tv_Date.getText().toString();
      //  final  String practitionerID=doctorID.toString();
        //final  String practitionerRegNo=medRegNo.toString();
        if( docname=="Dr E.Jonas") {
            final String practitionerID = doctorID.toString();
            final String practitionerRegNo = medRegNo.toString();

            if (device.isEmpty() || docname.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Please ensure all fields are entered.", Toast.LENGTH_SHORT).show();


            } else {
                RequestDevice(practitionerRegNo, practitionerID, patientID, device, date, status);

            }
        }
    }
});

       // Singleton.getInstance(RequestDevice.this).addToRequestQue(stringRequest);

    }
    public  void RequestDevice (final  String practitionerRegNo,final  String practitionerID,final  String patientID ,final String  deviceID,final String  date,final  String status ){
StringRequest stringRequest = new StringRequest(Request.Method.POST, "",
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject = jsonArray.getJSONObject(0);
                    String code = jsonObject.getString("code");
                    String message = jsonObject.getString("message");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast toast = Toast.makeText(RequestDevice.this, response, Toast.LENGTH_LONG);
                toast.show();

            }
        }, new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(RequestDevice.this,"Error : There was an internal error with the response from our server, try again later.",Toast.LENGTH_LONG).show();

    }
}){
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String,String>paramas= new HashMap<String, String>();
        paramas.put("doctor_id",practitionerID);
        paramas.put("medical_reg_no",practitionerRegNo);
        paramas.put("patient_id",patientID);
        paramas.put("medical_device_id",deviceID);
        paramas.put("date",date);
        paramas.put("status",status);



        return  paramas;
    }
};
        Singleton.getInstance(RequestDevice.this).addToRequestQue(stringRequest);

    }









}


