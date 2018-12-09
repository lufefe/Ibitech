package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.DocPatientMedicationList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class DocPatientMedicationAdapter extends ArrayAdapter<DocPatientMedicationList> {
    List<DocPatientMedicationList> patientMedsList;
    private Context mCtx;

    public DocPatientMedicationAdapter(List<DocPatientMedicationList> patientMedsList, Context mCtx) {
        super(mCtx, R.layout.patient_medication_row,patientMedsList);
        this.patientMedsList = patientMedsList;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.patient_medication_row, null,true);
        TextView medicine = view.findViewById(R.id.tvMedication);
        TextView dateDiagnosed = view.findViewById(R.id.tvMedicationDate);
        TextView doctor = view.findViewById(R.id.tvMedicationDoc);

        DocPatientMedicationList medicationList = patientMedsList.get(position);
        medicine.setText(medicationList.getMedicine());
        dateDiagnosed.setText(medicationList.getDateDiagnosed());
        doctor.setText(medicationList.getDoctor());

        return view;
    }
}
