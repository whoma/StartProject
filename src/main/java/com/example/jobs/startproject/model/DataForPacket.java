package com.example.jobs.startproject.model;

import android.os.Build;
import android.os.StatFs;

import com.example.jobs.startproject.util.HttpUtil;

import static com.example.jobs.startproject.util.HttpUtil.getIp;

/**
 * Created by jobs on 2016/9/27.
 */

public class DataForPacket {
    private static String salt;
    private static String acount;
    private static String passwd;
    private static String packet_tail;
    private static String tail;
    public static int count = -1;
    public static boolean first = true;
    private static long mac2Int = Long.parseLong(HttpUtil.getMac(), 16);
    public static String reSalt;

    public static String getTail() {
        return tail;
    }

    public static int getCount() {
        if (count >= 255) {
            count = -1;
        }
        return ++count;
    }


    public static void setPasswd(String passwd) {
        DataForPacket.passwd = passwd;
    }

    public static void setPacket_tail(String packet_tail) {
        DataForPacket.packet_tail = packet_tail;
    }

    public static void setTail(String tail) {
        DataForPacket.tail = tail;
    }

    public static void setAcount(String acount) {
        DataForPacket.acount = acount;
    }

    public static void setSalt(String salt) {
        DataForPacket.salt = salt;
    }


    static {
        salt = "";
        acount = "";
        passwd = "";
        packet_tail = "";
        tail = "00000000";
    }

    /**
     * 第一次的数据 Start Request
     */
    public static String Data2Request() {
        StringBuilder builder = new StringBuilder();
        builder.append("0102");
        builder.append(HttpUtil.get2ServerTime());
        builder.append("000000000000000000000000000000000");
        return builder.toString();
    }

    /**
     * 第二次的数据 Login Auth
     */
    public static String Data2Login() {
        StringBuilder builder = new StringBuilder();
        builder.append("030100");
        builder.append(HttpUtil.ToAsciiHex((char) (acount.length() + 20)));
        String md5A = HttpUtil.getMD5("0301" + salt + HttpUtil.ToAsciiHex(passwd));
        builder.append(md5A);
        builder.append(String.format("%1$-" + 72 + "s", HttpUtil.ToAsciiHex(acount)).replace(" ", "0"));
        builder.append("20");
        builder.append("04");
        long a = Long.parseLong(md5A.substring(0, 12), 16);
        String ss = HttpUtil.Dump(a ^ mac2Int);
        builder.append(String.format("%1$" + 12 + "s", ss).replace(" ", "0"));
        builder.append(HttpUtil.getMD5("01" + HttpUtil.ToAsciiHex(passwd) + salt + "00000000"));
        builder.append("01");
        builder.append(HttpUtil.getIp());
        builder.append("00000000");
        builder.append("00000000");
        builder.append("00000000");
        builder.append(HttpUtil.getMD5(builder.toString() + "1400070b").substring(0, 16));
        builder.append("01");
        builder.append("00000000");
        builder.append(String.format("%1$-" + 64 + "s", HttpUtil.ToAsciiHex(Build.MODEL)).replace(" ", "0"));
        builder.append("08080808");
        builder.append(HttpUtil.getGateWay());
        builder.append("00000000");
        builder.append("0000000000000000");
        builder.append("94000000");
        builder.append("05000000");
        builder.append("01000000");
        builder.append("280a0000");
        builder.append("02000000");
        builder.append(String.format("%1$-" + 256 + "s", HttpUtil.ToAsciiHex("ANDROID")).replace(" ", "0"));
        builder.append("2100");
        builder.append("020c");
        builder.append(HttpUtil.CheckSum(builder.toString() + "012607110000" + HttpUtil.Dump(mac2Int)));
        builder.append("0000");
        builder.append(HttpUtil.Dump(mac2Int));
        builder.append("00");
        builder.append("00");
        builder.append("e913");
        return builder.toString();
    }

    /**
     * 第三次的数据 Alive
     */
    public static String Date2Alive() {
        String data = "ff" + HttpUtil.getMD5("0301" + salt + HttpUtil.ToAsciiHex(passwd)) + "000000";
        data += packet_tail;
        data += HttpUtil.get2ServerTime();
        return data;
    }

    /**
     * 第四次的数据 Misc Type 1
     *             Misc Type 3
     */
    public static String HeartBeatPacket(int number, int type, boolean first) {
        String keepAliveData = "07" + HttpUtil.ToAsciiHex(((char)number)) + "28000b" + HttpUtil.ToAsciiHex((char)type);
        if (first) {
            keepAliveData += "0f27";
        }
        else {
            keepAliveData += "1d00";
        }
        keepAliveData += "2f12000000000000";
        keepAliveData += tail;
        keepAliveData += "00000000";
        if (type == 3) {
            keepAliveData += "00000000" + HttpUtil.getIp() + "0000000000000000";
        }
        else {
            keepAliveData += "00000000000000000000000000000000";
        }
        return keepAliveData;
    }

    /**
     * 注销第一次
     */
    public static String OfflineOne() {
        String source = "0103" + HttpUtil.get2ServerTime() + "21" + "000000000000000000000000000000";
        return source;
    }

    /**
     * 注销第二次
     */
    public static String offlineSecond() {
        String source = "060100";
        source+= HttpUtil.ToAsciiHex((char) (acount.length() + 20));
        String md5_A = HttpUtil.getMD5("0601" + reSalt + HttpUtil.ToAsciiHex(passwd)); //Md5(code+type+challenge+passwd)
        source += md5_A;
        source += String.format("%1$-" + 72 + "s", HttpUtil.ToAsciiHex(acount)).replace(" ", "0");
        source += "2001";
        long md5a = Long.parseLong(md5_A.substring(0, 12), 16);

        source += String.format("%1$" + 12 + "s", HttpUtil.Dump(md5a ^ mac2Int)).replace(" ", "0");
        source += packet_tail;
        return source;
    }


}
