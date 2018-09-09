package com.divide.ibitech.divide_ibitech;
/**
 * Created by s216100801
 */
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.SymptomListAdapter;
import com.divide.ibitech.divide_ibitech.Models.SymptomList;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewSymptom extends AppCompatActivity {
    ListView listView;
    String URLgetSymptoms = "http://sict-iis.nmmu.ac.za/ibitech/app/getSymptoms.php";
    private FloatingActionButton fab_Today, fab_Week, fabMonth,fab_clear;
    private FloatingActionMenu fab_menu ;

    String getSymptomsTodaty = "http://sict-iis.nmmu.ac.za/ibitech/app/symptomToday.php";
  //  String URLgetSymptoms = "http://sict-iis.nmmu.ac.za/ibitech/app/getSymptoms.php";

    List <SymptomList> sympLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptom);
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        TextView patname = (TextView) findViewById(R.id.patientName);
        String value =preferences.getString("Fullname","");
        patname.setText(value);
        listView=(ListView)findViewById(R.id.listSymp);
        sympLists= new ArrayList<>();

      // ShowList();
        fab_Today =(FloatingActionButton) findViewById(R.id.fabToday);
        fab_Week =(FloatingActionButton) findViewById(R.id.fabWeek);
       fab_menu =(FloatingActionMenu)findViewById(R.id.fabmenu) ;



        fab_Today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListToday();
                sympLists.clear();


            }
        });
        fab_Week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);

                ShowList();
                sympLists.clear();
            }
        });

    }
    private  void  ShowList(){

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLgetSymptoms,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array=obj.getJSONArray("server_response");
                            for (int i = 0;i< array.length();i++){
                                JSONObject  sympObj = array.getJSONObject(i);
                                SymptomList Si = new SymptomList(sympObj.getString("symptom_name"),sympObj.getString("date_added"));
                                sympLists.add(Si);
                            }
                            SymptomListAdapter  adapter = new SymptomListAdapter(sympLists,getApplication());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){


        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
private  void ShowListToday(){



    StringRequest stringRequest = new StringRequest(Request.Method.GET, getSymptomsTodaty,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject obj = new JSONObject(response);
                        JSONArray array=obj.getJSONArray("server_response");
                        for (int i = 0;i< array.length();i++){
                            JSONObject  sympObj = array.getJSONObject(i);
                            SymptomList Si = new SymptomList(sympObj.getString("symptom_name"),sympObj.getString("date_added"));
                            sympLists.add(Si);
                        }
                        SymptomListAdapter  adaptertoday = new SymptomListAdapter(sympLists,getApplication());
                        listView.setAdapter(adaptertoday);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {

        }
    }){


    };
    Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
}

}