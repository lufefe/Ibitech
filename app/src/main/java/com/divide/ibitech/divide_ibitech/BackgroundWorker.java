package com.divide.ibitech.divide_ibitech;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class BackgroundWorker extends AsyncTask<String, Void, String> {

    Context context;
    AlertDialog alertDialog;

    BackgroundWorker(View.OnClickListener ctx){
        context = (Context) ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];
        String login_url = "http://sict-iis.nmmu.ac.za/ibitech/app/login.php";
        String register_url = "http://sict-iis.nmmu.ac.za/ibitech/app/register.php";

        if(type.equals("login")){
            try {
                String userID = params[1];
                String userCell = params[2];
                String userPassword = params[3];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("userID","UTF-8")+"="+URLEncoder.encode(userID,"UTF-8")+"&"
                        +URLEncoder.encode("userCellphone","UTF-8")+"="+URLEncoder.encode(userCell,"UTF-8")+"&"
                        +URLEncoder.encode("userPassword","UTF-8")+"="+URLEncoder.encode(userPassword,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(type.equals("register")){
            try {
                String userID = params[1];
                String userCell = params[2];
                String userEmail = params[3];
                String userPassword = params[4];
                String firstName = params [5];
                String surname = params [6];
                String dob = params [7];
                String gender = params [8];
                String address = params [9];
                String suburb = params [10];
                String city = params [11];
                String postal = params [12];
                String weight = params [13];
                String height = params [14];

                URL url = new URL(register_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                String post_data = URLEncoder.encode("userID","UTF-8")+"="+URLEncoder.encode(userID,"UTF-8")+"&"
                        +URLEncoder.encode("userCellphone","UTF-8")+"="+URLEncoder.encode(userCell,"UTF-8")+"&"
                        +URLEncoder.encode("userEmail","UTF-8")+"="+URLEncoder.encode(userEmail,"UTF-8")+"&"
                        +URLEncoder.encode("userPassword","UTF-8")+"="+URLEncoder.encode(userPassword,"UTF-8")
                        +URLEncoder.encode("userFirstName","UTF-8")+"="+URLEncoder.encode(firstName,"UTF-8")+"&"
                        +URLEncoder.encode("userSurname","UTF-8")+"="+URLEncoder.encode(surname,"UTF-8")+"&"
                        +URLEncoder.encode("userDateOfBirth","UTF-8")+"="+URLEncoder.encode(dob,"UTF-8")+"&"
                        +URLEncoder.encode("userGender","UTF-8")+"="+URLEncoder.encode(gender,"UTF-8")+"&"
                        +URLEncoder.encode("userAddress","UTF-8")+"="+URLEncoder.encode(address,"UTF-8")+"&"
                        +URLEncoder.encode("userSuburb","UTF-8")+"="+URLEncoder.encode(suburb,"UTF-8")+"&"
                        +URLEncoder.encode("userCity","UTF-8")+"="+URLEncoder.encode(city,"UTF-8")+"&"
                        +URLEncoder.encode("userPostalCode","UTF-8")+"="+URLEncoder.encode(postal,"UTF-8")+"&"
//                        +URLEncoder.encode("userStatus","UTF-8")+"="+URLEncoder.encode(userEmail,"UTF-8")+"&"
//                        +URLEncoder.encode("userBloodType","UTF-8")+"="+URLEncoder.encode(userEmail,"UTF-8")+"&"
                        +URLEncoder.encode("userWeight","UTF-8")+"="+URLEncoder.encode(weight,"UTF-8")+"&"
                        +URLEncoder.encode("userHeight","UTF-8")+"="+URLEncoder.encode(height,"UTF-8");
//                        +URLEncoder.encode("userProfilePic","UTF-8")+"="+URLEncoder.encode(userEmail,"UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                String result = "";
                String line;

                while ((line = bufferedReader.readLine()) != null){
                    result += line;
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return  result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("Login Status");
    }

    @Override
    protected void onPostExecute(String result) {
        alertDialog.setMessage(result);
        alertDialog.show();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
