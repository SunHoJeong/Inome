package com.example.jeong.hanjo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeong.hanjo.data.DoorlockLog;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-12-02.
 */
public class LogListViewAdapter extends BaseAdapter {
    private ArrayList<DoorlockLog> list;

    public LogListViewAdapter(ArrayList<DoorlockLog> list){
        this.list = list;
    }

    public void addItem(DoorlockLog log){
        list.add(log);
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.log_item, parent, false);

            TextView textView_log_id = (TextView) convertView.findViewById(R.id.textView_log_id);
            TextView textView_log_date = (TextView) convertView.findViewById(R.id.textView_log_date);
            TextView textView_log_time = (TextView) convertView.findViewById(R.id.textView_log_time);
            DoorlockLog log = list.get(position);

            textView_log_id.setText(log.getId());
            textView_log_date.setText(log.getDate());
            textView_log_time.setText(log.getTime());
        }

        return convertView;
    }
}
