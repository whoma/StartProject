package com.example.jobs.startproject.model;

/**
 * Created by jobs on 2016/10/18.
 */

public class UpdateInfo {
    private String version;
    private String information;
    private String pcdownloadlink;
    private String androiddownloadlink;
    private boolean isForce;

    public String getAndroiddownloadlink() {
        return androiddownloadlink;
    }

    public void setAndroiddownloadlink(String androiddownloadlink) {
        this.androiddownloadlink = androiddownloadlink;
    }

    public String getPcdownloadlink() {
        return pcdownloadlink;
    }

    public void setPcdownloadlink(String pcdownloadlink) {
        this.pcdownloadlink = pcdownloadlink;
    }

    public String getInformation() {
        return information;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isForce() {
        return isForce;
    }

    public void setForce(boolean force) {
        isForce = force;
    }
}
