package com.envy.patrema.envy_patrema.Models;

public class DocPatientConditionList {
    String condition, date, doctor;

    public DocPatientConditionList(String condition, String date, String doctor) {
        this.condition = condition;
        this.date = date;
        this.doctor = doctor;
    }

    public String getCondition() {
        return condition;
    }

    public String getDate() {
        return date;
    }

    public String getDoctor() {
        return doctor;
    }
}
