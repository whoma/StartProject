package com.example.jobs.startproject.activity;

/**
 * Created by jobs on 2016/10/16.
 */

public class ClassKey_Value {
    private String key;
    private String value;

    public ClassKey_Value(String key, String value) {
        this.value = value;
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
