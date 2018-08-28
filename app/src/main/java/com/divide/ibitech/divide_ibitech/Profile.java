package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    String id, name, status, cell, email,weight,height,medicalAid,profilePic;

    EditText tvName, tvAdrress, tvEmail, tvCellphone, tvMarital, tvWeight,tvHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        tvName = findViewById(R.id.editName);
        //tvAdrress = findViewById(R.id.tv_address);
        //tvMedicalAid = findViewById(R.id.tv_medicalAid);
        tvEmail = findViewById(R.id.editEmail);
        tvCellphone = findViewById(R.id.editCell);
        //tvMarital = findViewById(R.id.tv_marital);
        tvWeight = findViewById(R.id.editWeight);
        tvHeight = findViewById(R.id.editHeight);

        id = prefs.getString("pID","");
        name = prefs.getString("pName","");
        status = prefs.getString("pStatus","");
        cell = prefs.getString("pCell","");
        email = prefs.getString("pEmail","");
        weight = prefs.getString("pWeight","");
        height = prefs.getString("pHeight","");
        profilePic = prefs.getString("pProfilePic","");
        medicalAid = prefs.getString("pMedicalAid","");


        tvName.setText(name);
//        tvMedicalAid.setText(medicalAid);
        tvEmail.setText(email);
        tvCellphone.setText(cell);
        //tvMarital.setText(status);
        tvWeight.setText(weight);
        tvHeight.setText(height);




    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(Profile.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile){
            Intent searchIntent = new Intent(Profile.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.reports){
            Intent searchIntent = new Intent(Profile.this, Reports.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.settings){
            Intent searchIntent = new Intent(Profile.this, com.divide.ibitech.divide_ibitech.Settings.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.help){
            Intent searchIntent = new Intent(Profile.this, com.divide.ibitech.divide_ibitech.Help.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
}
