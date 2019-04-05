package com.envy.patrema.envy_patrema.Models;

public class CreateVisitList {

    private String name, surname, idNo;

    public CreateVisitList(String name, String surname, String idNo) {
        this.name = name;
        this.surname = surname;
        this.idNo = idNo;
    }

    public String getSurname() {
        return surname;
    }

    public String getIdNo() {
        return idNo;
    }

    public String getName() {
        return name;
    }

}
