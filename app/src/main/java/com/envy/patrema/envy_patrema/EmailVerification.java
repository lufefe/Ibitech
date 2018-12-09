package com.envy.patrema.envy_patrema;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailVerification extends AppCompatActivity {
    
    TextView txt_email, txt_status, txt_uid;
    Button btn_send, btn_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_verification);
        
        txt_email = findViewById(R.id.txtEmail);
        txt_status = findViewById(R.id.txtStatus);
        txt_uid = findViewById(R.id.txtUID);
        
        btn_refresh = findViewById(R.id.btnRefresh);
        btn_send = findViewById(R.id.btnSend);
        
        setInfo();

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_send.setEnabled(false);

                FirebaseAuth.getInstance().getCurrentUser()
                        .sendEmailVerification()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                btn_send.setEnabled(true);

                                if (task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(), "Verification email sent to : "+FirebaseAuth.getInstance().getCurrentUser(), Toast.LENGTH_LONG).show();
                                }
                                else
                                    Toast.makeText(getApplicationContext(), "Failed to send", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });

        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser()
                        .reload()
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                setInfo();
                            }
                        });
            }
        });
    }

    private void setInfo() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        txt_email.setText(new StringBuilder("EMAIL: ").append(user.getEmail()));
        txt_uid.setText(new StringBuilder("UID: ").append(user.getUid()));
        txt_status.setText(new StringBuilder("STATUS: ").append(String.valueOf(user.isEmailVerified())));
    }
}
