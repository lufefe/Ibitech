package com.envy.patrema.envy_patrema;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfile extends Fragment {

    android.support.v7.widget.Toolbar toolbar;
    CardView cvEditProfile;


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
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        toolbar = view.findViewById(R.id.tbProfile);
        toolbar.setTitle("Profile");

        cvEditProfile = view.findViewById(R.id.cvProfile);

        // Inflate the layout for this fragment
        return view;
    }

}
