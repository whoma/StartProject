package com.example.jobs.startproject.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.jobs.startproject.R;
import com.example.jobs.startproject.activity.ClassKey_Value;

import java.util.List;
import java.util.Map;

/**
 * Created by jobs on 2016/10/16.
 */

public class OneClassAdapter extends ArrayAdapter {
    int realSource;
    Context mcontext;
    public OneClassAdapter(Context context, int textViewResourceId, List<ClassKey_Value> objects) {
        super(context, textViewResourceId, objects);
        realSource = textViewResourceId;
        mcontext = context;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassKey_Value key_value = (ClassKey_Value) getItem(position);
        View view;
        ViewHander viewHander;
        if (convertView != null) {
            view = convertView;
            viewHander = (ViewHander) view.getTag();
        } else {
            view = LayoutInflater.from(mcontext).inflate(realSource, null);
            viewHander = new ViewHander();
            viewHander.one_item = (TextView) view.findViewById(R.id.one_item);
            viewHander.two_item = (TextView) view.findViewById(R.id.two_item);
            view.setTag(viewHander);
        }
        viewHander.one_item.setText(key_value.getKey());
        viewHander.two_item.setText(key_value.getValue());

        return view;
    }

    class ViewHander {
        TextView one_item;
        TextView two_item;
    }
}
