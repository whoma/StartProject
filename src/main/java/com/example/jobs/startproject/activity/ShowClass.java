package com.example.jobs.startproject.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.util.ArrayMap;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.context.MyApplication;
import com.example.jobs.startproject.model.MyAdapter;
import com.example.jobs.startproject.model.OneClassAdapter;
import com.example.jobs.startproject.model.UserInfo;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class ShowClass extends AppCompatActivity {

    public static List<UserInfo> list;
    private static List<String> studyLists;
    private TextView course;
    public static List<UserInfo> outputList;
    private GridView gridView;
    //周次
    private static int studyTime = 5;
    private static int beginYear = 2016;
    private static int beginMonth = 8;
    private static int beginDay = 5;
    private static Gson gson = new Gson();
    private List<ClassKey_Value> key_values;

    public static NavigationView navigationView_class;
    private DrawerLayout drawerLayout_class;
    private Toolbar toolbar_class;

    public static int getStudyTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(beginYear, beginMonth, beginDay);
        studyTime = (int) ((System.currentTimeMillis() + 8 * 3600 * 1000 - calendar.getTimeInMillis()) / (1000 * 24 * 60 * 60));
        return studyTime / 7 + 1;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showclass_layout);
        ChooseActivity.isClassSuccess = true;
        studyTime = getStudyTime();
        navigationView_class = (NavigationView) findViewById(R.id.navigation_class);
        drawerLayout_class = (DrawerLayout) findViewById(R.id.drawerlayout_class);
        toolbar_class = (Toolbar) findViewById(R.id.toolbar_class);
        toolbar_class.setTitle("StarterProject");
        toolbar_class.setNavigationIcon(R.drawable.black);
        setSupportActionBar(toolbar_class);
        toolbar_class.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout_class.openDrawer(Gravity.LEFT);
            }
        });

        navigationView_class.getMenu().getItem(1).setChecked(true);





        navigationView_class.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.wifi_item:
                        item.setCheckable(false);
                        if (ChooseActivity.isLoginSuccess) {
                            Intent i1 = new Intent(ShowClass.this, SomeInfo.class);
                            startActivity(i1);
                        } else {
                            Intent i1 = new Intent(ShowClass.this, LoginActivity.class);
                            startActivity(i1);
                        }
                        break;
                    case R.id.study_item:
                        break;
                    default:
                        break;
                }

                drawerLayout_class.closeDrawer(Gravity.LEFT);
                return true;
            }
        });
        gridView = (GridView) findViewById(R.id.gridview);
        if (!ChooseActivity.isHaveClass) {
            list = Study_Activity.list;
            // 进行数据保存
            Stogefile(gson.toJson(list));
            //
        }
        ChooseActivity.isHaveClass = true;
        course = (TextView) findViewById(R.id.course);
        showData();
    }

    public void showData() {
        outputList = new ArrayList<UserInfo>();
        //填入null
        for (int i = 0; i < 40; i++) {
            outputList.add(i, null);
        }
        for (int i = 0; i < list.size(); i++) {
            UserInfo userInfo = list.get(i);
            if (userInfo.getCourse_period().contains("一") && isStudyTime(userInfo.getStudy_time())) {
                outputList.set(Integer.valueOf(userInfo.getStudy_week()), userInfo);
            } else if (userInfo.getCourse_period().contains("三") && isStudyTime(userInfo.getStudy_time())) {
                outputList.set(Integer.valueOf(userInfo.getStudy_week()) + 8, userInfo);
            } else if (userInfo.getCourse_period().contains("五") && isStudyTime(userInfo.getStudy_time())) {
                outputList.set(Integer.valueOf(userInfo.getStudy_week()) + 16, userInfo);
            } else if (userInfo.getCourse_period().contains("七") && isStudyTime(userInfo.getStudy_time())) {
                outputList.set(Integer.valueOf(userInfo.getStudy_week()) + 24, userInfo);
            } else if (userInfo.getCourse_period().contains("九") && isStudyTime(userInfo.getStudy_time())) {
                outputList.set(Integer.valueOf(userInfo.getStudy_week()) + 32, userInfo);
            }
        }

        // ok

        MyAdapter adapter = new MyAdapter(ShowClass.this, R.layout.item_layout, outputList);
        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (outputList.get(position) != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ShowClass.this);
                    //builder.setMessage(outputList.get(position).toString());
                    View view2 = LayoutInflater.from(ShowClass.this).inflate(R.layout.show_one_class, null);
                    ListView oneListView = (ListView) view2.findViewById(R.id.oneListView);
                    key_values = null;
                    initKey_value(position);
                    OneClassAdapter adapter1 = new OneClassAdapter(ShowClass.this, R.layout.oneclass_item, key_values);
                    oneListView.setAdapter(adapter1);
                    builder.setView(view2);
                    builder.setCancelable(true);
                    builder.show();
                }
            }
        });

    }

    public List<ClassKey_Value> initKey_value(int value) {
        key_values = new ArrayList<ClassKey_Value>();
        String[] data = {"培养方案:", "课程号:", "课程名:", "课序号:", "学分:", "课程属性:", "考试类型:", "教师:",
                "修读方式:", "选课状态:", "周次:", "星期:", "节次:", "节数:", "校区:", "教学楼:", "教室:"};

        key_values.add(0, new ClassKey_Value(data[0], outputList.get(value).getMajor_plan()));
        key_values.add(1, new ClassKey_Value(data[1], outputList.get(value).getCourse_id()));
        key_values.add(2, new ClassKey_Value(data[2], outputList.get(value).getCourse_name()));
        key_values.add(3, new ClassKey_Value(data[3], outputList.get(value).getCourse_number()));
        key_values.add(4, new ClassKey_Value(data[4], outputList.get(value).getCourse_grades()));
        key_values.add(5, new ClassKey_Value(data[5], outputList.get(value).getCourse_attribute()));
        key_values.add(6, new ClassKey_Value(data[6], outputList.get(value).getExam_type()));
        key_values.add(7, new ClassKey_Value(data[7], outputList.get(value).getTeach_name()));
        key_values.add(8, new ClassKey_Value(data[8], outputList.get(value).getStudy_type()));
        key_values.add(9, new ClassKey_Value(data[9], outputList.get(value).getCourse_status()));
        key_values.add(10, new ClassKey_Value(data[10], outputList.get(value).getStudy_time()));
        key_values.add(11, new ClassKey_Value(data[11], outputList.get(value).getStudy_week()));
        key_values.add(12, new ClassKey_Value(data[12], outputList.get(value).getCourse_period()));
        key_values.add(13, new ClassKey_Value(data[13], outputList.get(value).getCourse_times()));
        key_values.add(14, new ClassKey_Value(data[14], outputList.get(value).getStudy_place()));
        key_values.add(15, new ClassKey_Value(data[15], outputList.get(value).getCourse_building()));
        key_values.add(16, new ClassKey_Value(data[16], outputList.get(value).getCourse_classroom()));
        return key_values;
    }


    public void changeStudyTimeOnToolBar(int studyTime) {
        MenuItem item = toolbar_class.getMenu().findItem(R.id.study_time);
        item.setTitle("第 " + studyTime + " 周");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.base_toolbar_menu, menu);
        final MenuItem item = menu.findItem(R.id.study_time);
        item.setTitle("第 " + studyTime + " 周");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.sync:
                studyTime = getStudyTime();
                changeStudyTimeOnToolBar(studyTime);
                showData();
                break;
            case R.id.study_time:
            case R.id.change_stduty:
                //选择
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setTitle("选择周数");
                View view = LayoutInflater.from(this).inflate(R.layout.choose_time, null);
                builder1.setView(view);
                final TextView textView = (TextView) view.findViewById(R.id.text);
                SeekBar seekBar = (SeekBar) view.findViewById(R.id.seekbar);
                seekBar.setProgress(studyTime - 1);
                textView.setText("第 " + studyTime + " 周");
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        textView.setText("第 " + (progress + 1) + " 周");
                        studyTime = progress + 1;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }
                });

                builder1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //
                    }
                });

                builder1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeStudyTimeOnToolBar(studyTime);
                        showData();
                    }
                });
                //
                builder1.show();
                break;
            case R.id.change_acount:
                Intent i = new Intent(ShowClass.this, Study_Activity.class);
                ChooseActivity.isHaveClass = false;
                startActivity(i);
                finish();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean isStudyTime(String data) {
        if (data.contains(",")) {
            String before = data.split("-")[0].trim();
            String after1 = data.split("-")[1].split(",")[0].trim();
            String after2 = data.split("-")[1].split(",")[1].split("周")[0].trim();
            if ((Integer.valueOf(before) <= studyTime && Integer.valueOf(after1) >= studyTime) || Integer.valueOf(after2) == studyTime) {
                return true;
            } else {
                return false;
            }
        } else if (data.contains("-") && data.contains("上")) {
            String before = data.split("-")[0].trim();
            String after = data.split("-")[1].split("周")[0].trim();
            if (Integer.valueOf(before) <= studyTime && Integer.valueOf(after) >= studyTime) {
                return true;
            } else {
                return false;
            }
        } else {
            String time = data.split("周")[0].trim();
            if (Integer.valueOf(time) == studyTime) {
                return true;
            } else {
                return false;
            }
        }
    }

    public static void Stogefile(String data) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = (MyApplication.getContext()).openFileOutput("user.data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(data);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null && out != null) {
                    writer.close();
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
