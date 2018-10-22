package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class Welcome extends AppCompatActivity {

    LinearLayout topPart, bottomPart;
    Button registerButton,loginButton;
    Animation uptodown,downtoup;
    String userType = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Variables for animation
        topPart = findViewById(R.id.topPart);
        bottomPart = findViewById(R.id.bottomPart);
        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        downtoup = AnimationUtils.loadAnimation(this,R.anim.downtoup);
        topPart.setAnimation(uptodown);
        bottomPart.setAnimation(downtoup);

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
            Intent intent = new Intent(this, Register.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, DocRegister.class);
            startActivity(intent);
        }
    }
    private void openLogin() {
        // checks selected user type in toggle switch
        if(userType.equals("Patient")){
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, DocLogin.class);
            startActivity(intent);
        }
    }
}


