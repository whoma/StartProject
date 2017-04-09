package com.example.jobs.startproject.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.activity.ChooseActivity;
import com.example.jobs.startproject.activity.LoginActivity;

/**
 * Created by jobs on 2016/10/14.
 */

public class ChooseLayout extends LinearLayout {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    public ChooseLayout(final Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.myself_choose, this);
        toolbar = (Toolbar) findViewById(R.id.toolbar_choose);
        toolbar.setTitle(" StarterProject");
        toolbar.setNavigationIcon(R.drawable.black);
        ((AppCompatActivity)context).setSupportActionBar(toolbar);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout_choose);
        navigationView = (NavigationView) findViewById(R.id.navigation_choose);


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
                        Intent i1 = new Intent(context, LoginActivity.class);
                        ((Activity)context).startActivity(i1);
                        break;
                    case R.id.study_item:
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
