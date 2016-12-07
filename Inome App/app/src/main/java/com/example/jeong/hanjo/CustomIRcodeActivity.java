package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jeong.hanjo.data.CustomInstruction;
import com.example.jeong.hanjo.utility.Server;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-12-07.
 */
public class CustomIRcodeActivity extends Activity{
    ArrayList<CustomInstruction> cusIRList;
    CustomIRcodeListAdapter adapter;
    ListView listView;
    String userId, userPw;
    String deviceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ircode_list);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userPw = intent.getStringExtra("userPw");
        deviceName = intent.getStringExtra("deviceName");
    }

    @Override
    protected void onResume() {
        super.onResume();

        cusIRList = Server.showCustomInstruction(userId, userPw, deviceName);
        adapter = new CustomIRcodeListAdapter(cusIRList);
        listView = (ListView)findViewById(R.id.listView_custom_IRcode);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                CustomInstruction IRcodeInfo = cusIRList.get(position);
                Server.remoteDevice(userId, userPw, deviceName, IRcodeInfo.getUserInstruction());
            }
        });
    }
}
