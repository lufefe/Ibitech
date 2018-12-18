package com.envy.patrema.envy_patrema;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class PatientProfile extends Fragment {

    android.support.v7.widget.Toolbar toolbar;


    public PatientProfile() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_patient_profile, container, false);

        toolbar = view.findViewById(R.id.tbProfile);
        toolbar.setTitle("Profile");
        toolbar.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

        // Inflate the layout for this fragment
        return view;
    }

}
