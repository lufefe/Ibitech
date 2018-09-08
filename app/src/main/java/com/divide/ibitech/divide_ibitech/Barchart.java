package com.divide.ibitech.divide_ibitech;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Barchart extends AppCompatActivity {
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_chart);

        pd = new ProgressDialog(Barchart.this);
        pd.setMessage("loading");


        load_data_from_server();
    }
    public void load_data_from_server() {
        pd.show();
        String URL = "http://sict-iis.nmmu.ac.za/ibitech/app/GRAPH.php";

        final BarChart barChart =  findViewById(R.id.barchart);
        final ArrayList<BarEntry> entries = new ArrayList<>();

        StringRequest stringRequest =new StringRequest(Request.Method.POST, URL
                , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {

                    JSONObject obj = new JSONObject(response);
                    JSONArray array = obj.getJSONArray("server_response");


                    for (int i = 0; i < array.length(); i++) {

                        JSONObject jsonobject = array.getJSONObject(i);

                        String allergyTotal = jsonobject.getString("AllergyTotal").trim();
                        entries.add(new BarEntry((Integer.parseInt(allergyTotal)),i));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Something went wrong." + e.toString(), Toast.LENGTH_LONG).show();

                }
                BarDataSet barDataSet = new BarDataSet(entries, "Allergies");
                barDataSet.setColor(Color.rgb(0, 82, 159));

                BarData data = new BarData(barDataSet);

                barChart.setData(data);

                barChart.animateXY(2000, 2000);
                barChart.invalidate();
                pd.hide();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null){
                    Toast.makeText(getApplicationContext(), "Something went wrong.", Toast.LENGTH_LONG).show();
                    pd.hide();

                }
            }
        }
        );
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

}