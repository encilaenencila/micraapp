package com.example.micraapp;

public class Userdata {

    int reporteruserid;
    String firstname;
    String lastname;

    public int getReporteruserid() {
        return reporteruserid;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public Userdata(int reporteruserid, String firstname, String lastname) {
        this.reporteruserid = reporteruserid;
        this.firstname = firstname;
        this.lastname = lastname;
    }
}
