package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListOfReports extends AppCompatActivity {
String items []= new String[]{"Condition","Allergy","Devices",};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_reports);

        ListView listview =(ListView)findViewById(R.id.listReport);
        ArrayAdapter <String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_expandable_list_item_1,items);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position==0){
                    Intent intent= new Intent(view.getContext(),Patient_Condition_Chart.class);
                    startActivity(intent);

                }
                else if (position==1)
                {

                    Intent intent= new Intent(view.getContext(),Patient_Allergy_Chart.class);
                    startActivity(intent);

                }
            }
        });

    }
}
