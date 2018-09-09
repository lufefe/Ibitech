package com.divide.ibitech.divide_ibitech.Models;

public class PatientsList {

    String name, surname, dob, cellNo;

    public PatientsList(String name, String surname, String dob, String cellNo) {
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
