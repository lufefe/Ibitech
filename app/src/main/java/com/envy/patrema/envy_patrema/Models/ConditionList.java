package com.envy.patrema.envy_patrema.Models;

public class ConditionList {
    private String condition_name,date_added, doctorName, doctorSurname;

    public ConditionList(String condition_name, String date_added, String doctorName, String doctorSurname) {
        this.condition_name = condition_name;
        this.date_added = date_added;
        this.doctorName = doctorName;
        this.doctorSurname = doctorSurname;
    }

    public String getCondition_name() {
        return condition_name;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public String getDoctorSurname() {
        return doctorSurname;
    }
}