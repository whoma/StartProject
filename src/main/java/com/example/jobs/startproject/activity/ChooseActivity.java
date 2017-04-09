package com.example.jobs.startproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.context.MyApplication;
import com.example.jobs.startproject.model.UserInfo;
import com.example.jobs.startproject.util.HttpUtil;
import com.example.jobs.startproject.util.UpdateManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class ChooseActivity extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar_choose;
    public static boolean isLoginSuccess = false;
    public static boolean isClassSuccess = false;
    private static Gson gson = new Gson();
    public static List<UserInfo> list = null;
    public static boolean isHaveClass = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose);
        UpdateManager.getUpdateInfo();

        isHaveClass = false;
        String data = openFile();
        if (!data.isEmpty()) {
            list = gson.fromJson(data, new TypeToken<List<UserInfo>>() {
            }.getType());
            isHaveClass = true;
            ShowClass.list = list;
        }

        navigationView = (NavigationView) findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        toolbar_choose = (Toolbar) findViewById(R.id.toolbar_choose);
        toolbar_choose.setTitle(" StartProject");
        toolbar_choose.setNavigationIcon(R.drawable.black);
        setSupportActionBar(toolbar_choose);

        toolbar_choose.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        drawerLayout.openDrawer(Gravity.LEFT);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setCheckable(false);
                switch (item.getItemId()) {
                    case R.id.wifi_item:
                        if (isLoginSuccess) {
                            Intent i1 = new Intent(ChooseActivity.this, SomeInfo.class);
                            startActivity(i1);
                        } else {
                            Intent i1 = new Intent(ChooseActivity.this, LoginActivity.class);
                            startActivity(i1);
                        }
                        break;
                    case R.id.study_item:
                        if (isClassSuccess || isHaveClass) {
                            Intent i2 = new Intent(ChooseActivity.this, ShowClass.class);
                            startActivity(i2);
                        } else {
                            Intent i2 = new Intent(ChooseActivity.this, Study_Activity.class);
                            startActivity(i2);
                        }
                        break;
                    default:
                        break;
                }
                drawerLayout.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
    }

    public static String openFile() {

        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        String line;
        try {
            in = MyApplication.getContext().openFileInput("user.data");
            reader = new BufferedReader(new InputStreamReader(in));
            if ((line = reader.readLine()) != null) {
                builder.append(line);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }
}
