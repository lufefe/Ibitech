package com.envy.patrema.envy_patrema;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    private SharedPreferences sharedPreferences;
    public SharedPreferences.Editor editor;
    public Context context;

    //For PatientLogin (Dashboard)
    private static final String PREF_NAME = "LOGIN";
    private static final String LOGIN = "IS_LOGIN";
    static final String ID = "IDNUMBER";
    static final String NAME = "NAME";
    static final String SURNAME = "SURNAME";
    static final String AGE = "AGE";
    static final String BLOODTYPE = "BLOODTYPE";
    static final String GENDER = "GENDER";
    static final String STATUS = "STATUS";
    static final String ADDRESS = "ADDRESS";
    static final String CELLNUMBER = "CELLPHONENUMBER";
    static final String EMAIL = "EMAIL";
    static final String WEIGHT = "WEIGHT";
    static final String HEIGHT = "HEIGHT";
    static final String PROFILEPIC = "PROFILEPIC";
    static final String SUBURBNAME = "SUBURBNAME";
    static final String POSTALCODE = "POSTALCODE";
    static final String CITYNAME = "CITYNAME";
    static final String PROVINCE = "PROVINCE";

    static final String MEDREGNO = "MEDREGNO";
    private static final String OCCUPATION = "OCCUPATION";


    // constructor access and retrieve shared preferences
    public SessionManager(Context context){
        this.context = context;
        int PRIVATE_MODE = 0;
        sharedPreferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    // creates a session for patient
    void createSession(String id, String name, String surname, String age, String bloodtype,
                       String gender, String status, String address, String cell, String email, String weight, String height, String profilePic, String suburbName, String postalCode, String cityName, String province){
        editor.putBoolean(LOGIN,true);
        editor.putString(ID,id);
        editor.putString(NAME,name);
        editor.putString(SURNAME,surname);
        editor.putString(AGE,age);
        editor.putString(BLOODTYPE,bloodtype);
        editor.putString(GENDER,gender);
        editor.putString(STATUS,status);
        editor.putString(ADDRESS,address);

        editor.putString(CELLNUMBER, cell);
        editor.putString(EMAIL, email);
        editor.putString(WEIGHT,weight);
        editor.putString(HEIGHT,height);
        editor.putString(PROFILEPIC,profilePic);

        editor.putString(SUBURBNAME, suburbName);
        editor.putString(POSTALCODE, postalCode);
        editor.putString(CITYNAME, cityName);
        editor.putString(PROVINCE, province);


        editor.apply();

    }

    // creates session for doctor
    void createDocSession(String id, String regNo, String cell, String name, String surname, String email, String occupation,String address, String profilePic, String suburbName, String postalCode, String cityName, String province){
        editor.putBoolean(LOGIN,true);
        editor.putString(ID,id);
        editor.putString(MEDREGNO,regNo);
        editor.putString(NAME,name);
        editor.putString(SURNAME,surname);
        editor.putString(CELLNUMBER, cell);
        editor.putString(EMAIL, email);
        editor.putString(OCCUPATION,occupation);
        editor.putString(ADDRESS,address);

        editor.putString(PROFILEPIC,profilePic);

        editor.putString(SUBURBNAME, suburbName);
        editor.putString(POSTALCODE, postalCode);
        editor.putString(CITYNAME, cityName);
        editor.putString(PROVINCE, province);

        editor.apply();
    }

    private boolean isLoggin(){
        return !sharedPreferences.getBoolean(LOGIN, false);
    }

    void checkLogin(){
        if(this.isLoggin()){
            Intent i = new Intent(context,PatientLogin.class);
            context.startActivity(i);
            //((Dashboard) context).finish();
        }
    }

    void checkDocLogin(){
        if(this.isLoggin()){
            Intent i = new Intent(context, DoctorLogin.class);
            context.startActivity(i);
            ((DocDashboard) context).finish();
        }
    }

    // maps variables with shared preferences
    HashMap<String,String>getUserDetails(){
        HashMap<String,String> user = new HashMap<>();
        user.put(ID,sharedPreferences.getString(ID,null));
        user.put(NAME,sharedPreferences.getString(NAME,null));
        user.put(SURNAME,sharedPreferences.getString(SURNAME,null));
        user.put(AGE,sharedPreferences.getString(AGE,null));
        user.put(BLOODTYPE,sharedPreferences.getString(BLOODTYPE,null));
        user.put(GENDER,sharedPreferences.getString(GENDER,null));
        user.put(STATUS,sharedPreferences.getString(STATUS,null));
        user.put(ADDRESS,sharedPreferences.getString(ADDRESS,null));

        user.put(CELLNUMBER,sharedPreferences.getString(CELLNUMBER,null));
        user.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        user.put(WEIGHT,sharedPreferences.getString(WEIGHT,null));
        user.put(HEIGHT,sharedPreferences.getString(HEIGHT,null));
        user.put(PROFILEPIC,sharedPreferences.getString(PROFILEPIC,null));

        user.put(SUBURBNAME, sharedPreferences.getString(SUBURBNAME, null));
        user.put(POSTALCODE,sharedPreferences.getString(POSTALCODE, null));
        user.put(CITYNAME,sharedPreferences.getString(CITYNAME, null));
        user.put(PROVINCE, sharedPreferences.getString(PROVINCE, null));


        return user;
    }

    HashMap<String,String>getDocDetails(){
        HashMap<String,String> doc = new HashMap<>();
        doc.put(ID,sharedPreferences.getString(ID,null));
        doc.put(MEDREGNO, sharedPreferences.getString(MEDREGNO, null));
        doc.put(NAME,sharedPreferences.getString(NAME,null));
        doc.put(SURNAME,sharedPreferences.getString(SURNAME,null));
        doc.put(EMAIL,sharedPreferences.getString(EMAIL,null));
        doc.put(CELLNUMBER,sharedPreferences.getString(CELLNUMBER,null));
        doc.put(OCCUPATION,sharedPreferences.getString(OCCUPATION,null));
        doc.put(ADDRESS,sharedPreferences.getString(ADDRESS,null));

        doc.put(PROFILEPIC,sharedPreferences.getString(PROFILEPIC,null));
        doc.put(SUBURBNAME, sharedPreferences.getString(SUBURBNAME, null));
        doc.put(POSTALCODE,sharedPreferences.getString(POSTALCODE, null));
        doc.put(CITYNAME,sharedPreferences.getString(CITYNAME, null));
        doc.put(PROVINCE, sharedPreferences.getString(PROVINCE, null));

        return doc;
    }

    public void logout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context,PatientLogin.class);
        context.startActivity(i);
        //((Dashboard) context).finish();
    }
    void doclogout(){
        editor.clear();
        editor.commit();
        Intent i = new Intent(context, DoctorLogin.class);
        context.startActivity(i);
        ((DocDashboard) context).finish();
    }
}
