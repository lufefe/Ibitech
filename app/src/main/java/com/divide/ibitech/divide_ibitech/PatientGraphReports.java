package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class PatientGraphReports extends AppCompatActivity {
String items []= new String[]{"My Conditions","My Allergies","Medical Devices",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_patient_graph_reports);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Reports");
        setSupportActionBar(toolbar);

        ListView listview = findViewById(R.id.listReport);
        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,items);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent intent= new Intent(view.getContext(),PatientConditionsPieChart.class);
                    startActivity(intent);
                }
                else if (position==1)
                {
                    Intent intent= new Intent(view.getContext(),PatientAllergiesPieChart.class);
                    startActivity(intent);

                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.doc_nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(PatientGraphReports.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
