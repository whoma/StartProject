package com.example.jobs.startproject.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.model.UserInfo;
import com.example.jobs.startproject.util.HttpUtil;
import com.example.jobs.startproject.util.ImageUtil;
import com.googlecode.tesseract.android.TessBaseAPI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Study_Activity extends Activity {
    public static List<UserInfo> list = new ArrayList<UserInfo>();
    private EditText edit_name;
    private EditText edit_passwd;
    private ProgressBar progressBar;
    private static String name;
    private static String passwd;
    private String vcode;
    private String cookie;
    private static final int GETDATA = 1;
    private static final int SHOWPROGRESS = 2;
    private static final int CLOSEPROGRESS = 3;
    private static final int SHOWERROR = 4;
    private static boolean isDO = false;
    private static InputMethodManager inputMethodManager;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GETDATA:
                    Intent i = new Intent(Study_Activity.this, ShowClass.class);
                    startActivity(i);
                    break;
                case SHOWPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    break;
                case CLOSEPROGRESS:
                    progressBar.setVisibility(View.GONE);
                    break;
                case SHOWERROR:
                    Toast.makeText(Study_Activity.this, "您的密码不正确，请您重新输入！", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };
    //sd卡路径
    private static final String SD_PATH = Environment.getExternalStorageDirectory().toString();
    //字典名
    private static final String DICTIONARY = "/eng";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        //setContentView(R.layout.study_activity);
        View view = LayoutInflater.from(this).inflate(R.layout.study_activity, null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("StarterProject");
        builder.setCancelable(false);
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ChooseActivity.isHaveClass = false;
                finish();
            }
        });
        builder.setPositiveButton("绑定", null);

        edit_name = (EditText) view.findViewById(R.id.edit_name);
        edit_passwd = (EditText) view.findViewById(R.id.edit_passwd);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);


        AlertDialog dialog = builder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialog) {
                Button positive_bt = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE);
                positive_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        name = edit_name.getText().toString();
                        passwd = edit_passwd.getText().toString();
                        inputMethodManager.hideSoftInputFromWindow(edit_passwd.getWindowToken(), 0);
                        inputMethodManager.hideSoftInputFromWindow(edit_name.getWindowToken(), 0);
                        if (HttpUtil.isAvaliable()) {
                            stogeFile();
                            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(passwd)) {
                                Toast.makeText(Study_Activity.this, "账号和密码不能为空", Toast.LENGTH_SHORT).show();
                            } else {
                                //进行登录
                                ChooseActivity.isHaveClass = false;
                                while (true) {
                                    if (isDO) {
                                        GetData();
                                        break;
                                    }
                                }

                            }
                        } else {
                            Toast.makeText(Study_Activity.this, "WIFI未打开", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        dialog.show();
        //


    }

    public void GetData() {


        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Message message1 = new Message();
                message1.what = SHOWPROGRESS;
                handler.sendMessage(message1);
                while (true) {
                    //
                    try {
                        //
                        while (true) {
                            URL url1 = new URL("http://172.16.88.102/validateCodeAction.do?random=0.49367743948412746");
                            HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                            connection1.setRequestMethod("GET");
                            connection1.setConnectTimeout(8000);
                            connection1.setReadTimeout(8000);
                            Bitmap bitmap = BitmapFactory.decodeStream(connection1.getInputStream());
                            cookie = connection1.getHeaderField("Set-Cookie").split(";")[0];
                            //bitmap = ImageUtil.gray2Binary(bitmap);
                            bitmap = ImageUtil.grayScaleImage(bitmap);
                            //bitmap = ImageUtil.convertToBlackWhite(bitmap);

                            TessBaseAPI baseApi = new TessBaseAPI();
                            baseApi.init(SD_PATH, DICTIONARY);
                            baseApi.setPageSegMode(TessBaseAPI.PageSegMode.PSM_AUTO);
                            baseApi.setImage(bitmap);
                            baseApi.setVariable("tessedit_char_whitelist", "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm0123456789");
                            final String result = baseApi.getUTF8Text();
                            vcode = result;
                            if (vcode.length() == 4) {
                                baseApi.end();
                                break;
                            }
                        }
                        //

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                    try {
                        URL url = new URL("http://172.16.88.102/loginAction.do");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoOutput(true);
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Cookie", cookie);
                        connection.setReadTimeout(8000);
                        connection.setConnectTimeout(8000);
                        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                        out.writeBytes("zjh1=&tips=&lx=&evalue=&eflag=&fs=&dzslh=&zjh=" + name + "&mm=" + passwd + "&v_yzm=" + vcode);
                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "GBK"));
                        StringBuilder builder = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            builder.append(line);
                            builder.append("\n");
                        }
                        Log.w("post data", builder.toString());
                        if (builder.toString().contains("你输入的验证码错误，请您重新输入！")) {
                            continue;
                        }

                        if (builder.toString().contains("您的密码不正确，请您重新输入！")) {
                            Message message3 = new Message();
                            message3.what = SHOWERROR;
                            handler.sendMessage(message3);
                            break;
                        }
                        // http://172.16.88.102/xkAction.do?actionType=6
                        URL url2 = new URL("http://172.16.88.102/xkAction.do?actionType=6");
                        connection = (HttpURLConnection) url2.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        connection.setRequestProperty("Cookie", cookie);
                        InputStream ins = connection.getInputStream();
                        BufferedReader reader1 = new BufferedReader(new InputStreamReader(ins, "GBK"));
                        builder.setLength(0);
                        line = "";
                        while ((line = reader1.readLine()) != null) {
                            builder.append(line);
                            builder.append("\n");
                        }

                        if (builder.toString().equals("初始化函数")) {
                            continue;
                        }

                        Document document = Jsoup.parse(builder.toString());
                        Elements elements = document.select("tr.odd");
                        // 遍历
                        for (Element element : elements) {
                            Elements ele = element.select("td");
                            UserInfo info = new UserInfo();
                            if (ele.size() > 7) {
                                info.setMajor_plan(ele.get(0).text().toString().trim());
                                info.setCourse_id(ele.get(1).text().toString().trim());
                                info.setCourse_name(ele.get(2).text().toString().trim());
                                info.setCourse_number(ele.get(3).text().toString().trim());
                                info.setCourse_grades(ele.get(4).text().toString().trim());
                                info.setCourse_attribute(ele.get(5).text().toString().trim());
                                info.setExam_type(ele.get(6).text().toString().trim());
                                info.setTeach_name(ele.get(7).text().toString().trim());
                                info.setStudy_type(ele.get(9).text().toString().trim());
                                info.setCourse_status(ele.get(10).text().toString().trim());
                                info.setStudy_time(ele.get(11).text().toString().trim());
                                info.setStudy_week(ele.get(12).text().toString().trim());
                                info.setCourse_period(ele.get(13).text().toString().trim());
                                info.setCourse_times(ele.get(14).text().toString().trim());
                                info.setStudy_place(ele.get(15).text().toString().trim());
                                info.setCourse_building(ele.get(16).text().toString().trim());
                                info.setCourse_classroom(ele.get(17).text().toString().trim());
                                list.add(info);
                            } else {
                                int index = list.size() - 1;
                                UserInfo info_last = list.get(index);
                                info.setMajor_plan(info_last.getMajor_plan());
                                info.setCourse_id(info_last.getCourse_id());
                                info.setCourse_name(info_last.getCourse_name());
                                info.setCourse_number(info_last.getCourse_number());
                                info.setCourse_grades(info_last.getCourse_grades());
                                info.setCourse_attribute(info_last.getCourse_attribute());
                                info.setExam_type(info_last.getExam_type());
                                info.setTeach_name(info_last.getTeach_name());
                                info.setStudy_time(info_last.getStudy_time());
                                info.setStudy_type(info_last.getStudy_type());
                                info.setCourse_status(info_last.getCourse_status());
                                // dd
                                info.setStudy_time(ele.get(0).text().toString().trim());
                                info.setStudy_week(ele.get(1).text().toString().trim());
                                info.setCourse_period(ele.get(2).text().toString().trim());
                                info.setCourse_times(ele.get(3).text().toString().trim());
                                info.setStudy_place(ele.get(4).text().toString().trim());
                                info.setCourse_building(ele.get(5).text().toString().trim());
                                info.setCourse_classroom(ele.get(6).text().toString().trim());
                                list.add(info);
                            }

                        }

                        StringBuilder builder1 = new StringBuilder();
                        for (UserInfo userInfo : list) {
                            builder1.append(userInfo.toString());
                        }
                        Message message = new Message();
                        message.what = GETDATA;
                        handler.sendMessage(message);

                        break;

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                Message message2 = new Message();
                message2.what = CLOSEPROGRESS;
                handler.sendMessage(message2);
                finish();
            }
        });
        thread.start();


    }

    /**
     * 将raw上的文件存入到本地上
     */
    public static void stogeFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                File SD_CARD = Environment.getExternalStorageDirectory();
                File allFile = new File(SD_CARD, "/tessdata/eng.traineddata");
                if (!allFile.exists()) {
                    File fileDir = new File(SD_CARD, "/tessdata/");
                    //
                    HttpURLConnection httpURLConnection = null;
                    InputStream inputStream = null;
                    try {
                        httpURLConnection = (HttpURLConnection) new URL("http://10.10.10.235/tessdata/eng.traineddata").openConnection();
                        httpURLConnection.connect();
                        inputStream = httpURLConnection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    //
                    //InputStream inputStream = MyApplication.getContext().getResources().openRawResource(R.raw.eng);
                    try {
                        if (!fileDir.exists()) {
                            fileDir.mkdirs();
                        }
                        File file = new File(fileDir, "eng.traineddata");
                        if (!file.exists()) {
                            file.createNewFile();
                        }
                        FileOutputStream fileOutputStream = new FileOutputStream(file, true);
                        byte b[] = new byte[1024];
                        int len = 0;
                        while ((len =  inputStream.read(b)) != -1) {
                            fileOutputStream.write(b, 0, len);
                        }
                        inputStream.close();
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                isDO = true;
            }
        }).start();
    }

}
