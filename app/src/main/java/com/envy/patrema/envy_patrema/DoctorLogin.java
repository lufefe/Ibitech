package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
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

public class DoctorLogin extends AppCompatActivity {

    String medRegNo = "";
    Boolean validRegNo = false;

    EditText et_MedRegNo, et_Password;
    Button btn_Login;
    TextView tv_NewDocRegister, tv_ForgotPass, tv_PasswordToggle;
    ProgressBar pb_loading;

    String URL_LOGIN = "http://sict-iis.nmmu.ac.za/ibitech/app/doclogin.php";

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);

        sessionManager = new SessionManager(this);

        et_MedRegNo = findViewById(R.id.etMedReg);
        et_Password = findViewById(R.id.etPassword);

        btn_Login = findViewById(R.id.btnLogin);
        tv_NewDocRegister = findViewById(R.id.tvNewRegister);
        tv_ForgotPass = findViewById(R.id.tvForgotPass);

        pb_loading = findViewById(R.id.pbLoading);

        /*Real-time validation*/
        et_MedRegNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_MedRegNo.getText().length() > 0){
                    validRegNo = RegNoValidate();
                }
                else {
                    et_MedRegNo.setError(null);
                }
            }
        });

        tv_PasswordToggle = findViewById(R.id.tvTogglePassword);
        tv_PasswordToggle.setVisibility(View.GONE);
        et_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        et_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(et_Password.getText().length() > 0){
                    tv_PasswordToggle.setVisibility(View.VISIBLE);
                }
                else{
                    tv_PasswordToggle.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        tv_PasswordToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tv_PasswordToggle.getText() == "SHOW"){
                    tv_PasswordToggle.setText("HIDE");
                    et_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_Password.setSelection(et_Password.length());
                }
                else {
                    tv_PasswordToggle.setText("SHOW");
                    et_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_Password.setSelection(et_Password.length());

                }
            }
        });

        tv_NewDocRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorLogin.this, DoctorRegister.class));
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(validRegNo) || medRegNo.isEmpty()){
                    Toast.makeText(DoctorLogin.this, "Please ensure all fields are correctly filled",Toast.LENGTH_LONG).show();
                }

                if(validRegNo){
                    String regNo = et_MedRegNo.getText().toString();
                    String pass = et_Password.getText().toString();
                    UserLogin(regNo,pass);
                }
            }
        });

    }

    private void UserLogin(final String regNo,final String pass) {
        pb_loading.setVisibility(View.VISIBLE);
        btn_Login.setVisibility(View.GONE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");
                    JSONArray jsonArray = jsonObject.getJSONArray("login");

                    switch (success) {
                        case "1":
                            String id = "", cell = "", name = "", surname = "", email = "", occupation = "";
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject object = jsonArray.getJSONObject(i);

                                id = object.getString("id").trim();
                                cell = object.getString("cell").trim();
                                name = object.getString("name").trim();
                                surname = object.getString("surname").trim();
                                email = object.getString("email").trim();
                                occupation = object.getString("occupation").trim();

                            }
                            Toast.makeText(DoctorLogin.this, "PatientLogin Successful", Toast.LENGTH_LONG).show();
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);
                            saveDocPreferences(id, regNo, cell, name, surname, email, occupation);
                            //sessionManager to create session for doctor
                            sessionManager.createDocSession(id, regNo, cell, name, surname, email, occupation);
                            startActivity(new Intent(DoctorLogin.this, DocDashboard.class));

                            break;
                        case "-1":
                            Toast.makeText(DoctorLogin.this, "Wrong login details", Toast.LENGTH_LONG).show();
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);
                            break;
                        default:
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);
                            Toast.makeText(DoctorLogin.this, "PatientLogin Failed, this user doesn't exist in our database", Toast.LENGTH_LONG).show();
                            break;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loading.setVisibility(View.GONE);
                    btn_Login.setVisibility(View.VISIBLE);
                    Toast.makeText(DoctorLogin.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.GONE);
                btn_Login.setVisibility(View.VISIBLE);
                Toast.makeText(DoctorLogin.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("regNo",regNo);
                params.put("pass",pass);
                return params;
            }
        };
        Singleton.getInstance(DoctorLogin.this).addToRequestQue(stringRequest);

    }

    private void saveDocPreferences(String id, String regNo, String cell, String name, String surname, String email, String occupation) {
        SharedPreferences preferences = getSharedPreferences("DOCPREFS", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pID",id);
        editor.putString("pRegNo",regNo);
        editor.putString("pCell", cell);
        editor.putString("pName",name);
        editor.putString("pSurname",surname);
        editor.putString("pEmail",email);
        editor.putString("pOccupation",occupation);
        editor.apply();
    }

    private Boolean RegNoValidate() {
        medRegNo = et_MedRegNo.getText().toString();
        boolean valid = true;

        if(medRegNo.isEmpty() || medRegNo.length() > 12){
            et_MedRegNo.setError("Please enter a valid medical registration number");
            valid = false;
        }
        return valid;
    }
}
