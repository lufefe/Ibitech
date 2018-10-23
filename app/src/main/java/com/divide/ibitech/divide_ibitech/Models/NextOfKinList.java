package com.divide.ibitech.divide_ibitech.Models;

public class NextOfKinList {
    String name, surname, cellNo, relation;

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getCellNo() {
        return cellNo;
    }

    public String getRelation() {
        return relation;
    }

    public NextOfKinList(String name, String surname, String cellNo, String relation) {

        this.name = name;
        this.surname = surname;
        this.cellNo = cellNo;
        this.relation = relation;
    }
}
