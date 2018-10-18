package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.DocPatientMedicationAdapter;
import com.divide.ibitech.divide_ibitech.Models.DocPatientMedicationList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocPatientMedication extends AppCompatActivity {

    ListView listView;
    List<DocPatientMedicationList> medicationListView;

    TextView tvNoMeds;
    ImageView ivMeds;

    String patientID = "", patientName="";

    String URLGETMEDS = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientmedication.php";

    android.support.v7.widget.Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_medication);

        tvNoMeds = findViewById(R.id.tvNoMeds);
        ivMeds = findViewById(R.id.ivMedication);
        listView = findViewById(R.id.lvMedication);
        medicationListView = new ArrayList<>();

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientName = prefs.getString("pName","");

        SharedPreferences preferences = getSharedPreferences("PATIENT",MODE_PRIVATE);

        patientID = preferences.getString("pID", "");
        patientName = preferences.getString("pName", "");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Medication");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ShowList(patientID);
    }

    public void ShowList(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETMEDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");

                            //Parallel arrays
                            final String[] medicineID = new String[array.length()];
                            final String[] description = new String[array.length()];
                            final String[] visitID = new String[array.length()];
                            final String[] visitDate = new String[array.length()];
                            final String[] docID = new String[array.length()];
                            final String[] medReg = new String[array.length()];
                            final String[] condID = new String[array.length()];

                            for (int x= 0; x < array.length(); x++){
                                JSONObject medsObject = array.getJSONObject(x);

                                //only description, date, and doctor will be display, other will be used for future enhancements
                                medicineID[x] = medsObject.getString("medicine_id");
                                description[x] = medsObject.getString("description");
                                visitID[x] = medsObject.getString("visit_id");
                                visitDate[x] = medsObject.getString("visit_date");
                                docID[x] = medsObject.getString("doctor_id");
                                medReg[x] = medsObject.getString("medical_reg_no");
                                condID[x] = medsObject.getString("condition_id");

                                DocPatientMedicationList meds = new DocPatientMedicationList(medsObject.getString("description"), medsObject.getString("visit_date"), "Dr " + medsObject.getString("first_name") + " " + medsObject.getString("surname"));
                                medicationListView.add(meds);

                            }

                            listView.setVisibility(View.VISIBLE);
                            DocPatientMedicationAdapter adapter =  new DocPatientMedicationAdapter(medicationListView,getApplication());
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();

                            listView.setVisibility(View.GONE);
                            ivMeds.setVisibility(View.VISIBLE);
                            tvNoMeds.setText(patientName + " has no medication recorded yet.");
                            tvNoMeds.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DocPatientMedication.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(DocPatientMedication.this).addToRequestQue(stringRequest);
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
