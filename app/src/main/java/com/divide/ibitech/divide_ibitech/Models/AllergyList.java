package com.divide.ibitech.divide_ibitech.Models;

public class AllergyList {
    String allergy_name,date_added;

    public AllergyList(String allergy_name, String date_added) {
        this.allergy_name = allergy_name;
        this.date_added = date_added;
    }

    public String getAllergy_name() {
        return allergy_name;
    }

    public String getDate_added() {
        return date_added;
    }
}