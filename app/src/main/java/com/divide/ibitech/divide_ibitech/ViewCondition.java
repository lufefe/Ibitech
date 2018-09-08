package com.divide.ibitech.divide_ibitech;

import android.arch.lifecycle.SingleGeneratedAdapterObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ShowableListMenu;
import android.widget.ListView;

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
    String URLgetSymptoms = "http://sict-iis.nmmu.ac.za/ibitech/app-test/getSymptoms.php";

    List<ConditionList> condList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_condition);
        listView= (ListView)findViewById(R.id.listCond);
        condList= new ArrayList<>();
        Showlist();
    }
    private  void  Showlist(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLgetSymptoms,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");
                            for (int   x= 0;x<array.length();x++){
                                JSONObject condOBJ= array.getJSONObject(x);
                                ConditionList co = new ConditionList(condOBJ.getString("condition_name"),condOBJ.getString("date_added"));
                                condList.add(co);

                            }
                            ConditionListAdapter adapter= new ConditionListAdapter(condList,getApplication());
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
}