package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import io.ghyeok.stickyswitch.widget.StickySwitch;


public class Welcome extends AppCompatActivity {

    LinearLayout topPart, bottomPart;
    Button registerButton,loginButton;
    Animation uptodown,downtoup;
    TextView tv_Cherrie;
    String userType = "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);


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

        tv_Cherrie = findViewById(R.id.cherrie);

        final StickySwitch stickySwitch = findViewById(R.id.stick_switch);
        stickySwitch.setOnSelectedChangeListener(new StickySwitch.OnSelectedChangeListener() {
            @Override
            public void onSelectedChange(StickySwitch.Direction direction, String s) {
                //Toast.makeText(getBaseContext(), "Now selected : " +s, Toast.LENGTH_SHORT).show();
                userType = stickySwitch.getText();
            }
        });
        userType = stickySwitch.getText();
        SharedPreferences preferences = getSharedPreferences("userType",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("pUserType", userType);
        editor.apply();
    }

    private void openRegister() {
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


