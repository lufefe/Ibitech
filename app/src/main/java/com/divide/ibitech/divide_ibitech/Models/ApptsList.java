package com.divide.ibitech.divide_ibitech.Models;

public class ApptsList {

    String name, surname, cellNo;

    public ApptsList(String name,String surname, String cellNo) {
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
