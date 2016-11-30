package com.example.jeong.hanjo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jeong.hanjo.data.ResponseUserInfo;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-11-14.
 */
public class FamilyListViewAdapter extends BaseAdapter{
    private ArrayList<ResponseUserInfo> list;

    public FamilyListViewAdapter(ArrayList<ResponseUserInfo> list){
        this.list = list;
    }

    public void addItem(ResponseUserInfo res){
        list.add(res);
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
            convertView = inflater.inflate(R.layout.familylist_item, parent, false);

            TextView textView_familyUser = (TextView) convertView.findViewById(R.id.textView_familyUser);

            ResponseUserInfo familyUser = list.get(position);

            textView_familyUser.setText(familyUser.getName());
            Log.i("Adapter",familyUser.getId());
        }


        return convertView;
    }
}
