package com.envy.patrema.envy_patrema;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddNextOfKin extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    EditText KinID,KinName,KinSname,KinCell,KinEmail,KinAddress,KinRelation,KinDate,Kinststus;
    Button btnSave;
    String idNo = "";
    String  kID,kname,kSname,kDate,kCell,kEmail,kAddress,kRelation ,kStatus;
    String URL="http://sict-iis.nmmu.ac.za/ibitech/app/new.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_of_kin);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Add A Next Of Kin");
        setSupportActionBar(toolbar);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);
        idNo = prefs.getString("pID","");
        KinID = findViewById(R.id.kinId);
        KinName = findViewById(R.id.Fname);
        KinSname = findViewById(R.id.KinSname);
        KinCell =findViewById(R.id.KinNo);
        KinEmail = findViewById(R.id.KinEmail);
        KinAddress = findViewById(R.id.KinAddress);
        KinRelation =findViewById(R.id.KinRelation);
        KinDate = findViewById(R.id.kinDob);
        KinDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus){
                    datePicker(view);
                }
                else{
                    //do nothing
                }
            }
        });


        btnSave=findViewById(R.id.btnNet);



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                kID = KinID.getText().toString();
                kname = KinName.getText().toString();
                kSname = KinSname.getText().toString();
                kCell = KinCell.getText().toString();
                kEmail = KinEmail.getText().toString();
                kAddress = KinAddress.getText().toString();
                kRelation = KinRelation.getText().toString();
                kDate = KinDate.getText().toString();
                kStatus = KinID.getText().toString();
                Date d = new Date(kDate);
                String format = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).format(d);

                addKids(kID,kname,kSname,format,kCell,kEmail,kAddress,kRelation,idNo);

            }

        });

    }

    private void datePicker(View view) {
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year,month,day);
        setDate(calendar);
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        KinDate.setText(dateFormat.format(calendar.getTime()));
    }
    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
        }
    }

    private void addKids(final String kID, final String kname, final String kSname,final String kDate,final String kCell,final String kEmail,final String kAddress,final String kRelation,final String idNo) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")) {
                        Toast.makeText(AddNextOfKin.this, "Next Of Kin successfully added.", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(AddNextOfKin.this,Dashboard.class));
                        finish();
                    }
                    else {
                        Toast.makeText(AddNextOfKin.this, "There was an error in adding your Next Of Kin, try again later.", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(AddNextOfKin.this, "JSON Error" + e.toString(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AddNextOfKin.this,"Volley Error"+error.toString(),Toast.LENGTH_LONG).show();

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params = new HashMap<>();

                params.put("KinID",kID);
                params.put("KinName", kname);
                params.put("KinSName",kSname);
                params.put("Kindate",kDate);
                params.put("Kincell",kCell);
                params.put("KinEmail",kEmail);
                params.put("KinAdd",kAddress);
                params.put("KinRelation",kRelation);
                //  params.put("Kindate",kDate);
                params.put("patID",idNo);

                return params;
            }
        };

        Singleton.getInstance(AddNextOfKin.this).addToRequestQue(stringRequest);

    }

}
