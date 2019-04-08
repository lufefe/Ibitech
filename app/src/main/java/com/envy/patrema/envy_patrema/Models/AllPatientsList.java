package com.envy.patrema.envy_patrema.Models;

public class AllPatientsList {

    private String name, surname, dob, cellNo;

    public AllPatientsList(String name, String surname, String dob, String cellNo) {
        this.name = name;
        this.surname = surname;
        this.dob = dob;
        this.cellNo = cellNo;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getDob() {
        return dob;
    }

    public String getCellNo() {
        return cellNo;
    }
}
