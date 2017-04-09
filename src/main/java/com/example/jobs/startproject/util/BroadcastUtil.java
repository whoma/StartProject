package com.example.jobs.startproject.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

import com.example.jobs.startproject.context.MyApplication;
import com.example.jobs.startproject.receiver.MyReceiver;

/**
 * Created by jobs on 2016/10/4.
 */

public class BroadcastUtil {
    private static MyReceiver myReceiver;
    private static LocalBroadcastManager manager;
    private static IntentFilter filter;

    static {
        myReceiver = new MyReceiver();
        manager = LocalBroadcastManager.getInstance(MyApplication.getContext());
        filter = new IntentFilter();
    }

    public static void register() {
        filter.addAction("com.example.change");
        manager.registerReceiver(myReceiver, filter);
    }

    public static void sendLocalBroadcast()  {
        Intent intent = new Intent("com.example.change");
        manager.sendBroadcast(intent);
    }
}
