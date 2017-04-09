package com.example.jobs.startproject.context;

import android.app.Application;
import android.content.Context;

/**
 * Created by jobs on 2016/9/27.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
