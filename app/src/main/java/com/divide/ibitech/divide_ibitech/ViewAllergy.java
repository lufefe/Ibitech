package com.divide.ibitech.divide_ibitech;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.AllergyListAdapter;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;
//import com.divide.ibitech.divide_ibitech.Models.ConditionList;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllergy extends AppCompatActivity {
    ListView listView;
    String URLAllergy = "http://sict-iis.nmmu.ac.za/ibitech/app-test/getallergys.php";
    String getAlleryToday = "http://sict-iis.nmmu.ac.za/ibitech/app/symptomToday.php";
String TodayAllergy="http://sict-iis.nmmu.ac.za/ibitech/app/TodayAllergy.php";
    /// String URLAllergyfilter = "http://sict-iis.nmmu.ac.za/ibitech/app/search.php";
    String id = "";
    TextView input_search;
    private FloatingActionButton fab_Today, fab_Week, fabMonth,fab_clear;
    private FloatingActionMenu fab_menu ;
    List<AllergyList> alleList;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_allergy);
        listView = findViewById(R.id.listAllergy);
        alleList = new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        TextView patname = findViewById(R.id.patientName);
        String value = preferences.getString("Fullname", "");
        patname.setText(value);
//        String value = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","");
//        id = preferences.getString("pID","");
//        patname.setText(value);
//        //ShowList(id);
       // ShowList();
        fab_Today =(FloatingActionButton) findViewById(R.id.fabToday);
        fab_Week =(FloatingActionButton) findViewById(R.id.fabWeek);
        fab_menu =(FloatingActionMenu)findViewById(R.id.fabmenu) ;
//    }

        fab_Today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowListToday();
                alleList.clear();


            }
        });
        fab_Week.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listView.setAdapter(null);

                ShowList();
                alleList.clear();
            }
        });
    }
    private void ShowList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLAllergy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array =obj.getJSONArray("server_response");
                            for (int   x= 0;x<array.length();x++){
                                JSONObject condOBJ= array.getJSONObject(x);
                                AllergyList alle = new AllergyList(condOBJ.getString("allergy_name"),condOBJ.getString("date_added"));
                                alleList.add(alle);

                            }
                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
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
    private void ShowListToday() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, TodayAllergy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array =obj.getJSONArray("server_response");
                            for (int   x= 0;x<array.length();x++){
                                JSONObject condOBJ= array.getJSONObject(x);
                                AllergyList alle = new AllergyList(condOBJ.getString("allergy_name"),condOBJ.getString("date_added"));
                                alleList.add(alle);

                            }
                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
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

//    private void ShowList(final String id) {
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLAllergy,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        try {
//                            JSONObject obj = new JSONObject(response);
//                            JSONArray array =obj.getJSONArray("server_response");
//                            for (int   x= 0;x<array.length();x++){
//                                JSONObject condOBJ= array.getJSONObject(x);
//                                AllergyList allergy = new AllergyList(condOBJ.getString("allergy_name"),condOBJ.getString("date_added"));
//                                alleList.add(allergy);
//
//                            }
//                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
//                            listView.setAdapter(adapter);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            Toast.makeText(ViewAllergy.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//                Toast.makeText(ViewAllergy.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
//            }
//        }){
//
//            @Override
//            protected Map<String,String> getParams() {
//                Map<String,String> params = new HashMap<>();
//
//                params.put("id",id);
//                return params;
//            }
//
//        };
//        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
//    }
}