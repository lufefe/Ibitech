package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class NavHeader extends AppCompatActivity {

    TextView tvProfileName;
    String name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nav_header_main);

        tvProfileName = findViewById(R.id.tv_profileName);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        name = prefs.getString("pName","");

        tvProfileName.setText(name);
    }
}
