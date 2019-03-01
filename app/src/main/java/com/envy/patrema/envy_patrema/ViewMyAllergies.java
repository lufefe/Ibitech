package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.envy.patrema.envy_patrema.Adapter.AllergyListAdapter;
import com.envy.patrema.envy_patrema.Models.MyAllergiesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.divide.ibitech.divide_ibitech.Models.ConditionList;

public class ViewMyAllergies extends AppCompatActivity {

    SessionManager sessionManager;
    android.support.v7.widget.Toolbar toolbar;
    ListView lvAllergies;
    List<MyAllergiesList> allergyList;
    String emailAddress="";
    String URLGETALLRGY = "http://10.0.2.2/app/getmyallergies.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_my_allergies);

        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetails();
        emailAddress = user.get(SessionManager.EMAIL);

        lvAllergies = findViewById(R.id.lv_Allergies);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.tbViewAllergies);
        toolbar.setTitle("My Allergies");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        allergyList = new ArrayList<>();
        GetAllergies(emailAddress);
    }

    private void GetAllergies(final String emailAddress) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETALLRGY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");

                            //Parallel arrays for selected row

                            final String[] allergyID = new String[array.length()];
                            final String[] allergyName = new String[array.length()];
                            final String[] allergyType = new String[array.length()];
                            final String[] species = new String[array.length()];
                            final String[] dateAdded = new String[array.length()];
                            final String[] treatment = new String[array.length()];
                            final String[] tested = new String[array.length()];

                            for (int x = 0; x < array.length(); x++) {

                                JSONObject object = array.getJSONObject(x);

                                /*allergyID[x] = object.getString("allergy_id");
                                allergyName[x] = object.getString("allergy_name");
                                allergyType[x] = object.getString("allergy_type");
                                species[x] = object.getString("species");
                                dateAdded[x] = object.getString("date_added");
                                treatment[x] = object.getString("treatment_id");
                                tested[x] = object.getString("tested");*/

                                MyAllergiesList myAllergiesList = new MyAllergiesList(object.getString("allergy"), object.getString("date_added"), object.getString("tested"));
                                allergyList.add(myAllergiesList);

                            }

                            AllergyListAdapter adapter = new AllergyListAdapter(allergyList, ViewMyAllergies.this);
                            lvAllergies.setAdapter(adapter);
/*
                            lvAllergies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                                    for (int i = 0; i < allergyID.length; i++) {

                                        if (parent.getItemIdAtPosition(position) == i) {

                                            SharedPreferences preferences = getSharedPreferences("PATIENTALLERGY", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pAllergyID", allergyID[i]);
                                            editor.putString("pAllergyName", allergyName[i]);
                                            editor.putString("pAllergyType", allergyType[i]);
                                            editor.putString("pSpecies", species[i]);
                                            editor.putString("pDateAdded", dateAdded[i]);
                                            editor.putString("pTreatmentID", treatment[i]);
                                            editor.putString("pTested", tested[i]);
                                            editor.apply();
                                            startActivity(new Intent(ViewMyAllergies.this, PatientAllergy.class));
                                            finish();

                                        }
                                    }

                                }
                            });*/

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewMyAllergies.this, "You have no allergies recorded yet." + e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewMyAllergies.this, "There has been an error retrieving your allergies from the database, please try again later" + error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", emailAddress);
                return params;
            }

        };
        Singleton.getInstance(ViewMyAllergies.this).addToRequestQue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();

        if (i == android.R.id.home){
            startActivity(new Intent(getApplicationContext(), PatientMainActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


}