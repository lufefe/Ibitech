package com.divide.ibitech.divide_ibitech;

import android.app.VoiceInteractor;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

        //Declare variables
        String idNumber = "", cellphoneNumber = "";
        Boolean validID = false,validCell = false;

        //Define views
        EditText et_IDNumber,et_CellphoneNum,et_Password;
        Button btn_Login;
        TextView tv_NewRegister,tv_ForgotPass,tv_PasswordToggle;
        ProgressBar pb_loading;

        //Login URL
        String URL_LOGIN = "http://sict-iis.nmmu.ac.za/ibitech/app-test/login.php";

        SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sessionManager = new SessionManager(this);

        et_IDNumber = findViewById(R.id.etIDNumber);
        et_CellphoneNum =  findViewById(R.id.etCellphoneNum);
        et_Password =  findViewById(R.id.etPassword);
        btn_Login = findViewById(R.id.btnLogin);
        tv_NewRegister = findViewById(R.id.tvNewRegister);
        tv_ForgotPass = findViewById(R.id.tvForgotPass);
        pb_loading = findViewById(R.id.pbLoading);


        /**Real-time validation**/
        //ID Number
        et_IDNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_IDNumber.getText().length() > 0){
                    validID = IDNumberValidate();
                }
                else{
                    et_IDNumber.setError(null);
                }

            }
        });

        //Cellphone Number
        et_CellphoneNum.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_CellphoneNum.getText().length() > 0){
                    validCell = CellphoneValidate();
                }
                else {
                    et_CellphoneNum.setError(null);
                }
            }
        });


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

        //Register Intent
        tv_NewRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });


        //Login button
        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!(validID) || !(validCell)) {
                    if(idNumber.isEmpty() || cellphoneNumber.isEmpty())
                        Toast.makeText(Login.this, "Please ensure all fields are correctly filled!",Toast.LENGTH_LONG).show();
                }
                if((validID) && (validCell)) {
                    String id = et_IDNumber.getText().toString();
                    String cell = et_CellphoneNum.getText().toString();
                    String pass = et_Password.getText().toString();
                    UserLogin(id,cell,pass);
                }
            }
        });

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

                    if (success.equals("1")) {
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
                        Toast.makeText(Login.this, "Login Successful", Toast.LENGTH_LONG).show();
                        pb_loading.setVisibility(View.GONE);
                        btn_Login.setVisibility(View.VISIBLE);

                        //uses SessionManager class
                        sessionManager.createSession(id, name, surname, age, bloodtype, gender, status, address,cellNo,email,weight,height,profilePic,medicalAid);
                        startActivity(new Intent(Login.this, Dashboard.class));

                    } else if (success.equals("-1")) {
                        Toast.makeText(Login.this, "Wrong login details", Toast.LENGTH_LONG).show();
                        pb_loading.setVisibility(View.GONE);
                        btn_Login.setVisibility(View.VISIBLE);

                    } else {
                        pb_loading.setVisibility(View.GONE);
                        btn_Login.setVisibility(View.VISIBLE);
                        Toast.makeText(Login.this, "Login Failed, this user doesn't exist in our database", Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    pb_loading.setVisibility(View.GONE);
                    btn_Login.setVisibility(View.VISIBLE);
                    Toast.makeText(Login.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.GONE);
                btn_Login.setVisibility(View.VISIBLE);
                Toast.makeText(Login.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
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
        Singleton.getInstance(Login.this).addToRequestQue(stringRequest);

    }

    private boolean IDNumberValidate() {
        idNumber = et_IDNumber.getText().toString();
         boolean valid = true;

        if(idNumber.isEmpty() || idNumber.length() != 13){
            et_IDNumber.setError("Please enter a valid ID number");
            valid = false;
        }

        return valid;
    }
    private boolean CellphoneValidate() {
        cellphoneNumber = et_CellphoneNum.getText().toString();
        boolean valid = true;

        if(cellphoneNumber.isEmpty() || cellphoneNumber.length() != 10){
            et_CellphoneNum.setError("Please enter a valid cellphone number");
            valid = false;
        }

        return valid;
    }

}


