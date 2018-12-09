package com.envy.patrema.envy_patrema.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.envy.patrema.envy_patrema.Models.DocPatientConditionList;
import com.envy.patrema.envy_patrema.R;

import java.util.List;

public class DocPatientConditonAdapter extends ArrayAdapter<DocPatientConditionList> {
    List<DocPatientConditionList> patientConditionLists;
    private Context mCtx;

    public DocPatientConditonAdapter(@NonNull Context context, List<DocPatientConditionList> patientConditionLists) {
        super(context, R.layout.patient_medication_row,patientConditionLists);
        this.patientConditionLists = patientConditionLists;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.patient_medication_row, null,true);
        TextView condition = view.findViewById(R.id.tvMedication);
        TextView date = view.findViewById(R.id.tvMedicationDate);
        TextView doctor = view.findViewById(R.id.tvMedicationDoc);

        DocPatientConditionList conditionList = patientConditionLists.get(position);
        condition.setText(conditionList.getCondition());;
        date.setText(conditionList.getDate());
        doctor.setText(conditionList.getDoctor());

        return view;
    }
}
