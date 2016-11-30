package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jeong on 2016-11-13.
 */
public class FamilyIdActivity extends Activity {
    TextView textView_familyInfo;
    String familyId;
    String familyPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_family);

        Intent intent = new Intent(this.getIntent());
        familyId = intent.getStringExtra("familyId"); //family id
        familyPw = intent.getStringExtra("familyPw"); //family pw

        textView_familyInfo = (TextView) findViewById(R.id.textView_familyInfo);
        textView_familyInfo.setText(familyId);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void showFamilyMemClicked(View v){
        Toast.makeText(FamilyIdActivity.this,"구성원목록",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FamilyIdActivity.this, FamilyListActivity.class);
        intent.putExtra("familyId", familyId);
        intent.putExtra("familyPw", familyPw);
        startActivity(intent);
    }

    public void showIRDeviceClicked(View v){
        Toast.makeText(FamilyIdActivity.this,"IR디바이스목록",Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(FamilyIdActivity.this, IRdeviceListActivity.class);
        intent.putExtra("familyId", familyId);
        intent.putExtra("familyPw", familyPw);
        startActivity(intent);
    }

    public void showLogClicked(View v){
        //TODO: 도어락 로그 구현
        Toast.makeText(FamilyIdActivity.this,"도어락로그",Toast.LENGTH_SHORT).show();
    }

    public void logoutClicked(View v){
        Toast.makeText(FamilyIdActivity.this,"로그아웃",Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
