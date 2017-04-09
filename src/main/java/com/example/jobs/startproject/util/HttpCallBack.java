package com.example.jobs.startproject.util;

/**
 * Created by jobs on 2016/9/27.
 */

public interface HttpCallBack {
    public void onFinish(final String response);
    public void onError(final Exception error);
}
