package com.divide.ibitech.divide_ibitech.Models;

import android.widget.ArrayAdapter;

public class MedicalDevicesList {
    String device_name, doctor, status;

    public MedicalDevicesList(String device_name, String doctor, String status) {
        this.device_name = device_name;
        this.doctor = doctor;
        this.status = status;
    }

    public String getDevice_name() {
        return device_name;
    }

    public String getDoctor() {
        return doctor;
    }

    public String getStatus() {
        return status;
    }
}
