package com.envy.patrema.envy_patrema;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.clans.fab.FloatingActionButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientDashboard extends Fragment {

    TextView tv_FullName, tv_Age, tv_BloodType, tv_Address,tv_Gender,tv_MaritalStatus;
    ImageView img_ProfilePic;
    CardView btnManageAllergies, btnManageDevices, btnManageSymptoms, btnManageConditions, btnManageMedicalAid, btnManageNextOfKin;
    FloatingActionButton fab_Symptoms, fab_Allergy;

    SessionManager sessionManager;

    public PatientDashboard() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(getContext());
        //sessionManager.checkLogin();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_patient_dashboard, container, false);

        tv_FullName = view.findViewById(R.id.tvName);
        tv_Age = view.findViewById(R.id.age);
        tv_BloodType = view.findViewById(R.id.bloodType);
        tv_Address = view.findViewById(R.id.tvAddress);
        tv_Gender = view.findViewById(R.id.gender);
        tv_MaritalStatus = view.findViewById(R.id.maritalStatus);
        img_ProfilePic = view.findViewById(R.id.imgProfilePic);

        fab_Symptoms = view.findViewById(R.id.fabSymptoms);
        fab_Allergy = view.findViewById(R.id.fabAllergy);

        return view;
    }



}
