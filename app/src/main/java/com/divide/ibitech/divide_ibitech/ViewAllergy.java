package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewAllergy extends AppCompatActivity {
    ListView listView;
    String URLGETALLRGY = "http://sict-iis.nmmu.ac.za/ibitech/app/getallergy.php";
    String URLAllergyfilter = "http://sict-iis.nmmu.ac.za/ibitech/app/search.php";
    String id = "";
    TextView input_search;
    List<AllergyList> alleList;
    SessionManager sessionManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_allergy);
        listView=findViewById(R.id.listAllergy);
        alleList= new ArrayList<>();
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        String name = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","");
        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name + "\'s allergies");
        setSupportActionBar(toolbar);

        ShowList();
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
            startActivity(new Intent(ViewAllergy.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowList() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLGETALLRGY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");

                            for (int x= 0; x < array.length(); x++){
                                JSONObject condOBJ= array.getJSONObject(x);

                                AllergyList allergy = new AllergyList(condOBJ.getString("allergy_name"),
                                                                    condOBJ.getString("date_added"));
                                alleList.add(allergy);

                            }
                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
                            listView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewAllergy.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewAllergy.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){

           /* @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }*/

        };
        Singleton.getInstance(ViewAllergy.this).addToRequestQue(stringRequest);
    }
}