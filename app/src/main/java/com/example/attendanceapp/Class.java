package com.example.attendanceapp;

import java.io.Serializable;

public class Class implements Serializable {
    private String name;
    private String begin,end,date;

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

    public Class(String name, String begin, String end, String date) {
        this.name = name;
        this.begin = begin;
        this.end = end;
        this.date=date;
    }
}
