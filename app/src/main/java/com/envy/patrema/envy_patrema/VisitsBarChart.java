package com.envy.patrema.envy_patrema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VisitsBarChart extends AppCompatActivity {

    BarChart mChart;
    TextView tvPatName;
    String patName="", patID="";

    String URL_GETPATVSTSREPORT = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientvisitsforreport.php";
    String URL_GETPATCONDSREPORT = "http://sict-iis.nmmu.ac.za/ibitech/app/getpatientconditionsforreport.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visits_bar_chart);

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        tvPatName = findViewById(R.id.patientName);

        patID = preferences.getString("pID","");
        patName = preferences.getString("pName", "");
        tvPatName.setText(patName);


        TextView  tv_Date = findViewById(R.id.tvDate);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        tv_Date.setText(dateFormat.format(date));

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("My Visits");
        setSupportActionBar(toolbar);

        getPatientVisits(patID);

        mChart = findViewById(R.id.bargraph);
        mChart.getDescription().setEnabled(false);


    }

    private void getPatientVisits(final String patID) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETPATCONDSREPORT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");
                            List<BarEntry> yValues= new ArrayList<>();

                            for (int i = 0; i < 4;i++){
                                JSONObject object = jsonArray.getJSONObject(i);
                                yValues.add(new BarEntry(3f,50f));
                                //yValues.add(new BarEntry(Integer.parseInt(object.getString("condition_total")),(int) value));

                            }

                            BarDataSet set = new BarDataSet(yValues, "Visits Frequency");
                            set.setColors(ColorTemplate.MATERIAL_COLORS);
                            set.setDrawValues(true);

                            BarData data = new BarData(set);

                            mChart.setData(data);
                            mChart.setFitBars(true);
                            mChart.invalidate();
                            mChart.animateY(500);


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(VisitsBarChart.this,"Error"+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(VisitsBarChart.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("id",patID);
                return params;
            }
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
            startActivity(new Intent(VisitsBarChart.this,PatientGraphReports.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
