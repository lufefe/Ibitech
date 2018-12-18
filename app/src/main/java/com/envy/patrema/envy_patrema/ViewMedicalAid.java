package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ViewMedicalAid extends AppCompatActivity {

    String URL_GETMEDAID = "http://10.0.2.2/app/getmedicalaid.php";

    TextView tvMedAidName, tvRegDate, tvMedAidType, tvMedAidCell;
    String patientID;
    Button btnUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_medical_aid);

        tvMedAidName = findViewById(R.id.tvMedicalAidName);
        tvMedAidType = findViewById(R.id.tvMedAidType);
        tvRegDate = findViewById(R.id.tvRegDate);
        tvMedAidCell = findViewById(R.id.tvMedAidNo);
        btnUpdate = findViewById(R.id.btnUpdate);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), AddMedicalAid.class));
                finish();
            }
        });

        SharedPreferences proPrefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        patientID = proPrefs.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Medical Aid details");
        setSupportActionBar(toolbar);

        getMedicalAid(patientID);
    }

    private void getMedicalAid(final String patientID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETMEDAID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    String medAidName, medAidType,medAidRegDate,medAidCell;

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    JSONObject object = jsonArray.getJSONObject(0);

                    medAidName = object.getString("medical_aid_name");
                    medAidType = object.getString("medical_aid_type");
                    medAidRegDate = object.getString("registration_date");
                    medAidCell = object.getString("phone_number");

                    tvMedAidName.setText(medAidName);
                    tvMedAidType.setText(medAidType);
                    tvRegDate.setText(medAidRegDate);
                    tvMedAidCell.setText(medAidCell);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewMedicalAid.this,"Couldn't retrieve medical aid details.",Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewMedicalAid.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })

        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("patID",patientID);

                return params;
            }
        }
                ;
        Singleton.getInstance(ViewMedicalAid.this).addToRequestQue(stringRequest);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            //startActivity(new Intent(ViewMedicalAid.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
