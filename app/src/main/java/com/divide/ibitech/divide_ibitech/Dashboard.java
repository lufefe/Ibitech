package com.divide.ibitech.divide_ibitech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    TextView tv_FullName, tv_Age, tv_BloodType, tv_Address,tv_Gender,tv_MaritalStatus;
    ImageView img_ProfilePic;
    private Bitmap bitmap;
    String getId;
    CardView btnManageAllergies, btnManageDevices, btnManageSymptoms, btnManageConditions, btnManageMedicalAid, btnManageNextOfKin;
    private static final String TAG = Dashboard.class.getSimpleName(); //getting the info

    SessionManager sessionManager;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_dashboard){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.profile){
            Intent searchIntent = new Intent(Dashboard.this, Profile.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }else if(id == R.id.reports){
            Intent searchIntent = new Intent(Dashboard.this, PatientGraphReports.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.tutorial){
            Intent searchIntent = new Intent(Dashboard.this, Tutorial.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.settings){
            Intent searchIntent = new Intent(Dashboard.this, com.divide.ibitech.divide_ibitech.Settings.class);
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
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //    for shared preferences
        SharedPreferences preferences = getSharedPreferences("PROFILEPREFS",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,drawer,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        tv_FullName = findViewById(R.id.tvName);
        tv_Age = findViewById(R.id.age);
        tv_BloodType = findViewById(R.id.bloodType);
        tv_Address = findViewById(R.id.tvAddress);
        tv_Gender = findViewById(R.id.gender);
        tv_MaritalStatus = findViewById(R.id.maritalStatus);

        img_ProfilePic = findViewById(R.id.imgProfilePic);
        //btn_photo_upload = findViewById(R.id.btnPhoto);

        FloatingActionButton fab_Symptoms = findViewById(R.id.fabSymptoms);
        FloatingActionButton fabRequestDevice = findViewById(R.id.fabRequest);
        FloatingActionButton fab_Allergy = findViewById(R.id.fabAllergy);

        //For Dashboard display

        HashMap<String,String> user = sessionManager.getUserDetails();
        String sName = user.get(SessionManager.NAME);
        String sSurname = user.get(SessionManager.SURNAME);
        String sAge = user.get(SessionManager.AGE);
        String sBloodType = user.get(SessionManager.BLOODTYPE);
        String sGender = user.get(SessionManager.GENDER);
        String sStatus = user.get(SessionManager.STATUS);
        String sAddress = user.get(SessionManager.ADDRESS);

        tv_FullName.setText(String.format("%s %s", sName, sSurname));
        tv_Age.setText(sAge);
        tv_BloodType.setText(sBloodType);
        tv_Gender.setText(sGender);
        tv_MaritalStatus.setText(sStatus);
        tv_Address.setText(sAddress);

       // For Edit Profile
        String sID = user.get(SessionManager.ID);
        String sCell = user.get(SessionManager.CELLNUMBER);
        String sEmail = user.get(SessionManager.EMAIL);
        String sWeight = user.get(SessionManager.WEIGHT);
        String sHeight = user.get(SessionManager.HEIGHT);
        String sProfilePic = user.get(SessionManager.PROFILEPIC);
        String sMedicaAid = user.get(SessionManager.MEDICALAID);

        editor.putString("pID",sID);
        editor.putString("pName",sName + " " + sSurname);
        editor.putString("pFirstName",sName);
        editor.putString("pSurname", sSurname);
        editor.putString("pStatus",sStatus);
        editor.putString("pCell",sCell);
        editor.putString("pEmail",sEmail);
        editor.putString("pWeight", sWeight);
        editor.putString("pHeight",sHeight);
        editor.putString("pProfilePic",sProfilePic);
        editor.putString("pMedicalAid", sMedicaAid);
        editor.apply();

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.profilepic);
        RoundedBitmapDrawable roundedBitmapDrawable = RoundedBitmapDrawableFactory.create(getResources(),bitmap);
        roundedBitmapDrawable.setCircular(true);
        img_ProfilePic.setImageDrawable(roundedBitmapDrawable);


        fab_Symptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AddSymptom.class));
            }
        });


        fab_Allergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, AddAllergy.class));
            }
        });

        fabRequestDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, RequestDevice.class));
            }
        });

        btnManageAllergies = findViewById(R.id.cvAllergies);
        btnManageSymptoms = findViewById(R.id.cvSymptoms);
        btnManageConditions = findViewById(R.id.cvConditions);
        btnManageDevices = findViewById(R.id.cvDevices);
        btnManageMedicalAid = findViewById(R.id.cvMedicalAid);
        btnManageNextOfKin = findViewById(R.id.cvNextOfKin);

        btnManageConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewCondition.class));
            }
        });

        btnManageAllergies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewAllergy.class));
            }
        });

        btnManageSymptoms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewSymptom.class));
            }
        });

        btnManageDevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewMedicalDevices.class));
            }
        });
        btnManageMedicalAid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewMedicalAid.class));
            }
        });
        btnManageNextOfKin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Dashboard.this, ViewNextOfKin.class));
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),filePath);
                img_ProfilePic.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
            UploadPicture(getId,getStringImage(bitmap));
        }
    }

    private void UploadPicture(final String id,final String photo) {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading...");
        progressDialog.show();

        String URL_UPLOAD = "http://sict-iis.nmmu.ac.za/ibitech/app/upload.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Log.i(TAG, response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(Dashboard.this,"Success",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                            Toast.makeText(Dashboard.this,"Try Again" + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Dashboard.this,"Try Again" + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id",id);
                params.put("photo",photo);
                return params;
            }
        };
        Singleton.getInstance(Dashboard.this).addToRequestQue(stringRequest);
    }

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray,Base64.DEFAULT);
    }
}
