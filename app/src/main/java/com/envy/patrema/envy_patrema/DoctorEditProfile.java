package com.envy.patrema.envy_patrema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.ImageView;
import android.widget.Toast;

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

import de.hdodenhof.circleimageview.CircleImageView;

public class DoctorEditProfile extends AppCompatActivity{

    SessionManager sessionManager;

    android.support.v7.widget.Toolbar toolbar;
    ImageView imgEditImage;
    Bitmap bitmap;
    CircleImageView profile_image;

    TextInputEditText etName, etRegistrationNo, etOccupation;
    String docName, regNo = "", occupation, email;

    Boolean firstStart;

    String URL_UPLOAD = "http://10.0.2.2/app/upload_doc_profile_image.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_edit_profile);

        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> doc = sessionManager.getDocDetails();
        docName = String.format("%s %s", doc.get(SessionManager.NAME), doc.get(SessionManager.SURNAME));
        regNo = doc.get(SessionManager.MEDREGNO);
        occupation = doc.get(SessionManager.OCCUPATION);
        email = doc.get(SessionManager.EMAIL);

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        firstStart = prefs.getBoolean("docFirstStart", true);

        toolbar = findViewById(R.id.tbEditProfile);
        toolbar.setTitle("Edit Profile");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        imgEditImage = findViewById(R.id.imgEditImage);
        profile_image = findViewById(R.id.imgProfilePic);

        SharedPreferences bitmapPref = getSharedPreferences("bitmapPref", MODE_PRIVATE);
        String encoded = bitmapPref.getString("bitmapString", "0");

        if (!encoded.equals("0")){
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }

        etName = findViewById(R.id.etName);
        etRegistrationNo = findViewById(R.id.etRegistrationNo);
        etOccupation = findViewById(R.id.etOccupation);

        imgEditImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseFile();
            }
        });

        if (docName.contains("null"))
            etName.setText("Not Set");
        else
            etName.setText(docName);

        etRegistrationNo.setText(regNo);

        if (occupation.contains("null"))
            etOccupation.setText("Not Set");
        else
            etOccupation.setText(occupation);


    }



    private void chooseFile(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int i = item.getItemId();

        if (i == android.R.id.home) {
            if (firstStart){
                startActivity(new Intent(getApplicationContext(), DoctorDashboard.class));
                finish();

                return true;
            }
            else {
                this.finish();
                return true;
            }
        }
        else if (i == R.id.btnSaveProfile) {
            UploadPicture(email, regNo, getStringImage(bitmap));
        }

        return super.onOptionsItemSelected(item);
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

    public String getStringImage(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] imageByteArray = byteArrayOutputStream.toByteArray();

        SharedPreferences bitPrefs = getSharedPreferences("bitmapPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = bitPrefs.edit();
        editor.putString("bitmapString",Base64.encodeToString(imageByteArray, Base64.DEFAULT) );
        editor.apply();


        return Base64.encodeToString(imageByteArray, Base64.DEFAULT);
    }

    private void UploadPicture(final String email, final String regNo, final String photo) {

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

                        // TODO -> update sessionManager for PROFILEPIC

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
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("email", email);
                params.put("med_reg_no", regNo);
                params.put("photo", photo);

                return params;
            }

        };

        Singleton.getInstance(DoctorEditProfile.this).addToRequestQue(stringRequest);

    }


}
