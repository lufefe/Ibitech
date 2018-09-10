package com.divide.ibitech.divide_ibitech;
/**
 * Created by s216100801
 */
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.SymptomListAdapter;
import com.divide.ibitech.divide_ibitech.Models.SymptomList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewSymptom extends AppCompatActivity {
    ListView listView;
    String URLGETSYMPTS = "http://sict-iis.nmmu.ac.za/ibitech/app/getsymptoms.php";

    String id = "", fullname = "";

    List <SymptomList> sympLists;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_symptom);
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        listView = findViewById(R.id.listSymp);

        fullname = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","") ;
        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(fullname + "\'s symptoms");
        setSupportActionBar(toolbar);

        sympLists= new ArrayList<>();
        ShowList(id);
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
            startActivity(new Intent(ViewSymptom.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void  ShowList(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETSYMPTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                            for (int i = 0; i < jsonArray.length();i++){
                                JSONObject object = jsonArray.getJSONObject(i);

                                SymptomList list = new SymptomList(object.getString("symptom_name"),
                                                                object.getString("date_added"));
                                sympLists.add(list);
                            }
                            SymptomListAdapter adapter = new SymptomListAdapter(sympLists,getApplication());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewSymptom.this,"Error"+e.toString(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewSymptom.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }
        }

    ;
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }
}