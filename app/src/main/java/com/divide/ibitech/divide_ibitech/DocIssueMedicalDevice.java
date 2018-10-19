package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class DocIssueMedicalDevice extends AppCompatActivity {

    TextView tvPatientName, tvDateRequested, tvDateIssued,etInstructions;
    Button btnIssueDevice;
    String patientID = "",patientName="";
    String deviceID = "", deviceName="", requestDate="", issueDate="", instructions="", status="";
    String docID="",medRegNo="";

    String URL_ISSUE = "http://sict-iis.nmmu.ac.za/ibitech/app/issuemedicaldevice.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_issue_medical_device);

        sessionManager = new SessionManager(this);

        HashMap<String,String> doc = sessionManager.getDocDetails();
        docID = doc.get(sessionManager.ID);
        medRegNo = doc.get(sessionManager.MEDREGNO);

        tvPatientName = findViewById(R.id.tvPatientName);
        tvDateRequested = findViewById(R.id.tvDateRequested);
        tvDateIssued = findViewById(R.id.tvDateIssued);
        etInstructions = findViewById(R.id.etInstructions);

        btnIssueDevice = findViewById(R.id.btnIssueDevice);

        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        SharedPreferences preferences = getSharedPreferences("PATIENTDEVICES", MODE_PRIVATE);

        deviceID = preferences.getString("pDeviceID", "");
        deviceName = preferences.getString("pDeviceName", "");
        requestDate = preferences.getString("pRequestDate", "");
        issueDate = preferences.getString("pReceiveDate", "");
        instructions = preferences.getString("pInstructions", "");
        status = preferences.getString("pStatus", "");

        tvPatientName.setText(patientName);
        tvDateRequested.setText(requestDate);

        if (issueDate.equals("null"))
            tvDateIssued.setText("Not issued");
        else{
            tvDateIssued.setText(issueDate);
            btnIssueDevice.setVisibility(View.GONE);
        }

        etInstructions.setText(instructions);


        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(deviceName);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        btnIssueDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ISSUE, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if (success.equals("1")) {
                                Toast.makeText(DocIssueMedicalDevice.this, "Device successfully issued.", Toast.LENGTH_LONG).show();
                                finish();
                            }
                            else {
                                Toast.makeText(DocIssueMedicalDevice.this, "Device Issue Failed, you have already issued this device.", Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DocIssueMedicalDevice.this, "Issue Error" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DocIssueMedicalDevice.this,"2Issue Error"+error.toString(),Toast.LENGTH_LONG).show();

                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String,String> params = new HashMap<>();

                        params.put("docID",docID);
                        params.put("medRegNo",medRegNo);
                        params.put("patID",patientID);
                        params.put("medDevID", deviceID);

                        return params;
                    }
                };

                Singleton.getInstance(DocIssueMedicalDevice.this).addToRequestQue(stringRequest);
            }
        });

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
