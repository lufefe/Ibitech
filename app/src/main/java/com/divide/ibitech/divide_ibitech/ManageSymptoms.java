package com.divide.ibitech.divide_ibitech;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.divide.ibitech.divide_ibitech.Adapter.MyAdapter;

public class ManageSymptoms extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_symptoms);



        String[] symptoms = {"Headache","Acne","Sore throat"};
        ListAdapter adapter = new ManageSymptomsCustomAdapter(this, symptoms);
        ListView listView = findViewById(R.id.lv_manageSymptoms);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String symptom = String.valueOf(parent.getItemAtPosition(position));
                Toast.makeText(ManageSymptoms.this, symptom, Toast.LENGTH_SHORT).show();
            }
        });



    }

}
