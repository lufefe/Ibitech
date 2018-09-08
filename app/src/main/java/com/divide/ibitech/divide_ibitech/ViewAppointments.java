package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.divide.ibitech.divide_ibitech.Adapter.DocAppointmentsAdapter;
import com.divide.ibitech.divide_ibitech.Models.AppointmentsList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewAppointments extends AppCompatActivity {

    ListView listView;
    List<AppointmentsList> apptsLists;
    String URL_GETAPPTS = "http://sict-iis.nmmu.ac.za/ibitech/app/getappointments.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_appointments);
        listView = findViewById(R.id.lv_ViewAppointments);
        apptsLists = new ArrayList<>();

        GetAppts();

    }

    private void GetAppts() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_GETAPPTS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("server_response");

                    //Parallel arrays
                    final String [] patientID = new String[jsonArray.length()];
                    final String [] patientName = new String[jsonArray.length()];
                    final String [] patientSurname = new String[jsonArray.length()];
                    String [] patientCell = new String[jsonArray.length()];

                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject object = jsonArray.getJSONObject(i);

                         patientID[i] = object.getString("patient_id");
                         patientName[i] = object.getString("first_name");
                         patientSurname[i] = object.getString("surname");
                         patientCell[i] = object.getString("cellphone_number");

                        AppointmentsList appts = new AppointmentsList(object.getString("first_name"),object.getString("surname"), object.getString("cellphone_number"));
                        apptsLists.add(appts);
                    }

                        DocAppointmentsAdapter adapter = new DocAppointmentsAdapter(getApplication(),apptsLists);
                        listView.setAdapter(adapter);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //String appt = String.valueOf(parent.getItemIdAtPosition(position));

                            for (int i =0; i < patientID.length; i++){
                                if (parent.getItemIdAtPosition(position) == i)
                                {
                                    //Toast.makeText(ViewAppointments.this, patientName[i], Toast.LENGTH_SHORT).show();
                                    SharedPreferences preferences = getSharedPreferences("DIAGNOSIS",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = preferences.edit();

                                    editor.putString("pID", patientID[i]);
                                    editor.putString("pName", patientName[i] + " " + patientSurname[i]);
                                    editor.apply();
                                    startActivity(new Intent(ViewAppointments.this, Diagnosis.class));
                                    finish();
                                }

                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(ViewAppointments.this,"Error "+e.toString(),Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ViewAppointments.this,"MJError "+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        Singleton.getInstance(ViewAppointments.this).addToRequestQue(stringRequest);

    }
}
