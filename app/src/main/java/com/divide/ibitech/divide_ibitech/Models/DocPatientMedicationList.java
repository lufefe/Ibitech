package com.divide.ibitech.divide_ibitech.Models;

public class DocPatientMedicationList {
    String medicine, dateDiagnosed, doctor;

    public DocPatientMedicationList(String medicine, String dateDiagnosed, String doctor) {
        this.medicine = medicine;
        this.dateDiagnosed = dateDiagnosed;
        this.doctor = doctor;
    }

    public String getMedicine() {
        return medicine;
    }

    public String getDateDiagnosed() {
        return dateDiagnosed;
    }

    public String getDoctor() {
        return doctor;
    }
}
