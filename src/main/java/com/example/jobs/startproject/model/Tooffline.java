package com.example.jobs.startproject.model;

/**
 * Created by jobs on 2016/10/12.
 */

public class Tooffline {
    private String date;
    private String note;
    private String outmessage;
    private String serverDate;

    public Tooffline() {
    }


    public Tooffline(String date, String serverDate, String outmessage, String note) {
        this.date = date;
        this.serverDate = serverDate;
        this.outmessage = outmessage;
        this.note = note;
    }

    public String getNote() {

        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getServerDate() {
        return serverDate;
    }

    public void setServerDate(String serverDate) {
        this.serverDate = serverDate;
    }

    public String getOutmessage() {
        return outmessage;
    }

    public void setOutmessage(String outmessage) {
        this.outmessage = outmessage;
    }


}
