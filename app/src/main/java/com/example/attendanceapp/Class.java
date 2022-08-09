package com.example.attendanceapp;

import java.io.Serializable;

public class Class implements Serializable {
    private String name;
    private String begin,end,date;
    private String active;



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

    public Class(String name, String begin, String end, String date,String active) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.date=date;
        this.active=active;
    }
}
