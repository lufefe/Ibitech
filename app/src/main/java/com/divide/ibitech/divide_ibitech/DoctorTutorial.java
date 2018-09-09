package com.divide.ibitech.divide_ibitech;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.divide.ibitech.divide_ibitech.R;

public class DoctorTutorial extends AppCompatActivity {
    private ViewPager mPager;
    private int[] layouts={R.layout.slide_doc_one,R.layout.slide_doc_two,R.layout.slide_doc_three,R.layout.slide_doc_four, R.layout.slide_doc_five};
    private com.divide.ibitech.divide_ibitech.Adapter.DoctorTutorialAdapter doctorViewPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_tutorial);
        mPager=(ViewPager)findViewById(R.id.docViewPager);
        doctorViewPage= new com.divide.ibitech.divide_ibitech.Adapter.DoctorTutorialAdapter(layouts,this);
        mPager.setAdapter(doctorViewPage);
    }
}