package com.divide.ibitech.divide_ibitech;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class Tutorial extends AppCompatActivity {

    SessionManager sessionManager;

    private int[] layouts={R.layout.slide_zero,R.layout.slide_one,R.layout.slide_two,R.layout.slide_three,R.layout.slide_four,R.layout.slide_five,R.layout.slide_six,R.layout.slide_seven,R.layout.slide_eight,R.layout.slide_nine,R.layout.slide_ten};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        ViewPager mPager = findViewById(R.id.viewPager);
        com.divide.ibitech.divide_ibitech.Adapter.TutorialAdapter mpageAdapter = new com.divide.ibitech.divide_ibitech.Adapter.TutorialAdapter(layouts, this);
        mPager.setAdapter(mpageAdapter);
    }
}
