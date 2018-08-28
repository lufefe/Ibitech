package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddCondition extends AppCompatActivity {

    Spinner sp_Severity;
    AutoCompleteTextView autoCompleteTextView;
    String[] conditionNames;
    String idNumber, condition,addDate,severity;
    TextView tv_date;
    Button btn_add,btn_cancel;
    SharedPreferences prefs;
    Boolean cValid = false;
    SessionManager sessionManager;

    String URL_CONDITION = "http://sict-iis.nmmu.ac.za/ibitech/app-test/addcondition.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_condition);

        sessionManager = new SessionManager(this);
        HashMap<String,String> user = sessionManager.getUserDetails();
        idNumber = user.get(sessionManager.ID);


        autoCompleteTextView = findViewById(R.id.condition);
        conditionNames = getResources().getStringArray(R.array.conditions);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,conditionNames);
        autoCompleteTextView.setAdapter(adapter);

        //Load severity spinner
        sp_Severity = findViewById(R.id.spnSeverity);
        ArrayAdapter<CharSequence> severityAdapter = ArrayAdapter.createFromResource(this,R.array.severity_level,R.layout.support_simple_spinner_dropdown_item);
        severityAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_Severity.setAdapter(severityAdapter);

        sp_Severity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?>parent, View view, int position, long id){
                severity = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?>parent){

            }
        });

        btn_add = findViewById(R.id.btnAdd);
        btn_cancel = findViewById(R.id.btnCancel);
        prefs = getSharedPreferences("REGP",MODE_PRIVATE);
        idNumber = prefs.getString("pID","");

        tv_date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_date.setText(dateFormat.format(date));

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddCondition.this,Dashboard.class));
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiate();
            }
        });

        //Real-time validation
        autoCompleteTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(autoCompleteTextView.getText().length() > 0){
                    cValid = ConditionValidate();
                }
            }
        });
    }

    private Boolean ConditionValidate() {
        condition = autoCompleteTextView.getText().toString();
        cValid = false;
        if(condition.isEmpty()){
            autoCompleteTextView.setError("Please enter a condition");
        }
        else {
            cValid = true;
        }
        return cValid;
    }

    private void initiate() {
        if(cValid){
            condition = autoCompleteTextView.getText().toString().trim();
            addDate = tv_date.getText().toString().trim();
            addCondition(idNumber,condition,addDate);
        }
        else {
            Toast.makeText(AddCondition.this, "Make sure you have entered the condition correctly. ", Toast.LENGTH_LONG).show();
        }
    }

    public void addCondition(final String idNumber, final String condition, final String addDate){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_CONDITION, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(AddCondition.this, "Condition added successfully", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddCondition.this,Dashboard.class));
                    }
                    else {
                        Toast.makeText(AddCondition.this, "Sorry, condition cannot be added at the moment.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddCondition.this, "Error : There was an internal error in adding the condition", Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddCondition.this,"Error : There was an internal error in adding the condition",Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("id",idNumber);
                params.put("cond",condition);
                params.put("date",addDate);
                return params;
            }
        };

        Singleton.getInstance(AddCondition.this).addToRequestQue(stringRequest);
    }


}
