package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class Welcome extends AppCompatActivity {

    Button registerButton,loginButton;
    String userType = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        registerButton = findViewById(R.id.btnRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openRegister();
            }
        });

        loginButton = findViewById(R.id.btnLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });


        final StickySwitch stickySwitch = findViewById(R.id.stick_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(@NonNull StickySwitch.Direction direction, @NonNull String s) {
                userType = stickySwitch.getText();
            }
        });

        // Gets user type and stores it in preferences
        userType = stickySwitch.getText();
        SharedPreferences preferences = getSharedPreferences("userType",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("pUserType", userType);
        editor.apply();
    }

    private void openRegister() {
        // checks selected user type in toggle switch
        if(userType.equals("Patient")){
            Intent intent = new Intent(getApplicationContext(), PatientRegister.class);
            startActivity(intent);
            finish();

        }
        else {
            Intent intent = new Intent(this, DocRegister.class);
            startActivity(intent);
            finish();
        }
    }
    private void openLogin() {
        // checks selected user type in toggle switch
        if(userType.equals("Patient")){
            Intent intent = new Intent(this, PatientLogin.class);
            startActivity(intent);
            finish();
        }
        else {
            Intent intent = new Intent(this, DocLogin.class);
            startActivity(intent);
            finish();
        }
    }
}


