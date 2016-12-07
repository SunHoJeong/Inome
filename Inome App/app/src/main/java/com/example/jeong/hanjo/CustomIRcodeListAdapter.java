package com.example.jeong.hanjo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeong.hanjo.data.CustomInstruction;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-12-07.
 */
public class CustomIRcodeListAdapter extends BaseAdapter{
    ArrayList<CustomInstruction> IRcodeInfo;

    public CustomIRcodeListAdapter(ArrayList<CustomInstruction> list){
        this.IRcodeInfo = list;
    }

    @Override
    public int getCount() {
        return IRcodeInfo.size();
    }

    @Override
    public Object getItem(int position) {
        return IRcodeInfo.get(position);
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

            TextView textView_customIRcode = (TextView) convertView.findViewById(R.id.textView_IRdevice);
            CustomInstruction cusIRcode = IRcodeInfo.get(position);

            textView_customIRcode.setText(cusIRcode.getUserInstruction());
        }

        return convertView;
    }
}
