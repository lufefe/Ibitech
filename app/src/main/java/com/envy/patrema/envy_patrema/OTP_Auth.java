package com.envy.patrema.envy_patrema;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class OTP_Auth extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_auth);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Email Verification");
        setSupportActionBar(toolbar);

    }
}
