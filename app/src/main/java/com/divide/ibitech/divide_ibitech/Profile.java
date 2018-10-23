package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    String id ="", name="",surname="", status="", cell="", email="",weight="",height="",medicalAid="",profilePic="";

    EditText etName, etSurname, etEmail, etCellphone, etWeight, etHeight;

    ImageView img_ProfilePic;

    Button btnSave;

    LinearLayout llMedicalAid, llNextOfKin;

    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences prefs = getSharedPreferences("PROFILEPREFS", MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        etName = findViewById(R.id.editName);
        etSurname = findViewById(R.id.editSurname);
        //tvAdrress = findViewById(R.id.tv_address);
        //tvMedicalAid = findViewById(R.id.tv_medicalAid);
        etEmail = findViewById(R.id.editEmail);
        etCellphone = findViewById(R.id.editCell);
        //tvMarital = findViewById(R.id.tv_marital);
        etWeight = findViewById(R.id.editWeight);
        etHeight = findViewById(R.id.editHeight);

        llMedicalAid = findViewById(R.id.ll_MedicalAid);
        llNextOfKin = findViewById(R.id.ll_NextOfKin);

        img_ProfilePic = findViewById(R.id.imgProfilePicInProfile);

        id = prefs.getString("pID","");
        name = prefs.getString("pFirstName","");
        surname = prefs.getString("pSurname","");
        status = prefs.getString("pStatus","");
        cell = prefs.getString("pCell","");
        email = prefs.getString("pEmail","");
        weight = prefs.getString("pWeight","");
        height = prefs.getString("pHeight","");
        profilePic = prefs.getString("pProfilePic","");
        medicalAid = prefs.getString("pMedicalAid","");


        etName.setText(name);
        etSurname.setText(surname);
        //tvMedicalAid.setText(medicalAid);
        etEmail.setText(email);
        etCellphone.setText(cell);
        //tvMarital.setText(status);
        etWeight.setText(weight);
        etHeight.setText(height);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        img_ProfilePic.setImageDrawable(roundedBitmapDrawable);

        btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name, surname, cell, email, weight, height;
                name = etName.getText().toString();
                surname = etSurname.getText().toString();
                cell = etCellphone.getText().toString();
                email = etEmail.getText().toString();
                weight = etWeight.getText().toString();
                height = etHeight.getText().toString();
                updateProfile(id,name,surname,cell,email,weight,height);
            }
        });

        llMedicalAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, AddMedicalAid.class));
            }
        });

        llNextOfKin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Profile.this, AddNextOfKin.class));

            }
        });
    }

    private void updateProfile(final String id ,final String name,final String surname,final String cell,final String email,final String weight,final String height) {

        String URL_UPDATE = "http://sict-iis.nmmu.ac.za/ibitech/app/updateprofile.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(Profile.this,"Profile updated successfully.",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Profile.this,"There was a problem updating your profile, try again later." + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Profile.this,"There was an internal error, please try again later." + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("name",name);
                params.put("surname",surname);
                params.put("cell",cell);
                params.put("email",email);
                params.put("weight",weight);
                params.put("height",height);
                return params;
            }
        };
        Singleton.getInstance(Profile.this).addToRequestQue(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            startActivity(new Intent(Profile.this,Dashboard.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile){
            Intent searchIntent = new Intent(Profile.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.reports){
            Intent searchIntent = new Intent(Profile.this, PatientGraphReports.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.tutorial){
            Intent searchIntent = new Intent(Profile.this, Tutorial.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.settings){
            Intent searchIntent = new Intent(Profile.this, com.divide.ibitech.divide_ibitech.Settings.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
       else if (id == R.id.logout){
            sessionManager.logout();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_drawer, menu);
        return true;
        //return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }
}
