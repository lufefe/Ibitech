package com.envy.patrema.envy_patrema;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MedicalDevice extends AppCompatActivity {

    TextView tvDeviceName, tvInstructions, tvStatus, tvReqDate;
    String deviceName="",instructions="", status="", requestDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medical_device);

        tvDeviceName = findViewById(R.id.tvDeviceName);
        tvInstructions = findViewById(R.id.tvInstructions);
        tvStatus = findViewById(R.id.tvDeviceStatus);
        tvReqDate = findViewById(R.id.tvDeviceDate);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Device Information");
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getSharedPreferences("PATIENTDEVICES", MODE_PRIVATE);

        deviceName = preferences.getString("pDeviceName", "");
        instructions = preferences.getString("pInstructions", "");
        status = preferences.getString("pStatus", "");
        requestDate = preferences.getString("pRequestDate", "");

        tvDeviceName.setText(deviceName);
        tvInstructions.setText(instructions);
        tvStatus.setText(status);
        tvReqDate.setText(requestDate);


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
