package com.divide.ibitech.divide_ibitech.Models;

public class AppointmentsList {

    String name, surname, cellNo;

    public AppointmentsList(String name, String surname, String cellNo) {
        this.name = name;
        this.surname = surname;
        this.cellNo = cellNo;
    }

    public String getSurname() {
        return surname;
    }

    public String getName() {
        return name;
    }

    public String getCellNo() {
        return cellNo;
    }
}
