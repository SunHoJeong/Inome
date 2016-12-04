package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.example.jeong.hanjo.data.DoorlockLog;
import com.example.jeong.hanjo.utility.Server;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-12-02.
 */
public class LogActivity extends Activity {
    LogListViewAdapter adapter;
    ListView listView;
    ArrayList<DoorlockLog> logList;
    String familyId, familyPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doorlock_log);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");

        logList = Server.showLogInfo(familyId, familyPw);

        adapter = new LogListViewAdapter(logList);
        listView = (ListView)findViewById(R.id.listView_doorlock_log);
        listView.setAdapter(adapter);

    }
}
