package com.envy.patrema.envy_patrema.Models;

public class MyAllergiesList {

    String allergy, date_added, tested;

    public String getAllergy() {
        return allergy;
    }

    public String getDate_added() {
        return date_added;
    }

    public String getTested() {
        return tested;
    }

    public MyAllergiesList(String allergy, String date_added, String tested) {
        this.allergy = allergy;
        this.date_added = date_added;
        this.tested = tested;
    }
}