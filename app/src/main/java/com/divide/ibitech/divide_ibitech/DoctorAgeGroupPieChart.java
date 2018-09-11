package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class DoctorAgeGroupPieChart extends AppCompatActivity {
    String count="23";
    String condition1="Children";

    String condition2="Teenagers";
    String condition6="Youth";

    String condition3="Middle age";
    String condition4="Elders";



    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_age_group_piechart);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Patients by age groups");
        setSupportActionBar(toolbar);

        TextView tv_Date = findViewById(R.id.tvDate);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));
        pieChart=(PieChart)findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        ArrayList<PieEntry> yValues= new ArrayList<>();
        yValues.add(new PieEntry(14f,condition6));
       // yValues.add(new PieEntry(14f,condition5));
        yValues.add(new PieEntry(16f,condition4));

        yValues.add(new PieEntry(30,condition3));

        yValues.add(new PieEntry(27,condition2));
        yValues.add(new PieEntry(23,condition1));

        //Description description = new Description();
        //  description.setText("Patient Condition");
       // description.setTextSize(25);
//description.setTextAlign(condition1.charAt(5));
     //   pieChart.setDescription(description);
        pieChart.animateX(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues,"Age Groups");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);

    }

    private static int calculateAge(Date dob){
        Calendar dateOB = Calendar.getInstance();
        dateOB.setTime(dob);

        Calendar currentDate = Calendar.getInstance();
        currentDate.setTime(new Date());

        return currentDate.get(Calendar.YEAR) - dateOB.get(Calendar.YEAR);
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
            startActivity(new Intent(DoctorAgeGroupPieChart.this,DoctorGraphReports.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
