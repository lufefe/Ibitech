package com.divide.ibitech.divide_ibitech;

import android.arch.lifecycle.SingleGeneratedAdapterObserver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.ConditionListAdapter;
import com.divide.ibitech.divide_ibitech.Models.ConditionList;
//import com.divide.ibitech.divide_ibitech.Models.SymptomsList;
import com.divide.ibitech.divide_ibitech.Models.ConditionList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewCondition extends AppCompatActivity {
    ListView listView;
    String URLGETCONDS = "http://sict-iis.nmmu.ac.za/ibitech/app/getconditions.php";

    String id = "", fullname = "";

    List<ConditionList> condList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_condition);
        listView= findViewById(R.id.listCond);
        condList= new ArrayList<>();

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        fullname = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","") ;
        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(fullname + "\'s conditions");
        setSupportActionBar(toolbar);

        Showlist();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(ViewCondition.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private  void  Showlist(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLGETCONDS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");
                            for (int   x= 0;x<array.length();x++){
                                JSONObject condOBJ= array.getJSONObject(x);
                                ConditionList co = new ConditionList(condOBJ.getString("condition_name"),condOBJ.getString("visit_date"));
                                condList.add(co);

                            }
                            ConditionListAdapter adapter= new ConditionListAdapter(condList,getApplication());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewCondition.this,"Error"+e.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewCondition.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){



        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
}