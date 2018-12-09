package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DoctorGraphReports extends AppCompatActivity {
    String items []= new String[]{"Conditions","Allergies","Patient Age Groups"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_doctor_graph_reports);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Reports");
        setSupportActionBar(toolbar);

        ListView listview = findViewById(R.id.listReport);
        ArrayAdapter<String> adapter= new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent intent= new Intent(view.getContext(),DoctorConditionsPieChart.class);
                    startActivity(intent);

                }
                else if (position==1) {

                    Intent intent= new Intent(view.getContext(),DoctorAllergiesPieChart.class);
                    startActivity(intent);

                }
                else {
                        Intent intent= new Intent(view.getContext(),DoctorAgeGroupPieChart.class);
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
            startActivity(new Intent(DoctorGraphReports.this,DocDashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
