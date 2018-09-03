package com.divide.ibitech.divide_ibitech;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ManageSymptomsCustomAdapter extends ArrayAdapter<String>{

    ManageSymptomsCustomAdapter(@NonNull Context context, String[] symptoms) {
        super(context, R.layout.custom_manage_symptoms_row,symptoms);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_manage_symptoms_row, parent, false);

        String singleSymptom = getItem(position);
        TextView txtSymptomName = customView.findViewById(R.id.txtSymptomName);
        TextView txtDateAdded = customView.findViewById(R.id.txtDateAdded);
        TextView txtDocName = customView.findViewById(R.id.txtDocName);
        TextView txtDateVisited = customView.findViewById(R.id.txtDateVisited);
        TextView txtDocCellNo = customView.findViewById(R.id.txtDocCellNo);

        txtSymptomName.setText(singleSymptom);
        return customView;
    }
}
