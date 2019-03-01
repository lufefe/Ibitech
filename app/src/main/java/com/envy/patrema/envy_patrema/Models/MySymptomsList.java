package com.envy.patrema.envy_patrema.Models;
/**
 * Created by s216100801
 */
public class MySymptomsList {

    private String symptom ,date_added, severity;

    public MySymptomsList(String symptom, String date_added, String severity) {
        this.symptom = symptom;
        this.date_added = date_added;
        this.severity = severity;
    }


    public String getSymptom() {
        return symptom;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getSeverity() {
        return severity;
    }
}