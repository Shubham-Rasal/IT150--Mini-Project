package com.example.attendanceapp;

public class Student {
    private String name,email,password,numberOfClasses;

    public void setNumberOfClasses(String numberOfClasses) {
        this.numberOfClasses = numberOfClasses;
    }

    public String getNumberOfClasses() {
        return numberOfClasses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Student(String name, String email, String password,String numberOfClasses) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.numberOfClasses=numberOfClasses;
    }
}
