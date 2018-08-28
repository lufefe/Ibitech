package com.divide.ibitech.divide_ibitech;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Register extends AppCompatActivity {

    EditText username, password, confirmPassword, email, contactNumber,Name,Surname,Birth,gender,status,MemberN,;
    Button buttonRegister;
	
	private static  final string REGISTER_URL="serverHostLink.Register.php";
	

	{
		
		
	}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        username = findViewById(R.id.ID_Num);
        password = findViewById(R.id.Password);
        confirmPassword= findViewById(R.id.confirm_password);
        email= findViewById(R.id.Email);
        contactNumber = findViewById(R.id.Contact_Number);
		Name = findViewById(R.id.Firstname);
		Surname = findViewById(R.id.Lastname);
		Birth=findViewById(R.id.DOB);
		gender=findViewById(R.id.Gender);
		status=findViewById(R.id.Mstatus);
		email=findViewById(R.id.Email);
		MemberN=findViewById(R.id.MNumber);
        buttonRegister= findViewById(R.id.button_Register);

    }

	new_userRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerInttent = new Intent(Login.this,Register.class);
                Login.this.startActivity(registerInttent);
            }
        });
	
	

private void registerUser()

{
	string username=IDNum.getText().toString();
	String password=password.getText().toString();
	int contactNumber=contactNumber.getText().toString();
	string fName =fName.getText().toString();
	string lName= lName.getText().toString();
	DateTime Dob=Dob.getText().toString();
	string gender =gender.getText().toString();
	string status = status.getText().toString();
	string email= email.getText().toString();
	string 	memberNum=memberNum.getText().toString();
	register(username,password,contactNumber,fName,lName,Dob,gender,status,email,memberNum);
	
	
}
private void register(string username,string password,int contactNumber,string fName,string lName,DateTime Dob,string gender,string status,string email,string register)
{
	string urlsuffix="?username"+username+"&password"+password+"&contactNumber"+contactNumber+"&fName"+fName+"&lName"+lName+"&Dob"+"&gender"+gender+"&status"+status+"&email"+email+"&register"+register;
	
	
	class registerUser extends AsyncTask<String , void,String>{
		ProgressDialog loading;
		
		@Override
		provide void onPreExecute()
		{
			super.onPreExecute();
			loading=ProgressDialog.show(Register.this,"Please wait",null,true);
			
			
		}
			@Override protected void onPostExecute()
			
			{
				super.onPreExecute();
			Toast=makeText(getApplicationcontext(),"Interent Not found please check ",Toast.LENGTH_SHORT.show();
			}
		@Override
		protected String doInbacjgroud(String...params)
		{
			String s = params[0];
			BufferedReader bufferReader = null;
			 try	{
				 URL url = new 	URL(REGISTER_URL+s);
				 HttpURLConnection con = (HttpURLConnection)url.openConnection();
				 bufferReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
				 String result ;
				 result=bufferReader.readline();
				 return result;
				 
				 
			 }cath (Exception e)
			 {
			return null;
			 }
		}
	}
	RegisterUser ur new RegisterUser();
	ur.execute(urlsuffix);
	
}
}
