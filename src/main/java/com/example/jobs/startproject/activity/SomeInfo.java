package com.example.jobs.startproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.model.ChooseLayout;
import com.example.jobs.startproject.model.DataForPacket;
import com.example.jobs.startproject.service.HeartService;
import com.example.jobs.startproject.util.BroadcastUtil;
import com.example.jobs.startproject.util.HttpCallBack;
import com.example.jobs.startproject.util.Socket2Server;

public class SomeInfo extends AppCompatActivity {
    private Button offline;
    private TextView text_alltime;
    private static TextView text_usetime;
    private TextView text_allflux;
    private static TextView text_useflux;
    public static NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar_choose;
    public static final int CHANGE = 1;
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE:
                    text_usetime.setText(HeartService.getUseTime()+ " Min");
                    text_useflux.setText(HeartService.getUseFlux() + " MByte");
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_someinfo);
        ChooseActivity.isLoginSuccess = true;
        navigationView = (NavigationView) findViewById(R.id.navigation_info);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_info);
        toolbar_choose = (Toolbar) findViewById(R.id.toolbar_info);
        toolbar_choose.setTitle(" StarterProject");
        toolbar_choose.setNavigationIcon(R.drawable.black);
        setSupportActionBar(toolbar_choose);
        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar_choose.setNavigationOnClickListener(new View.OnClickListener() {
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
                            Intent i = new Intent(SomeInfo.this, ShowClass.class);
                            startActivity(i);
                        } else {
                            Intent i = new Intent(SomeInfo.this, Study_Activity.class);
                            startActivity(i);
                        }
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                navigationView.getMenu().getItem(0).setChecked(true);
                return true;
            }
        });


        navigationView.getMenu().getItem(0).setChecked(true);
        toolbar_choose.setTitle(navigationView.getMenu().getItem(0).getTitle());

        HeartService.isService = true;
        Intent intent = new Intent(SomeInfo.this, HeartService.class);
        startService(intent);

        BroadcastUtil.register();
        offline = (Button) findViewById(R.id.offline);
        text_alltime = (TextView) findViewById(R.id.text_alltime);
        text_usetime = (TextView) findViewById(R.id.text_usetime);
        text_allflux = (TextView) findViewById(R.id.text_allflux);
        text_useflux = (TextView) findViewById(R.id.text_useflux);

        text_alltime.setText(LoginActivity.getAllTime() + " 分钟");
        text_allflux.setText(LoginActivity.getAllFlux() + " MByte");

        offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HeartService.isService = false;
                Intent i = new Intent(SomeInfo.this, HeartService.class);
                stopService(i);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Socket2Server.Date2Socket(DataForPacket.OfflineOne(), Socket2Server.SIZE_REQUEST, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                DataForPacket.reSalt = response.substring(8, 16);
                            }

                            @Override
                            public void onError(Exception error) {
                            }
                        });

                        Socket2Server.Date2Socket(DataForPacket.offlineSecond(), Socket2Server.SIZE_OFFLINE_SECOND, new HttpCallBack() {
                            @Override
                            public void onFinish(String response) {
                                finish();
                                Intent i = new Intent(SomeInfo.this, LoginActivity.class);
                                startActivity(i );
                            }

                            @Override
                            public void onError(Exception error) {
                            }
                        });

                    }
                }).start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
