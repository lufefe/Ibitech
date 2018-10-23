package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

public class AddNextOfKin extends AppCompatActivity {
    TextView KinID,KinName,KinSname,KinCell,KinEmail,KinAddress,KinRelation,KinDate,Kinststus;
    Button btnSave;
    String idNo = "";
    String  kID,kname,kSname,kDate,kCell,kEmail,kAddress,kRelation ,kStatus;
    String URL="http://sict-iis.nmmu.ac.za/ibitech/app/new.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_next_of_kin);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);
        idNo = prefs.getString("pID","");
        KinID =(TextView) findViewById(R.id.kinId);
        KinName =(TextView) findViewById(R.id.Fname);
        KinSname =(TextView) findViewById(R.id.KinSname);
        KinCell =(TextView) findViewById(R.id.KinNo);
        KinEmail =(TextView) findViewById(R.id.KinEmail);
        KinAddress =(TextView) findViewById(R.id.KinAddress);
        KinRelation =(TextView) findViewById(R.id.KinRelation);
        KinDate =(TextView) findViewById(R.id.kinDob);
        btnSave=(Button)findViewById(R.id.btnNet);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kID = KinID.getText().toString();
                kname = KinName.getText().toString();
                kSname = KinSname.getText().toString();
                kCell = KinCell.getText().toString();
                kEmail = KinEmail.getText().toString();
                kAddress = KinAddress.getText().toString();
                kRelation = KinRelation.getText().toString();
                kDate = KinDate.getText().toString();
                kStatus = KinID.getText().toString();


                addKids(kID,kname,kSname,kDate,kCell,kEmail,kAddress,kRelation,idNo);
            }

        });



    }
    private void addKids(final String kID, final String kname, final String kSname,final String kDate,final String kCell,final String kEmail,final String kAddress,final String kRelation,final String idNo) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(AddNextOfKin.this, "Next Of Kin successfully added.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddNextOfKin.this,Dashboard.class));
                        finish();
                    }
                    else {
                        Toast.makeText(AddNextOfKin.this, "There was an error in adding your Next Of Kin, try again later.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddNextOfKin.this, "JSON Error" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddNextOfKin.this,"Volley Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("KinID",kID);
                params.put("KinName", kname);
                params.put("KinSName",kSname);
                params.put("Kindate",kDate);
                params.put("Kincell",kCell);
                params.put("KinEmail",kEmail);
                params.put("KinAdd",kAddress);
                params.put("KinRelation",kRelation);
                //  params.put("Kindate",kDate);
                params.put("patID",idNo);

                return params;
            }
        };

        Singleton.getInstance(AddNextOfKin.this).addToRequestQue(stringRequest);

    }

}
