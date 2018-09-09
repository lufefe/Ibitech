package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Patient_Condition_Chart extends AppCompatActivity {
    String count="23";


    String condition2=" Asthma ";
    String condition6="Hay fever";

    String condition3="Hear Deasese";
  //  TextView tv_Date;


    PieChart pieChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient__condition__chart);
        TextView  tv_Date = findViewById(R.id.tvDate);
        pieChart=(PieChart)findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
        ArrayList<PieEntry> yValues= new ArrayList<>();
        yValues.add(new PieEntry(54f,condition6));
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        TextView patname = findViewById(R.id.patientName);
        String value = preferences.getString("Fullname", "");
        patname.setText(value);

        yValues.add(new PieEntry(6,condition3));

        yValues.add(new PieEntry(40,condition2));


       // Description description = new Description();
        //  description.setText("Patient Condition");
        //escription.setTextSize(25);
//description.setTextAlign(condition1.charAt(5));
        //pieChart.setDescription(description);
        pieChart.animateX(1000, Easing.EasingOption.EaseInOutCubic);

        PieDataSet dataSet = new PieDataSet(yValues,"Condition");
        dataSet.setSliceSpace(3f);
        dataSet.setSelectionShift(5f);
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData data = new PieData((dataSet));
        data.setValueTextColor(Color.YELLOW);
        pieChart.setData(data);
    }
}
