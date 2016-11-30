package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.utility.Server;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-11-17.
 */
public class IRdeviceListActivity extends Activity {
    ListView listView;
    IRdeviceListViewAdapter adapter;
    String familyId, familyPw;
    ArrayList<ResponseIRDevice> IRdevList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irdecive_list);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("--IRdeviceList--", "onResume");

        IRdevList = Server.showIRdeviceInfo(familyId, familyPw);
        adapter = new IRdeviceListViewAdapter(IRdevList);
        listView = (ListView)findViewById(R.id.listView_IRdevice);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //GET family id,pw , user name
                // get item
                ResponseIRDevice item = (ResponseIRDevice) parent.getItemAtPosition(position);

                Intent intent = new Intent(IRdeviceListActivity.this, DetailIRdeviceActivity.class);
                intent.putExtra("familyId", familyId);
                intent.putExtra("familyPw", familyPw);
                intent.putExtra("ResponseIRDevice", item);

                startActivity(intent);
            }
        });
    }

    public void addIRdeviceClicked(View v){
        Intent intent = new Intent(IRdeviceListActivity.this, IRDeviceInfo.class);
        intent.putExtra("familyId",familyId);
        intent.putExtra("familyPw",familyPw);
        intent.putExtra("mode",MainActivity.MODE_ADD);
        startActivity(intent);
        Toast.makeText(IRdeviceListActivity.this, "IR디바이스 추가", Toast.LENGTH_SHORT).show();
    }

}
