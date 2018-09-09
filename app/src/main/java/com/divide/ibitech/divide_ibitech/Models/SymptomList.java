package com.divide.ibitech.divide_ibitech.Models;
/**
 * Created by s216100801
 */
public class SymptomList {
    String symptom_name ,date_added;

    public SymptomList(String symptom_name, String date_added) {
        this.symptom_name = symptom_name;
        this.date_added = date_added;
    }

    public String getSymptom_name() {
        return symptom_name;
    }

    public String getDate_added() {
        return date_added;
    }
}