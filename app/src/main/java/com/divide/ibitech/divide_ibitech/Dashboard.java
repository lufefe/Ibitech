package com.divide.ibitech.divide_ibitech;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Dashboard extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    TextView tv_FullName, tv_Age, tv_BloodType, tv_Address,tv_Gender,tv_MaritalStatus;
    ImageView img_ProfilePic;
    //Button btn_Logout,btn_photo_upload;
    private Bitmap bitmap;
    String getId;
    LinearLayout bt,device;
    private static final String TAG = Dashboard.class.getSimpleName(); //getting the info

    private static String URL_UPLOAD = "http://sict-iis.nmmu.ac.za/ibitech/app/upload.php";

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
            Intent searchIntent = new Intent(Dashboard.this, Reports.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.settings){
            Intent searchIntent = new Intent(Dashboard.this, com.divide.ibitech.divide_ibitech.Settings.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        }
        else if (id == R.id.help){
            Intent searchIntent = new Intent(Dashboard.this, com.divide.ibitech.divide_ibitech.Help.class);
            startActivity(searchIntent);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
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

        bt = findViewById(R.id.manageCondition);
        device = findViewById(R.id.manageDevice);

        //For Dashboard display

        HashMap<String,String> user = sessionManager.getUserDetails();
        String sName = user.get(sessionManager.NAME);
        String sSurname = user.get(sessionManager.SURNAME);
        String sAge = user.get(sessionManager.AGE);
        String sBloodType = user.get(sessionManager.BLOODTYPE);
        String sGender = user.get(sessionManager.GENDER);
        String sStatus = user.get(sessionManager.STATUS);
        String sAddress = user.get(sessionManager.ADDRESS);

        tv_FullName.setText(String.format("%s %s", sName, sSurname));
        tv_Age.setText(sAge);
        tv_BloodType.setText(sBloodType);
        tv_Gender.setText(sGender);
        tv_MaritalStatus.setText(sStatus);
        tv_Address.setText(sAddress);

       // For Edit Profile
        String sID = user.get(sessionManager.ID);
        String sCell = user.get(sessionManager.CELLNUMBER);
        String sEmail = user.get(sessionManager.EMAIL);
        String sWeight = user.get(sessionManager.WEIGHT);
        String sHeight = user.get(sessionManager.HEIGHT);
        String sProfilePic = user.get(sessionManager.PROFILEPIC);
        String sMedicaAid = user.get(sessionManager.MEDICALAID);

        editor.putString("pID",sID);
        editor.putString("pName",sName + " " + sSurname);
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

     /*   btn_Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });*/
 /*       btn_photo_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });*/
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this,AddCondition.class));
            }
        });
        device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, RequestDevice.class));
            }
        });

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),1);
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
            protected Map<String, String> getParams() throws AuthFailureError {
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
        String encodedImage = Base64.encodeToString(imageByteArray,Base64.DEFAULT);

        return  encodedImage;
    }
}
