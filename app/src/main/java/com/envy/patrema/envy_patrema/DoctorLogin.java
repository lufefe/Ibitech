package com.envy.patrema.envy_patrema;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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

public class DoctorLogin extends AppCompatActivity {

    String emailAddress = "", password = "";
    Boolean validEmail;

    EditText et_EmailAddress, et_Password;
    Button btn_Login;
    TextView tv_NewDocRegister, tv_ForgotPass, tv_PasswordToggle;
    ProgressBar pb_loading;
    Dialog dialog;
    TextView tv_dialogText;
    Button btnDialogDissmis;
    String dialogText = "";
    Boolean firstStart;

    String URL_LOGIN = "http://10.0.2.2/app/doctorlogin.php";

    SessionManager sessionManager;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_login);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("docFirstStart", true);

        dialog = new Dialog(this);
        sessionManager = new SessionManager(this);

        mAuth = FirebaseAuth.getInstance();

        et_EmailAddress = findViewById(R.id.etEmailAddress);
        et_Password = findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btnLogin);
        tv_NewDocRegister = findViewById(R.id.tvNewRegister);
        tv_ForgotPass = findViewById(R.id.tvForgotPass);
        pb_loading = findViewById(R.id.pbLoading);

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

        tv_NewDocRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorLogin.this, DoctorRegister.class));
            }
        });

        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailAddress = et_EmailAddress.getText().toString();
                password = et_Password.getText().toString();

                pb_loading.setVisibility(View.VISIBLE);
                btn_Login.setVisibility(View.INVISIBLE);

                mAuth.signInWithEmailAndPassword(emailAddress, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            verifyEmailAddress();

                        }
                        else {
                            dialogText = "There was an error with verifying your email account. Please check your log in details and try again.";
                            showErrorDialog(dialogText);

                            pb_loading.setVisibility(View.INVISIBLE);
                            btn_Login.setVisibility(View.VISIBLE);
                        }
                    }
                });


            }
        });

    }

    private void showErrorDialog(String message){
        dialog.setContentView(R.layout.custom_error_dialog);
        btnDialogDissmis = dialog.findViewById(R.id.btnDismiss);
        btnDialogDissmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        tv_dialogText = dialog.findViewById(R.id.txtErrorMessage);
        tv_dialogText.setText(message);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void verifyEmailAddress(){
        FirebaseUser user = mAuth.getCurrentUser();
        Boolean emailVerified = user.isEmailVerified();

        if (emailVerified){
            UserLogin(emailAddress, password);
        }
        else {
            dialogText = "Please verify your email first";
            showErrorDialog(dialogText);
            pb_loading.setVisibility(View.INVISIBLE);
            btn_Login.setVisibility(View.VISIBLE);
            mAuth.signOut();
        }
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
                            Toast.makeText(DoctorLogin.this, "Login Successful", Toast.LENGTH_LONG).show();
                            pb_loading.setVisibility(View.GONE);
                            btn_Login.setVisibility(View.VISIBLE);

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



}
