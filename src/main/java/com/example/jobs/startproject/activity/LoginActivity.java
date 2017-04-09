package com.example.jobs.startproject.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.keepalive.RemoteService;
import com.example.jobs.startproject.model.AcountAdapter;
import com.example.jobs.startproject.model.CallBackOnclick;
import com.example.jobs.startproject.model.DataForPacket;
import com.example.jobs.startproject.model.Tooffline;
import com.example.jobs.startproject.model.UserData;
import com.example.jobs.startproject.util.HttpCallBack;
import com.example.jobs.startproject.util.HttpSend;
import com.example.jobs.startproject.util.HttpUtil;
import com.example.jobs.startproject.util.Socket2Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {
    private static CoordinatorLayout root;
    private EditText acount_text;
    private EditText passwd_text;
    private Button bt_login;
    private CheckBox checkBox;
    private ImageView eye;
    private String acount;
    private String passwd;
    private static boolean STOP = false;
    private static boolean isHeart = false;
    public static boolean isContinue = false;
    private static String alreadyIp = "";
    static boolean isForceLogin = false;
    private ImageView image;
    private FileInputStream in;
    private FileOutputStream out;
    private BufferedReader reader;
    private BufferedWriter writer;
    private List<UserData> lists;
    private Gson gson;
    private String writeAcount;
    private String writePasswd;
    private AcountAdapter adapter;
    private PopupWindow window;
    private ListView listView;
    private static String allTime;
    private static String allFlux;
    private static String startFlux;
    private boolean isVisiable = true;
    private static boolean isSuccess = true;
    private DrawerLayout drawerLayout;
    public static NavigationView navigationView;
    private static final int STOPIN = 1;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOPIN:
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setTitle("Info");
                    builder.setMessage("该账号已经在ip为 " + alreadyIp + " 上登陆" + "是否使用强制登陆？");
                    builder.setPositiveButton("是的", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            isForceLogin = true;
                            isSuccess = false;
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            STOP = true;
                            isSuccess = false;
                        }
                    });
                    builder.show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_login);
        // toolbar.setLogo(R.drawable.wifi);
        toolbar.setNavigationIcon(R.drawable.black);
        toolbar.setTitle(" StarterProject");
        setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_login);
        navigationView = (NavigationView) findViewById(R.id.navigation_login);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.wifi_item:
                        break;
                    case R.id.study_item:
                        item.setCheckable(false);
                        if (ChooseActivity.isClassSuccess || ChooseActivity.isHaveClass) {
                            Intent i = new Intent(LoginActivity.this, ShowClass.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(LoginActivity.this, Study_Activity.class);
                            startActivity(i);
                        }
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });

        //
        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar.setTitle(navigationView.getMenu().getItem(0).getTitle());
        //

        acount = null;
        passwd = null;
        image = (ImageView) findViewById(R.id.image);
        root = (CoordinatorLayout) findViewById(R.id.root);
        acount_text = (EditText) findViewById(R.id.acount_text);
        passwd_text = (EditText) findViewById(R.id.passwd_text);
        bt_login = (Button) findViewById(R.id.bt_login);
        checkBox = (CheckBox) findViewById(R.id.checkbox);
        image = (ImageView) findViewById(R.id.image);
        eye = (ImageView) findViewById(R.id.eye);
        gson = new Gson();


        passwd_text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                btlogin_OnClick();
                return true;
            }
        });

        eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVisiable) {
                    passwd_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    passwd_text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                isVisiable = !isVisiable;
            }
        });

        String userDate = openFileReader();
        if (!TextUtils.isEmpty(userDate) && !userDate.equals("[]")) {
            lists = gson.fromJson(userDate, new TypeToken<List<UserData>>() {
            }.getType());
            writeAcount = lists.get(0).getAcount();
            writePasswd = lists.get(0).getPasswd();
            acount_text.setText(writeAcount);
            passwd_text.setText(writePasswd);
            checkBox.setChecked(true);
        } else {
            lists = new ArrayList<UserData>();
        }

        listView = new ListView(this);
        adapter = new AcountAdapter(LoginActivity.this, R.layout.simple_item, lists, new CallBackOnclick() {
            @Override
            public void onClick(View v) {
                int position = (int) v.getTag();
                switch (v.getId()) {
                    case R.id.more_acount:
                        writeAcount = lists.get(position).getAcount();
                        writePasswd = lists.get(position).getPasswd();
                        acount_text.setText(writeAcount);
                        passwd_text.setText(writePasswd);
                        checkBox.setChecked(true);
                        window.dismiss();
                        break;
                    case R.id.image_delete:
                        lists.remove(position);
                        nowStoge();
                        adapter.notifyDataSetChanged();
                        break;
                    default:
                        break;
                }
            }
        });

        listView.setAdapter(adapter);

        window = new PopupWindow(listView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        window.setTouchable(true);
        window.setOutsideTouchable(true);
        window.setBackgroundDrawable(new ColorDrawable());

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.setWidth(acount_text.getMeasuredWidth() + image.getMeasuredWidth());
                window.showAsDropDown(acount_text);
            }
        });

        // 输入时，密码框为空；当返回上次输入的时候，密码又回来了。
        final String beforeAcount = acount_text.getText().toString();
        final String beforePasswd = passwd_text.getText().toString();
        acount_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                passwd_text.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals(beforeAcount)) {
                    passwd_text.setText(beforePasswd);
                }
            }
        });
        //

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(LoginActivity.this, RemoteService.class));
                btlogin_OnClick();
            }
        });
    }


    public void btlogin_OnClick() {
        acount = acount_text.getText().toString();
        passwd = passwd_text.getText().toString();

        if (HttpUtil.isAvaliable()) {
            if (TextUtils.isEmpty(acount) || TextUtils.isEmpty(passwd)) {
                showSnack("账号和密码不能为空");
            } else {
                DataForPacket.setAcount(acount);
                DataForPacket.setPasswd(passwd);
                goLogin();
            }
        } else {
            showSnack("WIFI未打开");
        }
    }

    public static void showSnack(String source) {
        Snackbar.make(root, source, Snackbar.LENGTH_LONG).show();
    }

    public void goLogin() {
        STOP = false;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (this) {
                    while (true) {

                        //发送handShake
                        Socket2Server.Date2Socket(DataForPacket.Data2Request(), Socket2Server.SIZE_REQUEST, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                if (response.substring(0, 2).equals("02")) {
                                    DataForPacket.setSalt(response.substring(8, 16));
                                } else {
                                    showSnack("访问服务器失败");
                                    STOP = true;
                                }
                            }

                            @Override
                            public void onError(Exception error) {
                                showSnack("访问服务器失败");
                                STOP = true;
                            }
                        });
                        if (STOP) {
                            break;
                        }


                        //发送login
                        Socket2Server.Date2Socket(DataForPacket.Data2Login(), Socket2Server.SIZE_LOGIN, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                if (response.substring(0, 2).equals("05") && response.substring(8, 10).equals("03")) {
                                    showSnack("密码错误");
                                    STOP = true;
                                } else if (response.substring(0, 2).equals("05") && response.substring(8, 10).equals("16")) {
                                    showSnack("此WIFI无法进行登陆，请更换WIFI再重试");
                                    STOP = true;
                                } else if (response.substring(0, 2).equals("05") && response.substring(8, 10).equals("01")) {
                                    String ipString = response.substring(10, 18);
                                    for (int i = 0; i < 4; i++) {
                                        alreadyIp += Integer.valueOf(ipString.substring(i * 2, (i * 2) + 2), 16).toString();
                                        if (i == 3) {
                                            break;
                                        }
                                        alreadyIp += ".";
                                    }
                                    /**
                                     * 进行大招
                                     Intent intent = new Intent(LoginActivity.this, ForceLogin.class);
                                     intent.putExtra("source", alreadyIp);
                                     startActivity(intent);
                                     */
                                    //
                                    isForceLogin = false;
                                    Message message = new Message();
                                    message.what = STOPIN;
                                    handler.sendMessage(message);


                                    while (true) {
                                        if (!isSuccess) {
                                            break;
                                        }
                                    }


                                } else if (response.substring(0, 2).equals("04")) {
                                    DataForPacket.setPacket_tail(response.substring(46, 78));
                                    allTime = response.substring(10, 18);
                                    allFlux = response.substring(18, 26);
                                    //进行保存密码
                                    if (checkBox.isChecked()) {
                                        if (!lists.contains(new UserData(acount, passwd))) {
                                            lists.add(new UserData(acount, passwd));
                                            nowStoge();
                                        }
                                    }
                                }

                            }

                            @Override
                            public void onError(Exception error) {
                                showSnack("密码错误或出现异常");
                                STOP = true;
                            }
                        });


                        //启动force login
                        if (isForceLogin) {
                            forceOffline();
                            continue;
                        }


                        if (STOP) {
                            break;
                        }
                        /**
                         * Misc
                         */
                        Socket2Server.Date2Socket(DataForPacket.Date2Alive(), Socket2Server.SIZE_ALIVE, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                startFlux = response.substring(16, 25);
                            }

                            @Override
                            public void onError(Exception error) {
                                STOP = true;
                            }
                        });
                        if (STOP) {
                            break;
                        }


                        /**
                         * send1
                         */
                        do {
                            Socket2Server.Date2Socket(DataForPacket.HeartBeatPacket(DataForPacket.getCount(), 1, DataForPacket.first), Socket2Server.SIZE_MISC, new HttpCallBack() {
                                @Override
                                public void onFinish(String response) {

                                    String sCount = String.format("%2s", Integer.toHexString(DataForPacket.count)).replace(" ", "0");
                                    if (response.startsWith("07002800") || response.startsWith("07" + sCount + "2800")) {
                                        isContinue = false;
                                    } else if (response.substring(0, 2).equals("07") && response.substring(4, 6).equals("10")) {
                                        DataForPacket.setTail("00000000");
                                        DataForPacket.first = false;
                                        isContinue = true;

                                    }
                                }

                                @Override
                                public void onError(Exception error) {
                                    //DO
                                    STOP = true;
                                }

                            });

                        } while (isContinue);

                        if (STOP) {
                            break;
                        }

                        /**
                         * send3
                         */
                        Socket2Server.Date2Socket(DataForPacket.HeartBeatPacket(DataForPacket.getCount(), 3, false), Socket2Server.SIZE_MISC, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                DataForPacket.setTail(response.substring(32, 40));
                                isHeart = true;
                            }

                            @Override
                            public void onError(Exception error) {
                                STOP = true;
                            }
                        });

                        if (STOP) {
                            break;
                        }
                        /**
                         * 心跳包布局
                         */
                        if (isHeart) {
                            STOP = true;
                            Intent i = new Intent(LoginActivity.this, SomeInfo.class);
                            startActivity(i);

                            finish();
                        }

                        if (STOP) {
                            break;
                        }
                    }
                }

            }
        });
        thread.start();
    }

    public static String getAllTime() {
        allTime += " ";
        String realTime = allTime.substring(6, 8) + allTime.substring(4, 6) + allTime.substring(2, 4) + allTime.substring(0, 2);
        return Integer.parseInt(realTime, 16) + "";
    }

    public static String getAllFlux() {
        allFlux += " ";
        String realflux = allFlux.substring(6, 8) + allFlux.substring(4, 6) + allFlux.substring(2, 4) + allFlux.substring(0, 2);
        int flow = Integer.parseInt(realflux, 16);
        int flow0 = flow % 1024;
        int flow1 = flow - flow0;
        flow0 = flow0 * 1000;
        flow0 = flow0 - flow0 % 1024;
        return flow1 / 1024 + "." + flow0 / 1024 + "";
    }

    public static int getStartFlux() {
        startFlux += " ";
        String real = startFlux.substring(6, 8) + startFlux.substring(4, 6) + startFlux.substring(2, 4) + startFlux.substring(0, 2);
        return Integer.parseInt(real, 16);
    }

    public boolean forceOffline() {
        // login web 获取checkcode
        String address1 = "http://172.16.35.202/nav_login";
        String source1 = HttpSend.HttpGetAndPost(address1, "GET");
        String findCode = "checkcode=\"([\\d\\.]+)\";";
        Pattern pattern = Pattern.compile(findCode);
        Matcher matcher = pattern.matcher(source1);
        String checkCode = "";
        if (matcher.find()) {
            checkCode = matcher.group(0).split("\"")[1];
        }

        //验证码
        String address2 = "http://172.16.35.202/RandomCodeAction.action?randomNum=0.07176706276647526";
        HttpSend.HttpGetAndPost(address2, "POST");

        //login web 登陆成功
        String address3 = "http://172.16.35.202/LoginAction.action";
        HttpSend.value = "account=" + acount + "&password=" + HttpUtil.getSingleMD5(passwd) + "&code=&checkcode=" + checkCode + "&Submit=%E7%99%BB+%E5%BD%95";
        HttpSend.isValue = true;
        HttpSend.HttpGetAndPost(address3, "POST");
        HttpSend.isValue = false;
        String address4 = "http://172.16.35.202/nav_offLine";
        String source3 = HttpSend.HttpGetAndPost(address4, "POST");
        Document source = Jsoup.parse(source3);
        Elements sourceOftbody = source.select("tbody");
        Elements sourceOftr = sourceOftbody.select("tr");
        String fldsessionid[] = new String[sourceOftr.size()];
        for (int i = 0; i < sourceOftr.size(); i++) {
            Element tr = sourceOftr.get(i);
            fldsessionid[i] = tr.select("td").get(3).text();
        }
        //http://172.16.35.202/tooffline?t=+Math.random()+&fldsessionid=
        //注销
        Gson gson = new Gson();
        Tooffline tooffline;
        for (String s : fldsessionid) {
            String address = "http://172.16.35.202/tooffline?t=" + Math.random() + "&fldsessionid=" + s;
            String data = HttpSend.HttpGetAndPost(address, "GET");
            tooffline = gson.fromJson(data, Tooffline.class);
            if (!tooffline.getDate().equals("success")) {
                return false;
            }
        }
        return true;
    }

    public String openFileReader() {
        try {
            //先打开如果不存在就 新建
            out = openFileOutput("user", MODE_APPEND);
            in = openFileInput("user");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return builder.toString();
    }

    //立即写入
    public void nowStoge() {
        String data = gson.toJson(lists);
        try {
            out = openFileOutput("user", MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            writer.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                writer.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
