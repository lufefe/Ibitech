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
import com.envy.patrema.envy_patrema.Adapter.SymptomListAdapter;
import com.envy.patrema.envy_patrema.Models.SymptomList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewSymptom extends AppCompatActivity {

    SessionManager sessionManager;
    android.support.v7.widget.Toolbar toolbar;
    ListView lvSymptoms;
    String URLGETSYMPTS = "http://10.0.2.2/app/getmysymptoms.php";
    List <SymptomList> symptomList;
    String emailAddress= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptom);

        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetails();
        emailAddress = user.get(SessionManager.EMAIL);

        lvSymptoms = findViewById(R.id.lv_Symptoms);

        toolbar = findViewById(R.id.tbViewSymptoms);
        toolbar.setTitle("My Symptoms");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        symptomList = new ArrayList<>();
        GetSymptoms(emailAddress);
    }

    private void GetSymptoms(final String emailAddress) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETSYMPTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                            for (int i = 0; i < jsonArray.length(); i++){

                                JSONObject object = jsonArray.getJSONObject(i);

                                SymptomList symptoms = new SymptomList(object.getString("symptom"),
                                        object.getString("date_added"), object.getString("severity"));
                                symptomList.add(symptoms);
                            }

                            SymptomListAdapter sAdapter = new SymptomListAdapter(symptomList, ViewSymptom.this);
                            lvSymptoms.setAdapter(sAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewSymptom.this,"You have no symptoms inserted yet.",Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewSymptom.this,"There was an error retrieving your symptoms from our database, please try again later."+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();

                params.put("email",emailAddress);
                return params;
            }
        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
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