package com.envy.patrema.envy_patrema;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.ybs.passwordstrengthmeter.PasswordStrength;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PatientRegister extends AppCompatActivity implements TextWatcher {

    String newPassword = "",cPassword = "", emailAddress="", userType= "";
    EditText et_EmailAddress, et_EnterPassword, et_ConfirmPassword;
    Button btn_Register;
    TextView tv_login,strengthView;
    String passStrength;
    ProgressBar progressBar,pb_loading;
    Dialog dialog;
    TextView tv_dialogText;
    Button btnDialogDissmis;
    String dialogText = "";

    public Boolean validEmail= false, validNewPass = false,validCpass = false, checked = false;
    CheckBox policyCheck;

    String URL_REGIST = "http://10.0.2.2/app/patientregister.php";

    String URL_REGISTCONT = "http://10.0.2.2/app/patientregister2.php";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        SharedPreferences preferences = getSharedPreferences("USERTYPE",MODE_PRIVATE);
        userType = preferences.getString("pUserType", "");

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        tv_dialogText = findViewById(R.id.txtErrorMessage);
        btnDialogDissmis = findViewById(R.id.btnDismiss);


        // Get input values from xml
        et_EnterPassword = findViewById(R.id.etCreatePassword);
        et_EnterPassword.addTextChangedListener(this);
        et_ConfirmPassword= findViewById(R.id.etConfirmPassword);
        et_EmailAddress = findViewById(R.id.etEmailAddress);
        tv_login = findViewById(R.id.login);
        btn_Register= findViewById(R.id.btnRegister);
        policyCheck = findViewById(R.id.chkPolicy);
        pb_loading = findViewById(R.id.pbLoading);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(PatientRegister.this, PatientLogin.class);
                startActivity(loginIntent);
                finish();
            }
        });

        btn_Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checked)
                    policyCheck.setError("Please check the box to agree to our terms and conditions and privacy policy.");

                if ((validEmail) && (validCpass) && (checked)){
                    final String email = et_EmailAddress.getText().toString();
                    checkUserExists(email);

                }
            }
        });


        policyCheck = findViewById(R.id.chkPolicy);

        policyCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = false;
                if(policyCheck.isChecked()){
                    checked = true;
                    policyCheck.setError(null);
                    et_EmailAddress.requestFocus();
                }
            }
        });

        /* Real-time validation */

        //Email address
        et_EmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(et_EmailAddress.getText().length() > 0){
                    validEmail = EmailAddressValidate();
                }
                else {
                    et_EmailAddress.setError(null);
                }
            }
        });

        //New password
        et_EnterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_EnterPassword.getText().length() > 0){
                    validNewPass = NewPasswordValidate();
                }
                else {
                    et_EnterPassword.setError(null);
                }
            }
        });

        //Confirm password
        et_ConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                    if (et_ConfirmPassword.getText().length() > 0) {
                        validCpass = ConfirmPasswordValidate();
                    }
                    else {
                        et_ConfirmPassword.setError(null);
                    }
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

    private void sendVerificationEmail(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        registerPatient(emailAddress, newPassword, userType);
                        mAuth.signOut();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "Error sending the verification email.", Toast.LENGTH_LONG).show();
                        mAuth.signOut();
                    }
                }
            });
        }

    }
    private void registerPatient(final String emailAddress, final String password, final String userType) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTCONT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        //sessionManager.createSession(userID, userFName,userSurname,age.toString(),userBloodType,userGender,userMaritalStatus,userAddress,userCell, userEmail, userWeight,userHeight,"","");
                        dialogText = "A verification link has been sent to your email. Please verify your account and login.";
                        showErrorDialog(dialogText);
                        btnDialogDissmis.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getApplicationContext(), PatientLogin.class));
                                finish();
                            }
                        });

                    }
                    else {
                        pb_loading.setVisibility(View.INVISIBLE);
                        btn_Register.setVisibility(View.VISIBLE);
                        dialogText = "There has been an error in registering your account, our database administrator will sort it out. Try again later.";
                        showErrorDialog(dialogText);
                        //Delete the user from firebase
                    }

                } catch (JSONException e) {
                    pb_loading.setVisibility(View.INVISIBLE);
                    btn_Register.setVisibility(View.VISIBLE);
                    //e.printStackTrace();
                    dialogText = "There was an internal error with communicating with our database. Please try again later.";
                    showErrorDialog(dialogText);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.INVISIBLE);
                btn_Register.setVisibility(View.VISIBLE);
                dialogText = "Our servers are currently down. Sorry for the inconvenience.";
                showErrorDialog(dialogText);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("email",emailAddress);
                params.put("password",password);
                params.put("usertype",userType);

                return params;
            }
        };

        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    //Validate methods
    private boolean EmailAddressValidate() {
        emailAddress = et_EmailAddress.getText().toString();
        validEmail = false;

        if(emailAddress.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            et_EmailAddress.setError("Please enter a valid email address");
        }
        else {
            validEmail = true;
        }
        return validEmail;
    }

    private boolean NewPasswordValidate() {
        newPassword = et_EnterPassword.getText().toString();
        validNewPass = false;

        if(newPassword.isEmpty() || passStrength.equals("Weak")){
            et_EnterPassword.setError("Password should be at least 8 characters, with at least 1 number and 1 special character.");
        }
        else {
            validNewPass = true;
        }
        return validNewPass;
    }

    private boolean ConfirmPasswordValidate() {
        newPassword = et_EnterPassword.getText().toString();
        cPassword = et_ConfirmPassword.getText().toString();
        validCpass = false;

        if(cPassword.isEmpty() || !(newPassword.equals(cPassword))){
            et_ConfirmPassword.setError("Passwords entered don't match");
        }
        else {
            validCpass = true;
        }
        return validCpass;
    }


    // PASSWORD STRENGTH CHECKER METHODS
    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        updatePasswordStrengthView(charSequence.toString());
    }

    private void updatePasswordStrengthView(String password) {

        progressBar = findViewById(R.id.progressBar);
        strengthView = findViewById(R.id.password_strength);
        if(TextView.VISIBLE != strengthView.getVisibility()){
            return;
        }
        if(password.isEmpty()){
            strengthView.setText("");
            progressBar.setProgress(0);
            return;
        }

        PasswordStrength strength = PasswordStrength.calculateStrength(password);
        strengthView.setText(strength.getText(this));
        strengthView.setTextColor(strength.getColor());

        progressBar.getProgressDrawable().setColorFilter(strength.getColor(), PorterDuff.Mode.SRC_IN);
        if(strength.getText(this).equals("Weak")){
            passStrength = "Weak";
            progressBar.setProgress(25);
        }
        else if(strength.getText(this).equals("Medium")){
            passStrength = "Medium";
            progressBar.setProgress(50);
        }
        else if(strength.getText(this).equals("Strong")){
            passStrength = "Strong";
            progressBar.setProgress(75);
        }
        else {
            progressBar.setProgress(100);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    // method checks if email address already exists in database
    public void checkUserExists(final String emailAddress){
        pb_loading.setVisibility(View.VISIBLE);
        btn_Register.setVisibility(View.INVISIBLE);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {

                        mAuth.createUserWithEmailAndPassword(emailAddress, newPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful() ){
                                    sendVerificationEmail();
                                }
                                else {
                                    dialogText = "Error creating the user, the user already exists. Please check your email for a verification link.";
                                    showErrorDialog(dialogText);
                                    pb_loading.setVisibility(View.INVISIBLE);
                                    btn_Register.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                    else {
                        pb_loading.setVisibility(View.INVISIBLE);
                        btn_Register.setVisibility(View.VISIBLE);

                        dialogText = "This user already exist in our database";
                        showErrorDialog(dialogText);
                    }

                } catch (JSONException e) {
                    pb_loading.setVisibility(View.INVISIBLE);
                    btn_Register.setVisibility(View.VISIBLE);
                    dialogText = "Error communicating with the database, try again later. Sorry for the inconvenience.";
                    showErrorDialog(dialogText);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.INVISIBLE);
                btn_Register.setVisibility(View.VISIBLE);
                dialogText = "There has been an error with our internal servers, try again later. Sorry for the inconvenience.";
                showErrorDialog(dialogText);


            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("email",emailAddress);

                return params;
            }
        };

        Singleton.getInstance(PatientRegister.this).addToRequestQue(stringRequest);
    }

}
