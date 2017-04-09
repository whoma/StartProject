package com.example.jobs.startproject.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jobs.startproject.R;

import java.util.List;

/**
 * Created by jobs on 2016/10/3.
 */

public class AcountAdapter extends ArrayAdapter implements View.OnClickListener{
    private CallBackOnclick callBackOnclick;
    private int reSource;
    public AcountAdapter(Context context, int textViewResourceId, List<UserData> objects, CallBackOnclick callBackOnclick) {
        super(context, textViewResourceId, objects);
        reSource = textViewResourceId;
        this.callBackOnclick = callBackOnclick;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserData userData = (UserData) getItem(position);
        ViewHolder viewHolder;
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(reSource, null);
            viewHolder = new ViewHolder();
            viewHolder.more_acount = (TextView) view.findViewById(R.id.more_acount);
            viewHolder.image_delete = (ImageView) view.findViewById(R.id.image_delete);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.more_acount.setText(userData.getAcount());
        viewHolder.more_acount.setOnClickListener(this);
        viewHolder.image_delete.setOnClickListener(this);
        viewHolder.more_acount.setTag(position);
        viewHolder.image_delete.setTag(position);
        return view;
    }

    class ViewHolder {
        TextView more_acount;
        ImageView image_delete;
    }


    @Override
    public void onClick(View v) {
        if (callBackOnclick != null) {
            callBackOnclick.onClick(v);
        }
    }
}
