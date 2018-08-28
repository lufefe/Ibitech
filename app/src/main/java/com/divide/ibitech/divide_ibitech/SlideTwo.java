package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SlideTwo extends AppCompatActivity {

    EditText et_Weight,et_Height;
    Spinner sp_MaritalStatus, sp_BloodType;
    Button btn_Done, btn_Prev;
    String maritalStatus ="", bloodType = "";
    Boolean validWeight =false,validHeight = false;
    Float weight, height;
    ProgressBar pb_loading;
    SessionManager sessionManager;

    String URL_REGISTCONT = "http://sict-iis.nmmu.ac.za/ibitech/app-test/registercont.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_two);

        sessionManager = new SessionManager(this);

        sp_MaritalStatus = findViewById(R.id.marital_status);
        ArrayAdapter<CharSequence> maritalAdapter = ArrayAdapter.createFromResource(this,R.array.marital_status,R.layout.support_simple_spinner_dropdown_item);
        maritalAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_MaritalStatus.setAdapter(maritalAdapter);

        sp_MaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_BloodType = findViewById(R.id.list_blood);
        ArrayAdapter<CharSequence> bloodTypeAdapter = ArrayAdapter.createFromResource(this,R.array.list_blood,R.layout.support_simple_spinner_dropdown_item);
        bloodTypeAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sp_BloodType.setAdapter(bloodTypeAdapter);

        sp_BloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        et_Weight = findViewById(R.id.weight);
        et_Height = findViewById(R.id.height);
        btn_Done = findViewById(R.id.btnDone);
        btn_Prev = findViewById(R.id.btnPrevSlide);

        pb_loading = findViewById(R.id.pbLoading);

        et_Weight.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_Weight.getText().length() > 0){
                    validWeight = ValidateWeight();
                }
            }
        });

        et_Height.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_Height.getText().length() > 0){
                    validHeight = ValidateHeight();
                }
            }
        });

        btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((validWeight)&&(validHeight)){
                    savePreferences();
                }
                else {
                    Toast.makeText(SlideTwo.this,"Please enter all necessary details",Toast.LENGTH_LONG).show();
                }
            }
        });

        btn_Prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SlideTwo.this,SlideOne.class));
            }
        });

    }

    public void retrievePrevPreferences() {
        SharedPreferences preferences = getSharedPreferences("slideOnePrefs",MODE_PRIVATE);
        SharedPreferences prefs = getSharedPreferences("REGP",MODE_PRIVATE);

        SharedPreferences sharedPreferences = getSharedPreferences("userType", MODE_PRIVATE);
        String userType = sharedPreferences.getString("pUserType","");

        String idnumber = prefs.getString("pID","");
        String cellphone = prefs.getString("pCell","");
        String email = prefs.getString("pEmail","");
        String password = prefs.getString("pPass","");

        String name = preferences.getString("pName","");
        String surname = preferences.getString("pSurname","");
        String dob = preferences.getString("pDOB","");
        String gender = preferences.getString("pGender","");
        String address = preferences.getString("pAddress","");
        String suburb = preferences.getString("pSuburb","");
        String city = preferences.getString("pCity","");
        String code = preferences.getString("pPCode","");

        userRegisterCont(idnumber,cellphone,email,password,name,surname,dob,gender,address,suburb,city,code,maritalStatus,bloodType,weight.toString(),height.toString(),userType);
    }

    public void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("slideTwoPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String s_Status = maritalStatus;
        String s_BloodType = bloodType;
        String s_Weight = et_Weight.getText().toString();
        String s_Height = et_Height.getText().toString();

        editor.putString("pMarital",s_Status);
        editor.putString("pBloodType",s_BloodType);
        editor.putString("pWeight",s_Weight);
        editor.putString("pHeight",s_Height);
        editor.apply();

        retrievePrevPreferences();

    }

    private Boolean ValidateHeight() {
        height = Float.parseFloat(et_Height.getText().toString());
        validHeight = true;

        if(height.isNaN()){
            et_Height.setError("Please enter a valid height (in cm's)");
            validHeight = false;
        }
        return validHeight;
    }

    private Boolean ValidateWeight() {
        weight = Float.parseFloat(et_Weight.getText().toString());
        validWeight = true;

        if(weight.isNaN()){
            et_Weight.setError("Please enter a valid weight (in kg's)");
            validWeight = false;
        }

        return validWeight;
    }

    public void userRegisterCont(final String userID, final String userCell, final String userEmail, final String userPassword,
                                 final String userFName, final String userSurname, final String userDOB, final String userGender,
                                 final String userAddress,final String userSuburb,final String userCity,final  String userPostalCode,
                                 final String userMaritalStatus, final String userBloodType, final String userWeight, final String userHeight, final String userType) {
        pb_loading.setVisibility(View.VISIBLE);
        btn_Done.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTCONT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                   JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(SlideTwo.this, "Registration successful", Toast.LENGTH_LONG).show();

                        DateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        Date dob = format.parse("1997-04-25");

                        Integer age = calculateAge(dob);

                        sessionManager.createSession(userID, userFName,userSurname,age.toString(),userBloodType,userGender,userMaritalStatus,userAddress,userCell, userEmail, userWeight,userHeight,"","");
                        startActivity(new Intent(SlideTwo.this,Dashboard.class));
                        finish();
                    }
                    else if(success.equals("-1")){
                        pb_loading.setVisibility(View.GONE);
                        btn_Done.setVisibility(View.VISIBLE);
                        Toast.makeText(SlideTwo.this, "Failed , only inserted user table", Toast.LENGTH_LONG).show();
                    }
                    else {
                        pb_loading.setVisibility(View.GONE);
                        btn_Done.setVisibility(View.VISIBLE);
                        Toast.makeText(SlideTwo.this, "Failed , couldn't complete registration", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loading.setVisibility(View.GONE);
                    btn_Done.setVisibility(View.VISIBLE);
                    Toast.makeText(SlideTwo.this, "1Register Error" + e.toString(), Toast.LENGTH_LONG).show();

                } catch (ParseException e) {
                    e.printStackTrace();
                    pb_loading.setVisibility(View.GONE);
                    btn_Done.setVisibility(View.VISIBLE);
                    Toast.makeText(SlideTwo.this, "2Register Error" + e.toString(), Toast.LENGTH_LONG).show();

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.GONE);
                btn_Done.setVisibility(View.VISIBLE);
                Toast.makeText(SlideTwo.this,"3Register Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("id",userID);
                params.put("cell",userCell);
                params.put("email",userEmail);
                params.put("pass",userPassword);

                params.put("fname",userFName);
                params.put("surname",userSurname);
                params.put("dob",userDOB);
                params.put("gender",userGender);
                params.put("address",userAddress);
                params.put("suburb",userSuburb);
                params.put("city",userCity);
                params.put("code",userPostalCode);

                params.put("status",userMaritalStatus);
                params.put("bloodtype",userBloodType);
                params.put("weight",userWeight);
                params.put("height",userHeight);

                params.put("usertype",userType);

                return params;
            }
        };

        Singleton.getInstance(SlideTwo.this).addToRequestQue(stringRequest);
    }

    private static int calculateAge(Date dob){
        Calendar dateOB = Calendar.getInstance();
        dateOB.setTime(dob);

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        return currentDate.get(Calendar.YEAR) - dateOB.get(Calendar.YEAR);
    }


    public void UploadProfilePicture(View view) {

    }
}
