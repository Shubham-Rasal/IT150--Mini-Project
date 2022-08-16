package com.example.attendanceapp;

import java.io.Serializable;
import java.util.ArrayList;

public class Class implements Serializable {
    private String name;
    private String begin,end,date;
    private String active;
    private String presentStudent;

    public String getPresentStudent() {
        return presentStudent;
    }

    public Class(String presentStudent) {
        this.presentStudent = presentStudent;
    }

    public String getActive() {
        return String.valueOf(active);
    }

    public String getName() {
        return name;
    }

    public String getBegin() {
        return begin;
    }

    public String getEnd() {
        return end;
    }

    public String getDate() {
        return date;
    }

    public void setActive(String active) {

        this.active = active;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private Class(){}

    public Class(String name, String begin, String end, String date,String active,String presentStudent) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.date=date;
        this.active=active;
        this.presentStudent=presentStudent;


    }
}
