package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PatientLogin extends AppCompatActivity {

        //Declare variables
        String emailAddress="", password = "";
        Boolean validEmail;

        //Define views
        EditText et_EmailAddress, et_Password;
        Button btn_Login;
        TextView tv_NewRegister,tv_ForgotPass,tv_PasswordToggle;
        ProgressBar pb_loading;

        //PatientLogin URL
        String URL_LOGIN = "http://sict-iis.nmmu.ac.za/ibitech/app/login.php";

        SessionManager sessionManager;

        private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        mAuth = FirebaseAuth.getInstance();

        et_Password =  findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btnLogin);
        tv_NewRegister = findViewById(R.id.tvNewRegister);
        tv_ForgotPass = findViewById(R.id.tvForgotPass);
        pb_loading = findViewById(R.id.pbLoading);


        /*Real-time validation**/



        //Show password toggle
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

        //PatientRegister Intent
        tv_NewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PatientLogin.this,PatientRegister.class));
                finish();
            }
        });


        //PatientLogin button
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(true) {
                    mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                verifyEmailAddress();

                            }
                            else {
                                Toast.makeText(getApplicationContext(), "Pleases verify email first", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }
            }
        });

    }

    public void verifyEmailAddress(){
        FirebaseUser user = mAuth.getCurrentUser();
        Boolean emailVerified = user.isEmailVerified();

        if (emailVerified){
            startActivity(new Intent(PatientLogin.this, Dashboard.class));
        }
        else {
            Toast.makeText(getApplicationContext(), "Pleases verify email first", Toast.LENGTH_LONG).show();
            mAuth.signOut();
        }
    }



    private void UserLogin(final String id, final String cell, final String pass) {
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
                            String name = "", surname = "", age = "", bloodtype = "", gender = "", status = "", address = "", cellNo = "", email = "", weight = "", height = "", profilePic = "", medicalAid = "";
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject object = jsonArray.getJSONObject(i);

                                name = object.getString("name").trim();
                                surname = object.getString("surname").trim();
                                age = object.getString("age").trim();
                                bloodtype = object.getString("bloodtype").trim();
                                gender = object.getString("gender").trim();
                                status = object.getString("status").trim();
                                address = object.getString("address").trim();

                                cellNo = object.getString("cell").trim();
                                email = object.getString("email").trim();
                                weight = object.getString("weight").trim();
                                height = object.getString("height").trim();
                                profilePic = object.getString("profilePic").trim();
                                medicalAid = object.getString("medicalAid").trim();

                            }
                            Toast.makeText(PatientLogin.this, "PatientLogin Successful", Toast.LENGTH_LONG).show();
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);

                            //uses SessionManager class
                            sessionManager.createSession(id, name, surname, age, bloodtype, gender, status, address, cellNo, email, weight, height, profilePic, medicalAid);
                            startActivity(new Intent(PatientLogin.this, Dashboard.class));

                            break;
                        case "-1":
                            Toast.makeText(PatientLogin.this, "Wrong login details", Toast.LENGTH_LONG).show();
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);

                            break;
                        default:
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);
                            Toast.makeText(PatientLogin.this, "PatientLogin Failed, this user doesn't exist in our database", Toast.LENGTH_LONG).show();

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loading.setVisibility(View.GONE);
                    btn_Login.setVisibility(View.VISIBLE);
                    Toast.makeText(PatientLogin.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.GONE);
                btn_Login.setVisibility(View.VISIBLE);
                Toast.makeText(PatientLogin.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        })
    {
            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                params.put("cell",cell);
                params.put("pass",pass);
                return params;
            }
        };
        Singleton.getInstance(PatientLogin.this).addToRequestQue(stringRequest);

    }


}


