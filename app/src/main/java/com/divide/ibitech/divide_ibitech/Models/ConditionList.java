package com.divide.ibitech.divide_ibitech.Models;

public class ConditionList {
    String condition_name,date_added;

    public ConditionList(String condition_name, String date_added) {
        this.condition_name = condition_name;
        this.date_added = date_added;
    }

    public String getCondition_name() {
        return condition_name;
    }

    public String getDate_added() {
        return date_added;
    }
}