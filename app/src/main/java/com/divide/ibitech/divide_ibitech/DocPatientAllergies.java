package com.divide.ibitech.divide_ibitech;

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
import com.divide.ibitech.divide_ibitech.Adapter.AllergyListAdapter;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocPatientAllergies extends AppCompatActivity {

    List<AllergyList> alleList;
    ListView allegyListView;

    TextView tv_Error;
    ImageView iv_Error;
    android.support.v7.widget.Toolbar toolbar;
    String patientID = "",patientName="";

    String URLGETALLRGY = "http://sict-iis.nmmu.ac.za/ibitech/app/getallergy.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_patient_allergies);

        tv_Error = findViewById(R.id.tvNoAllergies);
        iv_Error = findViewById(R.id.ivAllergy);

        allegyListView=findViewById(R.id.listAllergy);

        //shared prefs for patient data
        SharedPreferences prefs = getSharedPreferences("PATIENT",MODE_PRIVATE);
        patientID = prefs.getString("pID", "");
        patientName = prefs.getString("pName","");

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(patientName + "\'s Allergies");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ShowList(patientID);

    }

    private void ShowList(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETALLRGY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");

                            //Parallel arrays
                            final String[] allergyID = new String[array.length()];
                            final String[] allergyName = new String[array.length()];
                            final String[] allergyType = new String[array.length()];
                            final String[] species = new String[array.length()];
                            final String[] dateAdded = new String[array.length()];
                            final String[] treatment = new String[array.length()];
                            final String[] tested = new String[array.length()];
                            alleList= new ArrayList<>();
                            for (int x= 0; x < array.length(); x++){
                                JSONObject allergyObject = array.getJSONObject(x);

                                allergyID[x] = allergyObject.getString("allergy_id");
                                allergyName[x] = allergyObject.getString("allergy_name");
                                allergyType[x] = allergyObject.getString("allergy_type");
                                species[x] = allergyObject.getString("species");
                                dateAdded[x] = allergyObject.getString("date_added");
                                treatment[x] = allergyObject.getString("treatment_id");
                                tested[x] = allergyObject.getString("tested");

                                AllergyList allergy = new AllergyList(allergyObject.getString("allergy_name"),
                                        allergyObject.getString("date_added"));
                                alleList.add(allergy);

                            }
                            allegyListView.setVisibility(View.VISIBLE);
                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
                            allegyListView.setAdapter(adapter);

                            allegyListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                                    for (int i =0; i < allergyID.length; i++){
                                        if (parent.getItemIdAtPosition(position) == i) {
                                            SharedPreferences preferences = getSharedPreferences("PATIENTALLERGY", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pAllergyID",allergyID[i]);
                                            editor.putString("pAllergyName", allergyName[i]);
                                            editor.putString("pAllergyType",allergyType[i]);
                                            editor.putString("pSpecies",species[i]);
                                            editor.putString("pDateAdded",dateAdded[i]);
                                            editor.putString("pTreatment",treatment[i]);
                                            editor.putString("pTested",tested[i]);
                                            editor.apply();
                                            startActivity(new Intent(DocPatientAllergies.this, PrescribeAllergyTreatment.class));
                                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }
                                    }

                                }
                            });


                        } catch (JSONException e) {
                            e.printStackTrace();
                            allegyListView.setVisibility(View.GONE);
                            iv_Error.setVisibility(View.VISIBLE);
                            tv_Error.setText(String.format("%s has no allergies recorded yet.", patientName));
                            tv_Error.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(DocPatientAllergies.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(DocPatientAllergies.this).addToRequestQue(stringRequest);
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
