package com.divide.ibitech.divide_ibitech;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SlideOne extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    EditText et_Name,et_Surname,et_DateofBirth,et_Address,et_Suburb,et_City,et_PostalCode;
    String name,surname,dob,gender,address,suburb,city,code;
    Boolean validFName = false, validSurname = false, validDOB = false,selected = false;
    RadioGroup rg_Gender;
    RadioButton rb_Gender;
    Button btn_NextSlide;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_one);


        et_Name = findViewById(R.id.Fname);
        et_Surname = findViewById(R.id.Lname);
        et_DateofBirth = findViewById(R.id.Dob);

        rg_Gender = findViewById(R.id.rgGender);
        et_Address = findViewById(R.id.address1);
        et_Suburb = findViewById(R.id.suburb);
        et_City = findViewById(R.id.City);
        et_PostalCode = findViewById(R.id.postalCode);

        rg_Gender = findViewById(R.id.rgGender);

        btn_NextSlide = findViewById(R.id.btnNextSlide);



        //Real-time validation
        //First name
        et_Name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_Name.getText().length() > 0){
                    validFName = FirstNameValidate();
                }
                else {
                    et_Name.setError(null);
                }
            }
        });

        //Surname
        et_Surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(et_Surname.getText().length() > 0){
                    validSurname = SurnameValidate();
                }
                else {
                    et_Surname.setError(null);
                }
            }
        });

        //DOB - w/ datepicker dialog
        et_DateofBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    datePicker(view);
                }
                else {
                    validDOB = DOBValidate();
                }
            }

        });


        rg_Gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                i = rg_Gender.getCheckedRadioButtonId();
                rb_Gender = findViewById(i);
                selected = true;

                if(rb_Gender.getText() == "Female"){
                    gender = "F";

                }
                else {
                    gender = "M";
                }
            }
        });

        btn_NextSlide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                address = et_Address.getText().toString();
                suburb = et_Suburb.getText().toString();
                city = et_City.getText().toString();
                code = et_PostalCode.getText().toString();
                if((validFName) && (validSurname) &&  (validDOB) && (selected)){
                    savePreferences();
                    startActivity(new Intent(SlideOne.this,SlideTwo.class));
                }
                else {
                    Toast.makeText(SlideOne.this,"Please ensure you've entered all necessary details",Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    //Validate Methods
    public Boolean FirstNameValidate() {
        name = et_Name.getText().toString();
        validFName = true;

        if(name.isEmpty() || name.length() < 2){
            et_Name.setError("Please enter your name");
            validFName = false;
        }
        return validFName;
    }
    private Boolean SurnameValidate() {
        surname = et_Surname.getText().toString();
        validSurname = true;

        if(surname.isEmpty() || surname.length() < 2){
            et_Surname.setError("Please enter your surname");
            validSurname = false;
        }
        return validSurname;
    }
    private Boolean DOBValidate() {
        dob = et_DateofBirth.getText().toString();
        validDOB = true;

        if(dob.isEmpty()){
            et_DateofBirth.setError("Please select your date of birth.");
            validDOB = false;
        }
        return validDOB;
    }

    private void savePreferences() {
        SharedPreferences preferences = getSharedPreferences("slideOnePrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String s_Name = et_Name.getText().toString();
        String s_Surname = et_Surname.getText().toString();
        String s_DOB = et_DateofBirth.getText().toString();
        String s_Gender = gender;
        String s_Address = et_Address.getText().toString();
        String s_Suburb = et_Suburb.getText().toString();
        String s_City = et_City.getText().toString();
        String s_PCode = et_PostalCode.getText().toString();

        editor.putString("pName",s_Name);
        editor.putString("pSurname",s_Surname);
        editor.putString("pDOB",s_DOB);
        editor.putString("pGender",s_Gender);
        editor.putString("pAddress",s_Address);
        editor.putString("pSuburb",s_Suburb);
        editor.putString("pCity",s_City);
        editor.putString("pPCode",s_PCode);
        editor.apply();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar calendar = new GregorianCalendar(year,month,day);
        setDate(calendar);
    }

    public void datePicker(View view){
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getFragmentManager(), "date");
    }

    private void setDate(final Calendar calendar){
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        et_DateofBirth.setText(dateFormat.format(calendar.getTime()));
    }

    public static class DatePickerFragment extends DialogFragment{
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener) getActivity(),year,month,day);
        }
    }

}
