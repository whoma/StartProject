package com.example.jobs.startproject.util;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.example.jobs.startproject.context.MyApplication;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static java.net.InetAddress.*;

/**
 * Created by jobs on 2016/9/27.
 */

public class HttpUtil {
    private static WifiManager wifiManager = null;
    private static WifiInfo wifiInfo = null;
    private static StringBuilder builder = null;

    static {
        wifiManager = (WifiManager) MyApplication.getContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        builder = new StringBuilder();
    }

    /**
     * 检查网络是否可用
     */
    public static boolean isAvaliable() {
        if (wifiManager.isWifiEnabled()) {
            return true;
        }
        return false;
    }

    /**
     * 将signedLong变成unsignedLong
     */
    public static long toUnsignedLong(long a) {
        if (a > 2147483647) {
            a = -2147483648 + (a - 2147483648L);
        }
        return a;
    }

    /**
     * 校验和
     */
    public static String CheckSum(String s) {
        long sum = 1234;
        long check = 0;
        byte bytes[] = String2Bytes(s);
        for (int i = 0; i + 4 < bytes.length; i += 4) {
            check = 0;
            for (int j = 0; j < 4; j++) {
                check = check * 256 + (int) bytes[i + j];
                check = toUnsignedLong(check);
            }
            sum ^= check;
            sum = toUnsignedLong(sum);
        }
        int reSum = ((int) (1968 * sum)) & 0xffffffff;
        return String.format("%2s", Integer.toHexString(reSum)).replace(" ", "0");
    }

    /**
     * 得到mac地址
     */
    public static String getMac() {
        return wifiInfo.getMacAddress().replace(":", "");
    }

    /**
     * 得到ip地址并以16进制输出
     */
    public static String getIp() {
        return int2hex(wifiInfo.getIpAddress());
    }

    /**
     * 将ip或者网关地址换算成16进制
     */
    public synchronized static String int2hex(int intSource) {
        builder.setLength(0);
        builder.append(intSource & 0xff).append(".");
        builder.append((intSource >> 8) & 0xff).append(".");
        builder.append((intSource >> 16) & 0xff).append(".");
        builder.append((intSource >> 24) & 0xff);
        String muti[] = builder.toString().split("\\.");
        String aim = "";
        for (int i = 0; i < muti.length; i++) {
            aim += String.format("%2s", Integer.toHexString(Integer.parseInt(muti[i]))).replace(" ", "0");
        }
        return aim;
    }

    /**
     * 得到ip,并以 xx.xx.xx.xx 输出
     */
    public static String getIpFormat() {
        String alreadyIp = "";
        String ip = getIp();
        for (int i = 0; i < 4; i++) {
            alreadyIp += Integer.valueOf(ip.substring(i * 2, (i * 2) + 2), 16).toString();
            if (i == 3) {
                break;
            }
            alreadyIp += ".";
        }
        return alreadyIp;
    }

    /**
     * 得到网关地址并以16进制输出
     */
    public static String getGateWay() {
        DhcpInfo dhcpInfo = wifiManager.getDhcpInfo();
        int intGateway = dhcpInfo.gateway;
        return int2hex(intGateway);
    }

    /**
     * Dump()方法
     */
    public static String Dump(long n) {
        String s = String.format("%2s", Long.toHexString(n)).replace(" ", "0");
        if ((s.length() & 1) != 0) {
            s = '0' + s;
        }
        return s;
    }

    /**
     * 将string变成ascii
     */
    public static String ToAsciiHex(String string) {
        String ss = "";
        for (char c : string.toCharArray()) {
            ss += String.format("%2s", Integer.toHexString(c)).replace(" ", "0");
        }
        return ss;
    }

    /**
     * 将char变成ascii
     */
    public static String ToAsciiHex(char c) {
        String ss = String.format("%2s", Integer.toHexString(c)).replace(" ", "0");
        return ss;
    }

    /**
     * md5 加密
     */

    public static String getSingleMD5(String string) {
        StringBuilder builder = new StringBuilder();
        try {
            byte bytes[] = string.getBytes("utf-8");
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte result[] = md5.digest(bytes);
            for (byte b : result) {
                builder.append(String.format("%2s", Integer.toHexString(b & 0xff)).replace(" ", "0"));
            }

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return builder.toString();
    }
    public static String getMD5(String string) {
        String ss = "";
        byte bytes[] = String2Bytes(string);
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte result[] = md5.digest(bytes);
            for (byte b : result) {
                ss += String.format("%2s", Integer.toHexString(b & 0xff)).replace(" ", "0");
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return ss;
    }

    /**
     * 每两位10进制转为一位的16进制
     */
    public static byte[] String2Bytes(String string) {
        if (string.length() % 2 != 0) {
            string += "0";
        }
        byte bytes[] = new byte[string.length() / 2];
        for (int i = 0; i < string.length() / 2; i++) {
            bytes[i] = (byte) Integer.parseInt(string.substring(i * 2, i * 2 + 2), 16);
        }
        return bytes;
    }

    public static String byte2String(byte bytes[]) {
        String ss = "";
        for (int i = 0; i < 39; i++) {
            ss += String.format("%2s", Integer.toHexString(0xff & bytes[i])).replace(" ", "0");
        }
        return ss;
    }


    /**
     * unix 元年的hex最后4位在取反
     */
    public static String get2ServerTime() {
        long unix = System.currentTimeMillis() / 1000;
        String s = Long.toHexString(unix);
        s = s.substring(s.length() - 4, s.length());
        String res = s.substring(2, 4) + s.substring(0, 2);
        return res;
    }
}
