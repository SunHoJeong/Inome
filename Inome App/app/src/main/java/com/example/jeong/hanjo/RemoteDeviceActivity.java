package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.utility.Server;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-11-28.
 */
public class RemoteDeviceActivity extends Activity {
    ListView listView;
    String familyId, familyPw;
    IRdeviceListViewAdapter adapter;
    ArrayList<ResponseIRDevice> IRdevList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remote_device);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
    }

    @Override
    protected void onResume() {
        super.onResume();

        IRdevList = Server.showIRdeviceInfo(familyId, familyPw);
        adapter = new IRdeviceListViewAdapter(IRdevList);
        listView = (ListView)findViewById(R.id.listView_remote_IRdevice);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                ResponseIRDevice item = (ResponseIRDevice) parent.getItemAtPosition(position);
                Toast.makeText(RemoteDeviceActivity.this, item.getName()+"/"+item.getCompany()+"/"+item.getClassification(), Toast.LENGTH_SHORT).show();
                //TODO:분류에 따라 실제 리모컨 UI로 이동
                //해당 아이템의 객체를 얻음 -> classification으로!
                //객체에서 분류에 따른 UI를 열어줌

            }
        });
    }
}
