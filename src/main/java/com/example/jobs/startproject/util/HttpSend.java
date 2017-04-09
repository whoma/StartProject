package com.example.jobs.startproject.util;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by jobs on 2016/9/30.
 */

public class HttpSend {
    private static String cookie = "";
    private static URL url = null;
    private static HttpURLConnection connection = null;
    public static String value = "";
    public static boolean isValue = false;
    private static DataInputStream in;
    private static DataOutputStream dataOutputStream = null;

    public static String HttpGetAndPost(String address, String method) {
        //
        try {
            url = new URL(address);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //
        try {
            connection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //
        try {
            connection.setRequestMethod(method);
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        connection.setReadTimeout(800);
        connection.setConnectTimeout(800);
        if (!cookie.equals("")) {
            connection.setRequestProperty("Cookie", cookie);
        }
        if (isValue && method.equals("POST"))  {
            connection.setDoOutput(true);
            try {
                dataOutputStream = new DataOutputStream(connection.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                dataOutputStream.writeBytes(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        StringBuilder builder = new StringBuilder();
        String line;
        try {
            in = new DataInputStream(connection.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cookie.equals("")) {
            cookie = connection.getHeaderField("Set-Cookie").split(";")[0];
        }
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = builder.toString();
        return s;
    }
}
