package com.example.jobs.startproject.model;

/**
 * Created by jobs on 2016/10/3.
 */

public class UserData {
    private String acount;
    private String passwd;

    public UserData(String acount, String passwd) {
        this.acount = acount;
        this.passwd = passwd;
    }

    public String getAcount() {
        return acount;
    }

    public void setAcount(String acount) {
        this.acount = acount;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserData)) return false;

        UserData userData = (UserData) o;

        if (!getAcount().equals(userData.getAcount())) return false;
        return getPasswd().equals(userData.getPasswd());

    }

    @Override
    public int hashCode() {
        int result = getAcount().hashCode();
        result = 31 * result + getPasswd().hashCode();
        return result;
    }
}
