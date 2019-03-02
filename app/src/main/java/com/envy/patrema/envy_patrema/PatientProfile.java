package com.envy.patrema.envy_patrema;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.facebook.FacebookSdk.getApplicationContext;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfile extends Fragment {

    SessionManager sessionManager;

    android.support.v7.widget.Toolbar toolbar;
    CardView cvEditProfile, cvSignOut;
    TextView tv_Fullname, tv_IdNumber;
    String fullname, idNumber, profilePic;

    CircleImageView profile_image;



    public PatientProfile() {
        // Required empty public constructor
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        cvEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), PatientEditProfile.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getContext(),
                        R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                startActivity(intent, bundle);

            }
        });

        cvSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logout();
            }
        });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        toolbar = view.findViewById(R.id.tbProfile);
        toolbar.setTitle("Profile");

        cvEditProfile = view.findViewById(R.id.cvProfile);
        cvSignOut = view.findViewById(R.id.cvSignOut);

        tv_Fullname = view.findViewById(R.id.tvName);
        tv_IdNumber = view.findViewById(R.id.tvIDNumber);

        profile_image = view.findViewById(R.id.imgProfilePic);
        SharedPreferences bitmapPref = this.getActivity().getSharedPreferences("bitmapPref", Context.MODE_PRIVATE);
        String encoded = bitmapPref.getString("bitmapString", "0");

        if (!encoded.equals("0")){
            byte[] imageAsBytes = Base64.decode(encoded.getBytes(), Base64.DEFAULT);
            profile_image.setImageBitmap(BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length));
        }


        sessionManager = new SessionManager(getApplicationContext());

        HashMap<String,String> user = sessionManager.getUserDetails();
        fullname = String.format("%s %s", user.get(SessionManager.NAME), user.get(SessionManager.SURNAME));
        idNumber = user.get(SessionManager.ID);

        tv_Fullname.setText(fullname);
        tv_IdNumber.setText(idNumber);

        // Inflate the layout for this fragment
        return view;
    }

}
