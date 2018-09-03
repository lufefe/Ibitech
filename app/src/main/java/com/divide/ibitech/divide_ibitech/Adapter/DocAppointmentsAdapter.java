package com.divide.ibitech.divide_ibitech.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.divide.ibitech.divide_ibitech.Models.ApptsList;
import com.divide.ibitech.divide_ibitech.R;

import java.util.List;

public class DocAppointmentsAdapter extends ArrayAdapter<ApptsList>{

    private List<ApptsList> apptsLists;

    public DocAppointmentsAdapter(@NonNull Context context, List<ApptsList> apptsLists) {
        super(context, R.layout.custom_docappointments_row,apptsLists);
        this.apptsLists = apptsLists;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.custom_docappointments_row, parent, false);

        ApptsList apptsList = apptsLists.get(position);
        TextView name = customView.findViewById(R.id.txtName);
        TextView surname = customView.findViewById(R.id.txtsurname);
        TextView cellNo = customView.findViewById(R.id.txtCellNo);
        ImageView image = customView.findViewById(R.id.imgProfilePic);

        name.setText(apptsList.getName());
        surname.setText(apptsList.getSurname());
        cellNo.setText(apptsList.getCellNo());
        image.setImageResource(R.drawable.profilepic);
        return customView;
    }
}
