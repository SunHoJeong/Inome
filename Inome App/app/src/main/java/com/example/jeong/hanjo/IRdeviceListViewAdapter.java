package com.example.jeong.hanjo;

/**
 * Created by Jeong on 2016-11-17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeong.hanjo.data.ResponseIRDevice;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-11-14.
 */
public class IRdeviceListViewAdapter extends BaseAdapter {
    private ArrayList<ResponseIRDevice> list;

    public IRdeviceListViewAdapter(ArrayList<ResponseIRDevice> list){
        this.list = list;
    }

    public void addItem(ResponseIRDevice device){
        list.add(device);
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
            convertView = inflater.inflate(R.layout.irlist_item, parent, false);

            TextView textView_IRdevice = (TextView) convertView.findViewById(R.id.textView_IRdevice);
            ResponseIRDevice dev = list.get(position);

            textView_IRdevice.setText(dev.getName());
        }

        return convertView;
    }
}
