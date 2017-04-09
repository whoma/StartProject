package com.example.jobs.startproject.model;

import android.widget.ArrayAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.jobs.startproject.R;

import java.util.List;

/**
 * Created by jobs on 2016/10/9.
 */

public class MyAdapter extends ArrayAdapter {
    private static String courseName = "一";
    private int reResourceId;
    private int width;
    private static Context mcontext;

    public MyAdapter(Context context, int textViewResourceId, List<UserInfo> objects) {
        super(context, textViewResourceId, objects);
        reResourceId = textViewResourceId;
        width = (int)context.getResources().getDisplayMetrics().xdpi / 8 - 2;
        mcontext = context;
    }
    public static int dp2px(int dp) {
        final float scale = mcontext.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }


    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHandler viewHandler;
        View view;
        if (convertView == null) {
            viewHandler = new ViewHandler();
            view = LayoutInflater.from(getContext()).inflate(reResourceId, null);
            viewHandler.course = (TextView) view.findViewById(R.id.course);
            view.setTag(viewHandler);
        } else {
            view = convertView;
            viewHandler = (ViewHandler) view.getTag();
        }



        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) viewHandler.course.getLayoutParams();
        params.width = dp2px(width);
        viewHandler.course.setLayoutParams(params);


        switch (position) {
            case 0:
                courseName = "一";
                viewHandler.course.setText("\n第\n" + courseName + "\n大\n节\n");
                break;
            case 8:
                courseName = "二";
                viewHandler.course.setText("\n第\n" + courseName + "\n大\n节\n");
                break;
            case 16:
                courseName = "三";
                viewHandler.course.setText("\n第\n" + courseName + "\n大\n节\n");
                break;
            case 24:
                courseName = "四";
                viewHandler.course.setText("\n第\n" + courseName + "\n大\n节\n");
                break;
            case 32:
                courseName = "五";
                viewHandler.course.setText("\n第\n" + courseName + "\n大\n节\n");
                break;
            default:
                UserInfo userInfo = (UserInfo) getItem(position);
                if (userInfo != null) {
                    viewHandler.course.setText(userInfo.getCourse_name() + "\n" + userInfo.getTeach_name() + "\n" + userInfo.getCourse_building() + "\n" + userInfo.getCourse_classroom());
                } else {
                    viewHandler.course.setText(" ");
                }
                break;
        }



        return view;
    }

    class ViewHandler {
        TextView course;
    }


}