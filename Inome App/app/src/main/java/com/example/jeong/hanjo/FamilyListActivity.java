package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseUserInfo;
import com.example.jeong.hanjo.utility.Server;

import java.util.ArrayList;

/**
 * Created by Jeong on 2016-11-14.
 */
public class FamilyListActivity extends Activity {
    TextView textView_familyInfo;
    ListView listView;
    FamilyListViewAdapter adapter;

    String familyId;
    String familyPw;
    ArrayList<ResponseUserInfo> Userlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family_list);
        textView_familyInfo = (TextView)findViewById(R.id.textView_familyInfo);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
        textView_familyInfo.setText("가족 구성원 목록   가족ID:"+familyId);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("--FamilyListActivity--","onResume");

        Userlist = Server.showUserInfo(familyId, familyPw);
        adapter = new FamilyListViewAdapter(Userlist);
        listView = (ListView)findViewById(R.id.listView_family);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                //GET family id,pw , user name
                // get item
                ResponseUserInfo item = (ResponseUserInfo) parent.getItemAtPosition(position);

                Intent intent = new Intent(FamilyListActivity.this, DetailUserActivity.class);
                intent.putExtra("familyId", familyId);
                intent.putExtra("familyPw", familyPw);
                intent.putExtra("userInfo", item);
                startActivity(intent);
          }
        });
    }

    public void addUserClicked(View v){
        //add user event
        Intent intent = new Intent(FamilyListActivity.this, UserInfo.class);
        intent.putExtra("familyId",familyId);
        intent.putExtra("familyPw",familyPw);
        intent.putExtra("mode",MainActivity.MODE_ADD);
        startActivity(intent);
        Toast.makeText(FamilyListActivity.this, "구성원추가", Toast.LENGTH_SHORT).show();
    }
}
