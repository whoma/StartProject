package com.example.jobs.startproject.util;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * Created by jobs on 2016/9/27.
 */

public class Socket2Server {
    private static DatagramSocket socket;
    private static DatagramPacket packet;
    public final static int SIZE_REQUEST = 77;
    public final static int SIZE_LOGIN   = 46;
    public final static int SIZE_ALIVE   = 65;
    public final static int SIZE_MISC    = 41;
    public final static int SIZE_OFFLINE_SECOND = 26;

    static {
        try {
            socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public synchronized static void Date2Socket(String data, int size, HttpCallBack callBack) {
                   byte bytes[] = HttpUtil.String2Bytes(data);
                    try {
                        packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName("202.113.95.244"), 61440);
                        socket.send(packet);
                        byte receives[] = new byte[size];
                        packet = new DatagramPacket(receives, receives.length);
                        socket.receive(packet);
                        String source = "";
                        for (int i = 0; i < packet.getLength(); i++) {
                            source += String.format("%2s", Integer.toHexString(receives[i] & 0xff)).replace(" ", "0");
                        }

                        if (callBack != null) {
                            callBack.onFinish(source);
                        }
                    } catch (Exception e) {
                        if (callBack != null) {
                            callBack.onError(e);
                        }
                    }
    }
}
