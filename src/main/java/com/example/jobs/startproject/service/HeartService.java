package com.example.jobs.startproject.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.activity.LoginActivity;
import com.example.jobs.startproject.activity.SomeInfo;
import com.example.jobs.startproject.context.MyApplication;
import com.example.jobs.startproject.model.DataForPacket;
import com.example.jobs.startproject.util.BroadcastUtil;
import com.example.jobs.startproject.util.HttpCallBack;
import com.example.jobs.startproject.util.Socket2Server;

/**
 * Created by jobs on 2016/9/29.
 */

public class HeartService extends Service {
    public static boolean isService = true;
    public boolean isContinue = true;
    private static String useTime;
    private static String useFlux;
    private static int startUseFlux;

    static {
        startUseFlux = LoginActivity.getStartFlux();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Notification.Builder builder = new Notification.Builder(MyApplication.getContext());
        builder.setSmallIcon(R.drawable.wifi);
        builder.setTicker("WIFI连接中");
        builder.setContentTitle("WIFI已连接");
        builder.setContentText("已经连接上，正在持续连接中。");
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(this, SomeInfo.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, intent, 0);
        builder.setContentIntent(pi);
        startForeground(1, builder.build());
    }

    @Override
    public int onStartCommand(Intent intent, final int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isService) {
                    /**
                     * Misc
                     */
                    Socket2Server.Date2Socket(DataForPacket.Date2Alive(), Socket2Server.SIZE_ALIVE, new HttpCallBack() {
                        @Override
                        public void onFinish(String response) {
                            useFlux = response.substring(16, 25);
                            useTime = response.substring(64, 72);
                            // 广播
                            BroadcastUtil.sendLocalBroadcast();
                        }

                        @Override
                        public void onError(Exception error) {
                        }
                    });

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    /**
                     * send1
                     */
                    do {
                        Socket2Server.Date2Socket(DataForPacket.HeartBeatPacket(DataForPacket.getCount(), 1, false), Socket2Server.SIZE_MISC, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {

                                String sCount = String.format("%2s", Integer.toHexString(DataForPacket.count)).replace(" ", "0");
                                if (response.startsWith("07002800") || response.startsWith("07" + sCount + "2800")) {
                                    isContinue = false;
                                } else if (response.substring(0, 2).equals("07") && response.substring(4, 6).equals("10")) {
                                    DataForPacket.setTail("00000000");
                                    isContinue = true;
                                }
                            }

                            @Override
                            public void onError(Exception error) {
                                //DO
                            }

                        });

                    } while (isContinue);

                        /**
                         * send3
                         */
                        Socket2Server.Date2Socket(DataForPacket.HeartBeatPacket(DataForPacket.getCount(), 3, false), Socket2Server.SIZE_MISC, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                DataForPacket.setTail(response.substring(32, 40));
                            }

                            @Override
                            public void onError(Exception error) {
                            }
                        });

                        try {
                            Thread.sleep(17000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                }
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    public static String getUseTime() {
        useTime += " ";
        String realTime = useTime.substring(6, 8) + useTime.substring(4, 6) + useTime.substring(2, 4) + useTime.substring(0, 2);
        return Integer.parseInt(realTime, 16) / 60 + "";
    }

    public static String getUseFlux() {
        useFlux += " ";
        String real = useFlux.substring(6, 8) + useFlux.substring(4, 6) + useFlux.substring(2, 4) + useFlux.substring(0, 2);
        double use = (Integer.parseInt(real, 16) - startUseFlux) / 1024;
        return use + "";
    }

}
