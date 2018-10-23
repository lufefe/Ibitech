package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.ConditionListAdapter;
import com.divide.ibitech.divide_ibitech.Adapter.NextOfKinAdapter;
import com.divide.ibitech.divide_ibitech.Models.ConditionList;
import com.divide.ibitech.divide_ibitech.Models.NextOfKinList;
import com.divide.ibitech.divide_ibitech.Models.PatientsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewNextOfKin extends AppCompatActivity {

    ListView listView;
    String id = "";
    List<NextOfKinList> nextList;

    TextView tvNoNext;
    ImageView ivNoNext;

    String URLGETNXTKIN = "http://10.0.2.2/app/getnextofkin.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_next_of_kin);

        listView = findViewById(R.id.listNextOfKin);
        nextList = new ArrayList<>();

        tvNoNext = findViewById(R.id.tvNoNext);
        ivNoNext = findViewById(R.id.imgNoNext);

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Your Next Of Kin");
        setSupportActionBar(toolbar);

        Showlist(id);
    }
    private  void  Showlist(final String id){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETNXTKIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                            //Parallel arrays
                            final String[] nextOfKinID = new String[jsonArray.length()];
                            final String[] first_name = new String[jsonArray.length()];
                            final String[] surname = new String[jsonArray.length()];
                            final String[] dob = new String[jsonArray.length()];
                            final String[] email = new String[jsonArray.length()];
                            final String[] cellphone = new String[jsonArray.length()];
                            final String[] address = new String[jsonArray.length()];
                            final String[] relation = new String[jsonArray.length()];

                            for (int x= 0; x < jsonArray.length(); x++){
                                JSONObject nextObject = jsonArray.getJSONObject(x);

                                nextOfKinID[x] = nextObject.getString("next_of_kin_id");
                                first_name[x] = nextObject.getString("first_name");
                                surname[x] = nextObject.getString("surname");
                                dob[x] = nextObject.getString("dob");
                                email[x] = nextObject.getString("email_address");
                                cellphone[x] = nextObject.getString("cellphone_number");
                                address[x] = nextObject.getString("address_line");
                                relation[x] = nextObject.getString("relation");


                                NextOfKinList next = new NextOfKinList(nextObject.getString("first_name"),
                                        nextObject.getString("surname"),
                                        nextObject.getString("cellphone_number"),
                                        nextObject.getString("relation"));
                                nextList.add(next);

                            }
                            NextOfKinAdapter adapter = new NextOfKinAdapter(nextList, getApplication());
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {

                                    for (int i =0; i < nextOfKinID.length; i++){
                                        if (parent.getItemIdAtPosition(position) == i) {
                                            SharedPreferences preferences = getSharedPreferences("PATIENTNEXTOFKIN", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pNextOfKinID",nextOfKinID[i]);
                                            editor.putString("pFirstName", first_name[i]);
                                            editor.putString("pSurname", surname[i]);
                                            editor.putString("pDOB", dob[i]);
                                            editor.putString("pEmail", email[i]);
                                            editor.putString("pCellphone", cellphone[i]);
                                            editor.putString("pAddress", address[i]);
                                            editor.putString("pRelation", relation[i]);
                                            editor.apply();
                                            startActivity(new Intent(ViewNextOfKin.this, NextOfKinDetails.class));
                                        }
                                    }

                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            ivNoNext.setVisibility(View.VISIBLE);
                            tvNoNext.setVisibility(View.VISIBLE);
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewNextOfKin.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                HashMap<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
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
            startActivity(new Intent(ViewNextOfKin.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
