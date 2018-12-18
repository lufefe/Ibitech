package com.envy.patrema.envy_patrema;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.tooltip.Tooltip;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddAllergy extends AppCompatActivity {

    AutoCompleteTextView act_Allergy;
    String[] allergyNames;
    Button btnCancel, btnAdd;
    TextView tv_Date;
    ImageView img_Info;
    String idNo = "";
    String URL_ADD = "http://sict-iis.nmmu.ac.za/ibitech/app/addallergy.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_allergy);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        tv_Date = findViewById(R.id.tvDate);
        img_Info = findViewById(R.id.imgInfo);
        act_Allergy = findViewById(R.id.actAllergy);

        allergyNames = getResources().getStringArray(R.array.allergies);
        ArrayAdapter<String> cAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,allergyNames);
        act_Allergy.setAdapter(cAdapter);

        idNo = prefs.getString("pID","");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));

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
                //startActivity(new Intent(AddAllergy.this,Dashboard.class));
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String allergy = act_Allergy.getText().toString();
                final String date = tv_Date.getText().toString();
                if(allergy.isEmpty()){
                    act_Allergy.setError("Please enter a valid allergy");
                }
                else {
                    addAllergy(allergy,date,idNo);
                }
            }
        });

    }

    private void addAllergy(final String allergy, final String date, final String idNo) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_ADD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(AddAllergy.this, "Allergy successfully added.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    else
                        Toast.makeText(AddAllergy.this, "You have already entered this allergy.", Toast.LENGTH_LONG).show();


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

                params.put("allergy",allergy);
                params.put("date", date);
                params.put("id",idNo);

                return params;
            }
        };

        Singleton.getInstance(AddAllergy.this).addToRequestQue(stringRequest);

    }
}
