package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ybs.passwordstrengthmeter.PasswordStrength;

public class DocRegister extends AppCompatActivity implements TextWatcher {

    String regNo = "",emailAddress = "", newPassword = "",cPassword = "";
    EditText et_RegNo,et_EmailAddress, et_EnterPassword, et_ConfirmPassword;
    Button btn_Submit;
    TextView tv_login,strengthView;
    String passStrength;
    ProgressBar progressBar,pb_loading;
    Boolean validRegNo = false, validEmail = false, validNewPass = false, validCPass = false;

    String URL_REGIST = "http://sict-iis.nmmu.ac.za/ibitech/app-test/docregister.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_register);

        et_RegNo = findViewById(R.id.etMedReg);
        et_EmailAddress = findViewById(R.id.etEmailAddress);
        et_EnterPassword = findViewById(R.id.etCreatePassword);
        et_ConfirmPassword = findViewById(R.id.etConfirmPassword);

        btn_Submit = findViewById(R.id.btnSubmit);

        tv_login = findViewById(R.id.login);
        pb_loading = findViewById(R.id.pbLoading);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(DocRegister.this, DocLogin.class);
                startActivity(loginIntent);
            }
        });

        /**Real-time validation*/
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
                if((validRegNo) && (validEmail) && (validCPass)){
                    docSubmit(regNo, emailAddress, newPassword);
                }
            }
        });

    }

    private void docSubmit(final String regNo, final String emailAddress, final String newPassword) {
        //to ADMIN for registration
    }

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
            progressBar.setProgress(25);
        }
        else if(strength.getText(this).equals("Medium")){
            progressBar.setProgress(50);
        }
        else if(strength.getText(this).equals("Strong")){
            progressBar.setProgress(75);
        }
        else {
            progressBar.setProgress(100);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

}
