package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.envy.patrema.envy_patrema.Adapter.DocPatientVisitsAdapter;
import com.envy.patrema.envy_patrema.Models.CreateVisitList;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewPatientVisits extends AppCompatActivity {

    MaterialSearchView searchView;

    ListView lvVisits;
    List<CreateVisitList> visitsLists;
    //String URL_GETAPPTS = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientvisits.php";
    String URL_GETPATIENTS = "http://10.0.2.2/app/getpatients.php";

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_patient_visit);

        lvVisits = findViewById(R.id.lv_Patients);
        visitsLists = new ArrayList<>();

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Select a patient");
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"));
        setSupportActionBar(toolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView = findViewById(R.id.search_view);

        GetPatients();

    }

    private void GetPatients() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPATIENTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    final JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    //Parallel arrays
                    final String [] patientID = new String[jsonArray.length()];
                    final String [] patientName = new String[jsonArray.length()];
                    final String [] patientSurname = new String[jsonArray.length()];

                    //for searching

                    final String [] patientFullName = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++){
                        JSONObject object = jsonArray.getJSONObject(i);
                        patientFullName[i] = object.getString("first_name") + " " + object.getString("surname");
                    }

                    searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
                        @Override
                        public boolean onQueryTextSubmit(String query) {
                            return false;
                        }

                        @Override
                        public boolean onQueryTextChange(String newText) {
                            if ((newText != null) && !newText.isEmpty()){
                                List<CreateVisitList> lstFound = new ArrayList<>();
                                for (int i = 0; i < patientFullName.length; i++){
                                    JSONObject object = null;
                                    try {
                                        object = jsonArray.getJSONObject(i);
                                    } catch (JSONException e){
                                        e.printStackTrace();
                                    }
                                    if (patientFullName[i].toLowerCase().contains(newText.toLowerCase())){
                                        CreateVisitList patients = null;
                                        try{
                                            assert object != null;
                                            patients = new CreateVisitList(object.getString("first_name"),object.getString("surname"), object.getString("id_number"));
                                        } catch (JSONException e){
                                            e.printStackTrace();
                                        }
                                        lstFound.add(patients);
                                    }
                                }
                                DocPatientVisitsAdapter adapter = new DocPatientVisitsAdapter(lstFound, ViewPatientVisits.this);
                                lvVisits.setAdapter(adapter);

                            }
                            else {
                                DocPatientVisitsAdapter adapter = new DocPatientVisitsAdapter(visitsLists, ViewPatientVisits.this);
                                lvVisits.setAdapter(adapter);
                            }
                            return true;
                        }
                    });

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                        patientID[i] = object.getString("id_number");
                        patientName[i] = object.getString("first_name");
                        patientSurname[i] = object.getString("surname");



                        CreateVisitList appts = new CreateVisitList(object.getString("first_name"),object.getString("surname"), object.getString("id_number"));
                        visitsLists.add(appts);
                    }

                    DocPatientVisitsAdapter adapter = new DocPatientVisitsAdapter(visitsLists, ViewPatientVisits.this);
                    lvVisits.setAdapter(adapter);

                    searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
                        @Override
                        public void onSearchViewShown() {

                        }

                        @Override
                        public void onSearchViewClosed() {
                            DocPatientVisitsAdapter adapter = new DocPatientVisitsAdapter(visitsLists, ViewPatientVisits.this);
                            lvVisits.setAdapter(adapter);
                        }
                    });


                    lvVisits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //String appt = String.valueOf(parent.getItemIdAtPosition(position));

                            for (int i =0; i < patientID.length; i++){
                                if (parent.getItemIdAtPosition(position) == i)
                                {
                                    //Toast.makeText(ViewPatientVisits.this, patientName[i], Toast.LENGTH_SHORT).show();
                                    SharedPreferences preferences = getSharedPreferences("DIAGNOSIS",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putString("pID", patientID[i]);
                                    editor.putString("pName", patientName[i] + " " + patientSurname[i]);
                                    editor.apply();
                                    startActivity(new Intent(ViewPatientVisits.this, Diagnosis.class));
                                }

                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewPatientVisits.this,"There are no patients yet.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewPatientVisits.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(ViewPatientVisits.this).addToRequestQue(stringRequest);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void finish() {
        startActivity(new Intent(ViewPatientVisits.this, DoctorDashboard.class));
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
