package com.example.jobs.startproject.util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.context.MyApplication;
import com.example.jobs.startproject.model.UpdateInfo;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by jobs on 2016/10/18.
 */

public class UpdateManager {
    private static UpdateInfo updateInfo = null;
    private static Gson gson = new Gson();
    private static int now_Versioncode;
    private static ProgressBar progressBar;
    private static final int START = 1;
    private static final int DOWNLOAD = 2;
    private static File stogeFile;
    private static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case START:
                    AlertDialog.Builder sbuilder = new AlertDialog.Builder(MyApplication.getContext());
                    sbuilder.setTitle("下载");
                    View view = LayoutInflater.from(MyApplication.getContext()).inflate(R.layout.download_item, null);
                    progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
                    AlertDialog salertDialog = sbuilder.create();
                    salertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                    salertDialog.show();
                    break;
                case DOWNLOAD:
                    AlertDialog.Builder dbuilder = null;
                    if (Integer.parseInt(updateInfo.getVersion()) > now_Versioncode) {
                        dbuilder = new AlertDialog.Builder(MyApplication.getContext());
                        dbuilder.setTitle("更新");
                        dbuilder.setMessage(updateInfo.getInformation());
                        dbuilder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    downloadAPK();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        if (updateInfo.isForce()) {
                            dbuilder.setCancelable(false);
                        }

                        if (!updateInfo.isForce()) {
                            dbuilder.setNegativeButton("取消", null);
                        }
                    }
                    AlertDialog dalertDialog = dbuilder.create();
                    dalertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
                    dalertDialog.show();


                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 获得服务器上的信息
     */
    public static void getUpdateInfo() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String data;
                HttpURLConnection conn = null;
                try {
                    now_Versioncode = MyApplication.getContext().getPackageManager().getPackageInfo(MyApplication.getContext().getPackageName(), 0).versionCode;
                    data = "type=mobile&version=" + now_Versioncode + "&ip=" + HttpUtil.getIpFormat();
                    conn = (HttpURLConnection) new URL("http://starterproject.f3322.net:61441/index.php?action=update").openConnection();
                    conn.setConnectTimeout(8000);
                    conn.setReadTimeout(8000);
                    conn.setRequestMethod("POST");
                    conn.setDoOutput(true);
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    out.writeBytes(data);
                    conn.connect();
                    InputStream in = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    updateInfo = gson.fromJson(builder.toString(), UpdateInfo.class);
                    UpdateING();
                    in.close();
                    reader.close();
                    out.close();
                    conn.disconnect();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 比较是否需要升级
     * dong big
     */
    public static void UpdateING() {
        handler.sendEmptyMessage(DOWNLOAD);
    }

    /**
     * 下载apk
     */
    public static void downloadAPK() {
        try {
            File file = new File(Environment.getExternalStorageDirectory(), "starterproject");
            if (!file.exists()) {
                file.mkdir();
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(updateInfo.getAndroiddownloadlink()).openConnection();
            httpURLConnection.connect();
            int length = httpURLConnection.getContentLength();
            int acount = 0;
            handler.sendEmptyMessage(START);
            InputStream in = httpURLConnection.getInputStream();
            stogeFile = new File(file, "starterproject.apk");
            OutputStream out = new FileOutputStream(stogeFile);
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = in.read(bytes)) != -1) {
                out.write(bytes, 0, len);
                acount += len;
                progressBar.setProgress((int) (((float) acount / length) * 100));
            }
            in.close();
            out.close();
            installAPK();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 安装apk
     */
    public static void installAPK() {
        if (!stogeFile.exists()) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + stogeFile.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        MyApplication.getContext().startActivity(intent);
    }

}
