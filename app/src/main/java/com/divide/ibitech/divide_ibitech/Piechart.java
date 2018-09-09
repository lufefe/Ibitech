package com.divide.ibitech.divide_ibitech;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendPosition;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.github.mikephil.charting.utils.ColorTemplate.*;

public class Piechart extends AppCompatActivity {
//String country="Mzantsi";
String count="23";
    String condition1="Alzheimers";

    String condition2="Asthma";
    String condition6="Arthritis";

    String condition3="Hay fever";
    String condition4="Heart Desease";
    String condition5="HIV & AIDS";


    PieChart pieChart;
    private BarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);


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
yValues.add(new PieEntry(34f,condition6));
        yValues.add(new PieEntry(14f,condition5));
        yValues.add(new PieEntry(23f,condition4));

        yValues.add(new PieEntry(65,condition3));

        yValues.add(new PieEntry(40,condition2));
        yValues.add(new PieEntry(23,condition1));

//Description description = new Description();
//description.setText("Patient Condition");
//description.setTextSize(25);
//description.setTextAlign(condition1.charAt(5));
///pieChart.setDescription(description);
       pieChart.animateX(1000,Easing.EasingOption.EaseInOutCubic);

PieDataSet dataSet = new PieDataSet(yValues,"Patient Condition");
dataSet.setSliceSpace(3f);
dataSet.setSelectionShift(5f);
dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
PieData data = new PieData((dataSet));
data.setValueTextColor(Color.YELLOW);
pieChart.setData(data);



    }
}