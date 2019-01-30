package com.envy.patrema.envy_patrema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.ganfra.materialspinner.MaterialSpinner;

public class PatientEditProfile extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    MaterialSpinner sp_MaritalStatus, sp_Province, sp_BloodType;
    RadioGroup rgGender;
    String gender = "", maritalStatus = "", prov = "";
    android.support.v7.widget.Toolbar toolbar;
    ImageView imgEditImage;
    Bitmap bitmap;
    CircleImageView profile_image;

    String idNumber, fullName, firstName, surname, dob, cell, email, address,suburb, city, province, postalCode, bloodType, weight, height;
    TextInputEditText etName, etID, etEmail, etCell, etHeight, etWeight, etAddress,etSuburb, etCity, etPostalCode;

    Boolean firstStart;

    String URL_UPLOAD = "http://10.0.2.2/app/upload_profile_image.php";
    String URL_UPDATE = "http://10.0.2.2/app/updatepatientprofile.php";
    String URL_FIRSTUPDATE = "http://10.0.2.2/app/firstupdateprofile.php";


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();

        if (i == android.R.id.home) {
            if (firstStart){
                startActivity(new Intent(getApplicationContext(), PatientMainActivity.class));
                finish();
                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putBoolean("firstStart", false);
                editor.apply();
                return true;
            }
            else {
                this.finish();
                return true;
            }
        }
        else if (i == R.id.btnSaveProfile) {

            idNumber = etID.getText().toString();
            fullName = etName.getText().toString();
            firstName = getFirstName(fullName);
            surname = getSurname(fullName);

            cell = etCell.getText().toString();

            weight = etWeight.getText().toString();
            height = etHeight.getText().toString();

            address = etAddress.getText().toString();
            suburb = etSuburb.getText().toString();
            city = etCity.getText().toString();
            province = getProvince(prov);
            postalCode = etPostalCode.getText().toString();


            if (firstStart){

                if (idNumber.equals("")) {
                    etID.setError("You cannot leave this field empty");
                }
                else{
                    dob = getDateOfBirth(idNumber);
                    UploadPicture(idNumber, getStringImage(bitmap));
                    firstProfileUpdate(idNumber, email, firstName, surname, cell, dob, gender, maritalStatus, bloodType, weight, height, address, suburb, city, province, postalCode);

                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putBoolean("firstStart", false);
                    editor.apply();
                    return true;
                }

            }
            else {

                if (idNumber.equals("")) {
                    etID.setError("You cannot leave this field empty");
                }
                else {
                    dob = getDateOfBirth(idNumber);
                    updateProfile(idNumber,email, firstName, surname, cell, dob,gender, maritalStatus, bloodType, weight, height, address,suburb, city,province, postalCode);
                    return true;
                }

            }

        }

        return super.onOptionsItemSelected(item);
    }

    private String getProvince(String prov) {
        String tempProv = "";

        switch (prov){
            case "EC":
                tempProv = "Eastern Cape";
                break;
            case "WC":
                tempProv = "Western Cape";
                break;
            case "LP":
                tempProv = "Limpopo";
                break;
            case "NC":
                tempProv = "Northern Cape";
                break;
            case "NW":
                tempProv = "North West";
                break;
            case "KZN":
                tempProv = "KwaZulu-Natal";
                break;
            case "MP":
                tempProv = "Mpumalanga";
                break;
            case "GP":
                tempProv = "Gauteng";
                break;
            case "FS":
                tempProv = "Free State";
                break;

        }
        return tempProv;
    }

    private String getSurname(String fullName) {
        String[] temp = fullName.split("\\s+");

        return temp[temp.length-1];

    }

    private String getFirstName(String fullName) {

        String[] temp = fullName.split("\\s+");

        return temp[0];

    }

    private String getDateOfBirth(String idNumber) {
        String year = idNumber.substring(0,2);
        String month = idNumber.substring(2,4);
        String day = idNumber.substring(4,6);

        String tempYear = "19"+year;

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);

        if (currentYear - Integer.parseInt(tempYear) >= 18 && currentYear - Integer.parseInt(tempYear) < 100){
            return day + "-" + month + "-" + tempYear;
        }
        else {
            tempYear = "20"+year;

            if (currentYear - Integer.parseInt(tempYear) >= 18 && currentYear - Integer.parseInt(tempYear) < 100)
                return day + "-" + month + "-" + tempYear;
        }

        return null;
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save_button, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit_profile);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("firstStart", true);

        SharedPreferences preferences = getSharedPreferences("patient", MODE_PRIVATE);
        email = preferences.getString("email", "");


        toolbar = findViewById(R.id.tbEditProfile);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgEditImage = findViewById(R.id.imgEditImage);
        profile_image = findViewById(R.id.imgProfilePic);

        etName = findViewById(R.id.etPatientName);
        etID = findViewById(R.id.etIDNumber);
        etEmail = findViewById(R.id.etEmailAddress);
        etEmail.setText(email);
        etCell = findViewById(R.id.etCellphone);

        etHeight = findViewById(R.id.etHeight);
        etWeight = findViewById(R.id.etWeight);
        etAddress = findViewById(R.id.etAddress);
        etSuburb = findViewById(R.id.etSuburb);
        etCity = findViewById(R.id.etCity);
        etPostalCode = findViewById(R.id.etCode);



        rgGender = findViewById(R.id.rg_Gender);
        rgGender.setOnCheckedChangeListener(this);

        imgEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        sp_Province = findViewById(R.id.provinceSpinner);
        String[] PROVINCES = {"EC", "WC", "MP", "NW", "NC","KZN","GP","LP","FS"};
        ArrayAdapter<String> provAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, PROVINCES);
        provAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_Province.setAdapter(provAdapter);

        sp_Province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                prov = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_MaritalStatus = findViewById(R.id.statusSpinner);
        String[] STATUS = {"Single", "Married", "Divorced", "Widowed", "Separated"};
        ArrayAdapter<String> statusAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, STATUS);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_MaritalStatus.setAdapter(statusAdapter);

        sp_MaritalStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maritalStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_BloodType = findViewById(R.id.bloodSpinner);
        String[] BLOOD = {"A+", "A-", "B+", "B-", "O+", "O-", "AB+", "AB-"};
        ArrayAdapter<String> bloodAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, BLOOD);
        bloodAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_BloodType.setAdapter(bloodAdapter);

        sp_BloodType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bloodType = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {

        switch (checkedId){
            case R.id.rbFemale:
                gender = "Female";
                break;

            case R.id.rbMale:
                gender = "Male";
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null){
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void UploadPicture(final String id, final String photo) {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading ...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPLOAD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success = jsonObject.getString("success");

                    if (success.equals("1")){
                        Toast.makeText(getApplicationContext(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Try Again" + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Try Again - Error Response", Toast.LENGTH_LONG).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();

                params.put("id", id);
                params.put("photo", photo);

                return params;
            }

        };

        Singleton.getInstance(PatientEditProfile.this).addToRequestQue(stringRequest);

    }

    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

    }
    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }


    public void updateProfile(final String idNumber,final String email, final String name, final String surname, final String cell,final String dob, final String gender, final String maritalStatus, final String bloodType,final String weight,final String height,final String address,final String suburb, final String city, final String province, final String postalCode){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_UPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(PatientEditProfile.this,"Profile updated successfully.<Update>",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(PatientEditProfile.this,"Profile updated not successfully.<Update>",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PatientEditProfile.this,"There was a problem updating your profile, try again later.<Update>" + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PatientEditProfile.this,"There was an internal error, please try again later. <Update>" + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", idNumber);
                params.put("name", name);
                params.put("surname", surname);
                params.put("cell", cell);
                params.put("email", email);
                params.put("status",maritalStatus);
                params.put("dob",dob);
                params.put("sex",gender);
                params.put("blood_type",bloodType);
                params.put("weight",weight);
                params.put("height",height);
                params.put("address",address);
                params.put("suburb", suburb);
                params.put("city",city);
                params.put("province",province);
                params.put("postalCode",postalCode);

                return params;
            }
        };
        Singleton.getInstance(PatientEditProfile.this).addToRequestQue(stringRequest);
    }

    public void firstProfileUpdate(final String idNumber,final String email, final String name, final String surname, final String cell,final String dob, final String gender, final String maritalStatus, final String bloodType,final String weight,final String height,final String address,final String suburb, final String city, final String province, final String postalCode){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_FIRSTUPDATE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1")){
                                Toast.makeText(PatientEditProfile.this,"Profile updated successfully.<FirstUpdate>",Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(PatientEditProfile.this,"There was a problem updating your profile, try again later.<FirstUpdate>" + e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(PatientEditProfile.this,"There was an internal error, please try again later. <FirstUpdate>" + error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("id", idNumber);
                params.put("name", name);
                params.put("surname", surname);
                params.put("cell", cell);
                params.put("email", email);
                params.put("status",maritalStatus);
                params.put("dob",dob);
                params.put("sex",gender);
                params.put("blood_type",bloodType);
                params.put("weight",weight);
                params.put("height",height);
                params.put("address",address);
                params.put("suburb", suburb);
                params.put("city",city);
                params.put("province",province);
                params.put("postalCode",postalCode);

                return params;
            }
        };
        Singleton.getInstance(PatientEditProfile.this).addToRequestQue(stringRequest);
    }

}
