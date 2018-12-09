package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NextOfKinDetails extends AppCompatActivity {

    TextView tvNextName, tvDOB, tvCellNo, tvEmail, tvAddress;
    String name ="", surname="", dob="", cellNo="", email="",address="", nextID="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_of_kin_details);

        tvNextName = findViewById(R.id.tvNextName);
        tvDOB = findViewById(R.id.tvDateOfBirth);
        tvCellNo = findViewById(R.id.tvCellNo);
        tvEmail = findViewById(R.id.tvEmailAddress);
        tvAddress = findViewById(R.id.tvAddress);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Next Of Kin Details");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("PATIENTNEXTOFKIN", MODE_PRIVATE);

        name = prefs.getString("pFirstName", "") + " " + prefs.getString("pSurname", "");
        dob = prefs.getString("pDOB", "");
        cellNo = prefs.getString("pCellphone", "");
        email = prefs.getString("pEmail", "");
        address = prefs.getString("pAddress", "");
        nextID = prefs.getString("pNextOfKinID", "");

        tvNextName.setText(name);
        tvDOB.setText(dob);
        tvCellNo.setText(cellNo);
        tvEmail.setText(email);
        tvAddress.setText(address);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(NextOfKinDetails.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
