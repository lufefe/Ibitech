package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddMedicalAid extends AppCompatActivity {

    AutoCompleteTextView actx_MedicalAid;
    String[] medicalAidNames;

    Button btnSave;

    String patientID = "", medicalAid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_medical_aid);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add Medical Aid");
        setSupportActionBar(toolbar);

        actx_MedicalAid = findViewById(R.id.actx_MedicalAid);

        medicalAidNames = getResources().getStringArray(R.array.medicalAid);
        ArrayAdapter<String> mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, medicalAidNames);
        actx_MedicalAid.setAdapter(mAdapter);

        btnSave = findViewById(R.id.btnAddMedAid);

        patientID = prefs.getString("pID","");

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicalAid = actx_MedicalAid.getText().toString();
                UpdateMedicalAid(patientID, medicalAid);
            }
        });

    }

    private void UpdateMedicalAid(final String patientID,final String medicalAid) {

        String URL_UPDATEMED = "http://sict-iis.nmmu.ac.za/ibitech/app/updatemedicalaid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATEMED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(AddMedicalAid.this,"Medical aid successfully saved.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AddMedicalAid.this,"There was a problem saving your medical aid, try again later." + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(AddMedicalAid.this,"There was an internal error, please try again later." + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",patientID);
                params.put("medicalAidName",medicalAid);
                return params;
            }
        };
        Singleton.getInstance(AddMedicalAid.this).addToRequestQue(stringRequest);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
