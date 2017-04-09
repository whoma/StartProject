package com.example.jobs.startproject.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Message;

import com.example.jobs.startproject.activity.SomeInfo;

public class MyReceiver extends BroadcastReceiver {
    public MyReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Message message = new Message();
        message.what = SomeInfo.CHANGE;
        SomeInfo.handler.sendMessage(message);
    }
}
