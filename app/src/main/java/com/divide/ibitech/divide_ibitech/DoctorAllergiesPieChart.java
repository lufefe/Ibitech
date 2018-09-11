package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DoctorAllergiesPieChart extends AppCompatActivity {

    PieChart pieChart;

    String URL_GETALLERGIESREPORT = "http://sict-iis.nmmu.ac.za/ibitech/app/getallergiesforreport.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allergies_piechart);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Allergies");
        setSupportActionBar(toolbar);

        getAllergies();

        TextView tv_Date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));

        pieChart = findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5,10,5,5);
        pieChart.setDragDecelerationFrictionCoef(0.99f);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);
        pieChart.setTransparentCircleRadius(61f);
    }

    private void getAllergies() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETALLERGIESREPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                            ArrayList<PieEntry> yValues= new ArrayList<>();

                            for (int i = 0; i < jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                yValues.add(new PieEntry(Integer.parseInt(object.getString("allergy_total")),object.getString("allergy_name")));

                            }

                            pieChart.animateX(1000,Easing.EasingOption.EaseInOutCubic);

                            PieDataSet dataSet = new PieDataSet(yValues,"Allergies Legend");
                            dataSet.setSliceSpace(3f);
                            dataSet.setSelectionShift(5f);
                            dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                            PieData data = new PieData((dataSet));
                            data.setValueTextColor(Color.YELLOW);
                            pieChart.setData(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(DoctorAllergiesPieChart.this,"Error"+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DoctorAllergiesPieChart.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

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
            startActivity(new Intent(DoctorAllergiesPieChart.this,DoctorGraphReports.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
