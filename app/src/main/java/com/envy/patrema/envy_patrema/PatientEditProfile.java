package com.envy.patrema.envy_patrema;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioGroup;
import android.widget.Toast;

import fr.ganfra.materialspinner.MaterialSpinner;

public class PatientEditProfile extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    MaterialSpinner sp_MaritalStatus, sp_Province;
    RadioGroup rgGender;
    String gender = "", maritalStatus = "", province = "";
    android.support.v7.widget.Toolbar toolbar;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;

            case R.id.btnSaveProfile:
                Toast.makeText(getApplicationContext(), "Profile Saved", Toast.LENGTH_LONG).show();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_button, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

        toolbar = findViewById(R.id.tbEditProfile);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        rgGender = findViewById(R.id.rg_Gender);
        rgGender.setOnCheckedChangeListener(this);

        sp_Province = findViewById(R.id.provinceSpinner);
        String[] PROVINCES = {"EC", "WC", "MP", "NW", "NC","KZN","GP","LP","FS"};
        ArrayAdapter<String> provAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PROVINCES);
        provAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Province.setAdapter(provAdapter);

        sp_Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                province = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_MaritalStatus = findViewById(R.id.statusSpinner);
        String[] STATUS = {"Single", "Married", "Divorced", "Widowed", "Separated"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, STATUS);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_MaritalStatus.setAdapter(statusAdapter);

        sp_MaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rbFemale:
                gender = "Female";
                break;

            case R.id.rbMale:
                gender = "Male";
                break;
        }

    }
}
