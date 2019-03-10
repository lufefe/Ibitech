package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class DoctorDashboard extends AppCompatActivity {

    TextView tv_DocName;
    ImageView imgProfilePic, imgEditProfile;
    ImageButton btnTutorial,btnReports;
    CardView cv_Visits, cv_Patients;
    Button btn_Logout;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_dashboard);

        sessionManager = new SessionManager(this);
        sessionManager.checkDocLogin();

        tv_DocName = findViewById(R.id.tvDocName);
        cv_Visits = findViewById(R.id.cvVisits);
        cv_Patients = findViewById(R.id.cvAllPatients);
        btn_Logout = findViewById(R.id.btnLogout);
        btnTutorial = findViewById(R.id.imgTutorial);
        btnReports = findViewById(R.id.docReports);

        imgProfilePic = findViewById(R.id.imgProfilePic);
        imgEditProfile = findViewById(R.id.imgEditImage);

        HashMap<String,String> doc = sessionManager.getDocDetails();
        String sName = doc.get(SessionManager.NAME);
        String sSurname = doc.get(SessionManager.SURNAME);

        tv_DocName.setText(String.format("Dr %s %s", sName, sSurname));

        btnTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(DoctorDashboard.this, DoctorTutorial.class));
            }
        });

        btnReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(DoctorDashboard.this,DoctorGraphReports.class));
            }
        });

        btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.doclogout();
            }
        });

        cv_Visits.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(DoctorDashboard.this, ViewPatientVisits.class));
           }
       });

       cv_Patients.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               startActivity(new Intent(DoctorDashboard.this, ViewPatients.class));
           }
       });

       imgEditProfile.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(getApplicationContext(), DoctorEditProfile.class));
           }
       });

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        imgProfilePic.setImageDrawable(roundedBitmapDrawable);

    }
}
