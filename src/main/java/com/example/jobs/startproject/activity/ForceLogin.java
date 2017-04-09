package com.example.jobs.startproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.jobs.startproject.R;

public class ForceLogin extends Activity {
    private TextView text_info;
    private TextView bt_no;
    private TextView bt_yes;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_force);
        setFinishOnTouchOutside(false);
        text_info = (TextView) findViewById(R.id.text_info);
        bt_no = (Button) findViewById(R.id.bt_no);
        bt_yes = (Button) findViewById(R.id.bt_yes);
        String info = getIntent().getStringExtra("source");
        text_info.setText("该账号已经在ip为 " + info + " 上登陆" + "是否使用强制登陆？");
        bt_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ForceLogin.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        bt_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               //DO
            }
        });

        setTitle("消息");
    }
}
