package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.PatientsList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;


public class DocPatientsAdapter extends ArrayAdapter<PatientsList> {

    private List<PatientsList> patientsLists;

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_docpatients_row, parent,false);

        PatientsList patientsList = patientsLists.get(position);
        TextView name = customView.findViewById(R.id.tvFullname);
        TextView cell = customView.findViewById(R.id.tvCellNo);
        TextView dob = customView.findViewById(R.id.tvDOB);
        ImageView imageView = customView.findViewById(R.id.imgProfilePic);

        name.setText(String.format("%s %s", patientsList.getName(), patientsList.getSurname()));
        cell.setText(patientsList.getCellNo());
        dob.setText(patientsList.getDob());
        imageView.setImageResource(R.drawable.profilepic);
        return customView;
    }

    public DocPatientsAdapter(@NonNull Context context, List<PatientsList> patientsLists) {
        super(context, R.layout.custom_docpatients_row, patientsLists);
        this.patientsLists = patientsLists;

    }
}
