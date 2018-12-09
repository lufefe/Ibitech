package com.envy.patrema.envy_patrema;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

public class DoctorTutorial extends AppCompatActivity {
    private int[] layouts={R.layout.slide_doc_two,R.layout.slide_doc_three,R.layout.slide_doc_four, R.layout.slide_doc_five};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_tutorial);

        ViewPager mPager = findViewById(R.id.docViewPager);
        com.envy.patrema.envy_patrema.Adapter.DoctorTutorialAdapter doctorViewPage = new com.envy.patrema.envy_patrema.Adapter.DoctorTutorialAdapter(layouts, this);
        mPager.setAdapter(doctorViewPage);
    }
}