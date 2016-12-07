package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.utility.Server;

/**
 * Created by Jeong on 2016-12-02.
 */
public class RemoconTvActivity extends Activity {
    String userId, userPw;
    ResponseIRDevice devInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remocon_tv);
        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userPw = intent.getStringExtra("userPw");
        devInfo = (ResponseIRDevice)intent.getSerializableExtra("devInfo");

    }
    public void btn_tv_onClicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_Power");
        Toast.makeText(RemoconTvActivity.this, "on", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_ch_upClicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_Chan_up");
        Toast.makeText(RemoconTvActivity.this, "chup", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_ch_downClicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_Chan_down");
        Toast.makeText(RemoconTvActivity.this, "chdo", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_vol_upClicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_Vol+");
        Toast.makeText(RemoconTvActivity.this, "volup", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_vol_downClicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_Vol-");
        Toast.makeText(RemoconTvActivity.this, "voldo", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_0Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_0");
        Toast.makeText(RemoconTvActivity.this, "tv0", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_1Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_1");
        Toast.makeText(RemoconTvActivity.this, "tv1", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_2Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_2");
        Toast.makeText(RemoconTvActivity.this, "tv2", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_3Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_3");
        Toast.makeText(RemoconTvActivity.this, "tv3", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_4Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_4");
        Toast.makeText(RemoconTvActivity.this, "tv4", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_5Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_5");
        Toast.makeText(RemoconTvActivity.this, "tv5", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_6Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_6");
        Toast.makeText(RemoconTvActivity.this, "tv6", Toast.LENGTH_SHORT).show();
    }

    public void btn_tv_7Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_7");
        Toast.makeText(RemoconTvActivity.this, "tv7", Toast.LENGTH_SHORT).show();

    }

    public void btn_tv_8Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_8");
        Toast.makeText(RemoconTvActivity.this, "tv8", Toast.LENGTH_SHORT).show();

    }

    public void btn_tv_9Clicked(View v){
        Server.remoteDevice(userId,userPw, devInfo.getName(),"TV_9");
        Toast.makeText(RemoconTvActivity.this, "tv9", Toast.LENGTH_SHORT).show();
    }

    public void btn_customInstructionClicked(View v){
        Intent intent = new Intent(RemoconTvActivity.this, CustomIRcodeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPw", userPw);
        intent.putExtra("deviceName", devInfo.getName());
        startActivity(intent);
    }
}
