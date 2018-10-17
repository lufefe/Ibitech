package com.divide.ibitech.divide_ibitech.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.divide.ibitech.divide_ibitech.Models.MedicalDevicesList;
import com.divide.ibitech.divide_ibitech.R;

import java.util.List;

public class MedicalDevicesAdapter extends ArrayAdapter<MedicalDevicesList> {
    private List<MedicalDevicesList> medicalDevicesLists;
    private Context mCtx;

    public MedicalDevicesAdapter( List<MedicalDevicesList> medicalDevicesLists, Context mCtx) {
        super(mCtx, R.layout.custom_medical_devices_row, medicalDevicesLists);
        this.medicalDevicesLists = medicalDevicesLists;
        this.mCtx = mCtx;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);

        View view = inflater.inflate(R.layout.custom_medical_devices_row, null,true);
        TextView device_name = view.findViewById(R.id.tvDeviceName);
        TextView doctor = view.findViewById(R.id.tvDoctor);
        TextView status = view.findViewById(R.id.tvStatus);

        MedicalDevicesList medDevicesList = medicalDevicesLists.get(position);
        device_name.setText(medDevicesList.getDevice_name());
        doctor.setText(medDevicesList.getDoctor());
        status.setText(medDevicesList.getStatus());

        return view;
    }
}
