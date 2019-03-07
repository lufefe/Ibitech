package com.envy.patrema.envy_patrema;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

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

public class DoctorRegister extends AppCompatActivity implements TextWatcher {

    String regNo = "",emailAddress = "", newPassword = "",cPassword = "", userType;
    EditText et_RegNo,et_EmailAddress, et_EnterPassword, et_ConfirmPassword;
    Button btn_Submit;
    TextView tv_login,strengthView;
    String passStrength;
    ProgressBar progressBar,pb_loading;
    Dialog dialog;
    TextView tv_dialogText;
    Button btnDialogDissmis;
    String dialogText = "";

    Boolean validRegNo = false, validEmail = false, validNewPass = false, validCPass = false;

    ConstraintLayout constraintLayout;
    private FirebaseAuth mAuth;

    String URL_REGIST = "http://10.0.2.2/app/doctorregister.php";

    String URL_REGISTCONT = "http://10.0.2.2/app/userregister.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_register);

        SharedPreferences preferences = getSharedPreferences("USERTYPE",MODE_PRIVATE);
        userType = preferences.getString("pUserType", "");

        constraintLayout = findViewById(R.id.clRegister);

        mAuth = FirebaseAuth.getInstance();

        dialog = new Dialog(this);
        tv_dialogText = findViewById(R.id.txtErrorMessage);
        btnDialogDissmis = findViewById(R.id.btnDismiss);

        et_RegNo = findViewById(R.id.etMedReg);
        et_EmailAddress = findViewById(R.id.etEmailAddress);
        et_EnterPassword = findViewById(R.id.etCreatePassword);
        et_EnterPassword.addTextChangedListener(this);
        et_ConfirmPassword = findViewById(R.id.etConfirmPassword);

        btn_Submit = findViewById(R.id.btnSubmit);


        tv_login = findViewById(R.id.login);
        pb_loading = findViewById(R.id.pbLoading);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(DoctorRegister.this, DoctorLogin.class);
                startActivity(loginIntent);
            }
        });

        /*Real-time validation*/
        et_RegNo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (et_RegNo.getText().length() > 0){
                    validRegNo = RegNoValidate();
                }
                else {
                    et_RegNo.setError(null);
                }
            }
        });

        et_EmailAddress.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_EmailAddress.getText().length() > 0){
                    validEmail = EmailAddressValidate();
                }
                else {
                    et_EmailAddress.setError(null);
                }
            }
        });

        et_EnterPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_ConfirmPassword.getText().length() > 0){
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
                    validCPass = ConfirmPasswordValidate();
                }
                else {
                    et_ConfirmPassword.setError(null);
                }
            }
        });

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_RegNo.requestFocus();
                if((validRegNo) && (validEmail) && (validCPass)){
                    final String medReg = et_RegNo.getText().toString();
                    final String email = et_EmailAddress.getText().toString();
                    checkUserExists(medReg, email);
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

    //SnackBar code
    /*private void docSubmit() {
        //to ADMIN for registration
        final Snackbar snackbar = Snackbar.make(constraintLayout,"Your details have been submitted, Further communication to occur via email.", Snackbar.LENGTH_LONG);
        snackbar.setAction("Dismiss", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                snackbar.dismiss();
            }
        });
        snackbar.show();

    }*/

    private Boolean ConfirmPasswordValidate() {
        newPassword = et_EnterPassword.getText().toString();
        cPassword = et_ConfirmPassword.getText().toString();
        boolean valid = false;

        if(cPassword.isEmpty() || !(newPassword.equals(cPassword))){
            et_ConfirmPassword.setError("Passwords entered don't match");
        }
        else {
            valid = true;
        }
        return valid;
    }

    private Boolean NewPasswordValidate() {
        newPassword = et_EnterPassword.getText().toString();
        passStrength = strengthView.getText().toString();
        boolean valid = false;

        if(newPassword.isEmpty() || (passStrength.equals("Weak"))){
            et_EnterPassword.setError("Password should be at least 8 characters, with at least 1 number and 1 special character.");
        }
        else {
            valid = true;
        }
        return valid;
    }

    private Boolean EmailAddressValidate() {
        emailAddress = et_EmailAddress.getText().toString();
        boolean valid = false;

        if(emailAddress.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()){
            et_EmailAddress.setError("Please enter a valid email address");
        }
        else {
            valid = true;
        }
        return valid;
    }

    private Boolean RegNoValidate() {
        regNo = et_RegNo.getText().toString();
        boolean valid = false;

        if(regNo.isEmpty() || regNo.length() > 12){
            et_RegNo.setError("Please enter a valid medical registration number.");
        }
        else {
            valid = true;
        }

        return valid;
    }

    //PASSWORD STRENGTH CHECKER
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

    private void sendVerificationEmail(){
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null){
            registerDoctor(regNo, emailAddress, newPassword, userType);
        }
    }

    private void registerDoctor(final String regNo, final String emailAddress, final String password, final String userType) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_REGISTCONT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    mAuth.signOut();
                                }
                                else {
                                    dialogText = "Error sending the verification email.";
                                    showErrorDialog(dialogText);
                                    mAuth.signOut();
                                }
                            }
                        });
                        //sessionManager.createSession(userID, userFName,userSurname,age.toString(),userBloodType,userGender,userMaritalStatus,userAddress,userCell, userEmail, userWeight,userHeight,"","");
                        dialogText = "A verification link has been sent to your email. Please verify your account and login.";
                        showErrorDialog(dialogText);
                        btnDialogDissmis.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                               startActivity(new Intent(getApplicationContext(), DoctorLogin.class));
                                finish();

                            }
                        });

                    }
                    else {
                        pb_loading.setVisibility(View.INVISIBLE);
                        btn_Submit.setVisibility(View.VISIBLE);
                        dialogText = "There has been an error in registering your account, our database administrator will sort it out. Try again later.";
                        showErrorDialog(dialogText);
                        //Delete the user from firebase
                    }

                } catch (JSONException e) {
                    pb_loading.setVisibility(View.INVISIBLE);
                    btn_Submit.setVisibility(View.VISIBLE);
                    //e.printStackTrace();
                    dialogText = "There was an internal error with communicating with our database. Please try again later.";
                    showErrorDialog(dialogText);

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.INVISIBLE);
                btn_Submit.setVisibility(View.VISIBLE);
                dialogText = "Our servers are currently down. Sorry for the inconvenience.";
                showErrorDialog(dialogText);

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("med_reg_no", regNo);
                params.put("email",emailAddress);
                params.put("password",password);
                params.put("usertype",userType);

                return params;
            }
        };

        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    // method checks if email address already exists in database
    public void checkUserExists(final String regNo, final String emailAddress){
        pb_loading.setVisibility(View.VISIBLE);
        btn_Submit.setVisibility(View.INVISIBLE);

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
                                    btn_Submit.setVisibility(View.VISIBLE);
                                }
                            }
                        });

                    }
                    else {
                        pb_loading.setVisibility(View.INVISIBLE);
                        btn_Submit.setVisibility(View.VISIBLE);

                        dialogText = "This user already exist in our database";
                        showErrorDialog(dialogText);
                    }

                } catch (JSONException e) {
                    pb_loading.setVisibility(View.INVISIBLE);
                    btn_Submit.setVisibility(View.VISIBLE);
                    dialogText = "Error communicating with the database, try again later. Sorry for the inconvenience.";
                    showErrorDialog(dialogText);
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pb_loading.setVisibility(View.INVISIBLE);
                btn_Submit.setVisibility(View.VISIBLE);
                dialogText = "There has been an error with our internal servers, try again later. Sorry for the inconvenience.";
                showErrorDialog(dialogText);


            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("med_reg_no", regNo);
                params.put("email",emailAddress);

                return params;
            }
        };

        Singleton.getInstance(DoctorRegister.this).addToRequestQue(stringRequest);
    }

}
