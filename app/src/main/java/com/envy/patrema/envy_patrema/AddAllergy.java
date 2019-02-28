package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddAllergy extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    SessionManager sessionManager;
    ConstraintLayout clTested;
    android.support.v7.widget.Toolbar toolbar;
    RadioGroup rgTested;
    AutoCompleteTextView act_Allergy;
    String[] allergyNames;
    Button btnCancel, btnAdd;
    ImageView img_Info;
    String emailAddress="",tested="", date_tested="", doctor="";
    String URL_ADD = "http://10.0.2.2/app/addallergy.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergy);

        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetails();
        emailAddress = user.get(SessionManager.EMAIL);


        toolbar = findViewById(R.id.tbAddAllergy);
        toolbar.setTitle("Add Allergy");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        clTested = findViewById(R.id.clTested);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        img_Info = findViewById(R.id.imgInfo);
        act_Allergy = findViewById(R.id.actAllergy);

        rgTested = findViewById(R.id.rg_Tested);
        rgTested.setOnCheckedChangeListener(this);

        allergyNames = getResources().getStringArray(R.array.allergies);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,allergyNames);
        act_Allergy.setAdapter(cAdapter);




        img_Info.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                new Tooltip.Builder(img_Info)
                        .setText("Add one allergy at a time.")
                        .setTextColor(Color.WHITE)
                        .setGravity(Gravity.TOP)
                        .setCornerRadius(8f)
                        .setDismissOnClick(true)
                        .show();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PatientDashboard.class));
                //finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String allergy = act_Allergy.getText().toString();
                if(allergy.isEmpty()){
                    act_Allergy.setError("You can't leave this field empty!");
                }
                else {
                    addAllergy(emailAddress, allergy, tested, doctor, date_tested);
                }
            }
        });

    }



    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rbYes:
                tested = "Yes";
                clTested.setVisibility(View.VISIBLE);
                break;

            case R.id.rbNo:
                tested = "No";
                clTested.setVisibility(View.INVISIBLE);
                break;
        }

    }

    private void addAllergy(final String emailAddress,final String allergy, final String tested, final String doctor, final String date_tested) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    switch (success) {
                        case "1":
                            Toast.makeText(AddAllergy.this, "Allergy successfully added.", Toast.LENGTH_LONG).show();
                            finish();
                            break;
                        case "-1":
                            Toast.makeText(AddAllergy.this, "You have already entered this allergy.", Toast.LENGTH_LONG).show();
                            break;
                        case "0":
                            Toast.makeText(AddAllergy.this, "Allergy couldn\'t be added.", Toast.LENGTH_LONG).show();
                            break;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddAllergy.this, "There has been an error in adding your allergy, try again later.", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddAllergy.this,"There has been an error in our internal server, try again later.",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("email", emailAddress);
                params.put("allergy",allergy);
                params.put("tested", tested);
                params.put("doctor", doctor);
                params.put("date_tested", date_tested);

                return params;
            }
        };

        Singleton.getInstance(AddAllergy.this).addToRequestQue(stringRequest);

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
