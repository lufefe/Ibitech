package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.AllergyListAdapter;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;
//import com.divide.ibitech.divide_ibitech.Models.ConditionList;
import com.divide.ibitech.divide_ibitech.Models.AllergyList;
import com.github.clans.fab.FloatingActionButton;

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
    String WeekAllergy = "http://sict-iis.nmmu.ac.za/ibitech/app/getAllergydateRangeMonth.php";
    String TodayAllergy = "http://sict-iis.nmmu.ac.za/ibitech/app/getAllergydateRangeWeek.php";

    FloatingActionButton fabToday,fabWeek;
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
//        fabToday=(FloatingActionButton)findViewById(R.id.fabtoday);
//        fabWeek=(FloatingActionButton)findViewById(R.id.fabWeek);
//        fabToday.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               showlistToday(id);
//                alleList.clear();
//
//              // listView.setAdapter(null);
//            }
//        });

//        fabWeek.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showlistWeek(id);
//                alleList.clear();
//                //listView.setAdapter(null);
//
//            }
//        });

        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);

        String name = preferences.getString("pFirstName","") + " " + preferences.getString("pSurname","");
        id = preferences.getString("pID","");

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(name + "\'s allergies");
        setSupportActionBar(toolbar);

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
            startActivity(new Intent(ViewAllergy.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    private void ShowList(final String id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLGETALLRGY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            JSONArray array = obj.getJSONArray("server_response");

                            //Parallel arrays
                            final String[] allergyID = new String[array.length()];
                            final String[] allergyName = new String[array.length()];
                            final String[] allergyType = new String[array.length()];
                            final String[] species = new String[array.length()];
                            final String[] dateAdded = new String[array.length()];
                            final String[] treatment = new String[array.length()];
                            final String[] tested = new String[array.length()];

                            for (int x= 0; x < array.length(); x++){
                                JSONObject allergyObject = array.getJSONObject(x);

                                allergyID[x] = allergyObject.getString("allergy_id");
                                allergyName[x] = allergyObject.getString("allergy_name");
                                allergyType[x] = allergyObject.getString("allergy_type");
                                species[x] = allergyObject.getString("species");
                                dateAdded[x] = allergyObject.getString("date_added");
                                treatment[x] = allergyObject.getString("treatment_id");
                                tested[x] = allergyObject.getString("tested");

                                AllergyList allergy = new AllergyList(allergyObject.getString("allergy_name"),
                                                                    allergyObject.getString("date_added"));
                                alleList.add(allergy);

                            }
                            AllergyListAdapter adapter =  new AllergyListAdapter(alleList,getApplication());
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                                    for (int i =0; i < allergyID.length; i++){
                                        if (parent.getItemIdAtPosition(position) == i) {
                                            SharedPreferences preferences = getSharedPreferences("PATIENTALLERGY", MODE_PRIVATE);
                                            SharedPreferences.Editor editor = preferences.edit();

                                            editor.putString("pAllergyID",allergyID[i]);
                                            editor.putString("pAllergyName", allergyName[i]);
                                            editor.putString("pAllergyType",allergyType[i]);
                                            editor.putString("pSpecies",species[i]);
                                            editor.putString("pDateAdded",dateAdded[i]);
                                            editor.putString("pTreatmentID",treatment[i]);
                                            editor.putString("pTested",tested[i]);
                                            editor.apply();
                                            startActivity(new Intent(ViewAllergy.this, PatientAllergy.class));
                                            finish();

                                        }
                                    }

                                }
                            });

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

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(ViewAllergy.this).addToRequestQue(stringRequest);
    }
    public  void showlistToday(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, TodayAllergy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("server_response");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject allergyObject = array.getJSONObject(i);

                                AllergyList allergy = new AllergyList(allergyObject.getString("allergy_name"),
                                        allergyObject.getString("date_added"));
                                alleList.add(allergy);

                            }
                            AllergyListAdapter adapter = new AllergyListAdapter(alleList, getApplication());
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewAllergy.this, "Error " + e.toString(), Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewAllergy.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }

        };
        Singleton.getInstance(ViewAllergy.this).addToRequestQue(stringRequest);

    }
    public  void showlistWeek(final String id){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, WeekAllergy,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject object = new JSONObject(response);
                            JSONArray array = object.getJSONArray("server_response");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject allergyObject = array.getJSONObject(i);

                                AllergyList allergy = new AllergyList(allergyObject.getString("allergy_name"),
                                        allergyObject.getString("date_added"));
                                alleList.add(allergy);

                            }
                            AllergyListAdapter adapter = new AllergyListAdapter(alleList, getApplication());
                            listView.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ViewAllergy.this, "You have no allergies inserted yet.", Toast.LENGTH_LONG).show();

                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewAllergy.this,"Error 2"+error.toString(),Toast.LENGTH_LONG).show();


            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();

                params.put("id",id);
                return params;
            }
        };
        Singleton.getInstance(ViewAllergy.this).addToRequestQue(stringRequest);

    }
}