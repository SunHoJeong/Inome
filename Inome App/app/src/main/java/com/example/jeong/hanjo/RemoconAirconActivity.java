package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.utility.Server;

/**
 * Created by Jeong on 2016-12-04.
 */
public class RemoconAirconActivity extends Activity{
    String userId, userPw;
    ResponseIRDevice devInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remocon_aircon);

        Intent intent = getIntent();
        userId = intent.getStringExtra("userId");
        userPw = intent.getStringExtra("userPw");
        devInfo = (ResponseIRDevice)intent.getSerializableExtra("devInfo");

    }
    public void btn_aircon_onClicked(View v){
        Server.remoteDevice(userId, userPw, devInfo.getName(), "Aircondition_Power_On");
        Toast.makeText(RemoconAirconActivity.this, "Aircondition_Power_On", Toast.LENGTH_SHORT).show();
    }

    public void btn_aircon_offClicked(View v){
        Server.remoteDevice(userId, userPw, devInfo.getName(), "Aircondition_Power_Off");
        Toast.makeText(RemoconAirconActivity.this, "Aircondition_Power_Off", Toast.LENGTH_SHORT).show();
    }

    public void btn_aircon_wind_upClicked(View v){
        Toast.makeText(RemoconAirconActivity.this, "windup", Toast.LENGTH_SHORT).show();
    }

    public void btn_aircon_wind_downClicked(View v){
        Toast.makeText(RemoconAirconActivity.this, "winddo", Toast.LENGTH_SHORT).show();
    }

    public void btn_aircon_temp_upClicked(View v){
        Toast.makeText(RemoconAirconActivity.this, "tempup", Toast.LENGTH_SHORT).show();
    }

    public void btn_aircon_temp_downClicked(View v){
        Toast.makeText(RemoconAirconActivity.this, "tempdown", Toast.LENGTH_SHORT).show();
    }

    public void btn_customInstructionClicked(View v){
        Intent intent = new Intent(RemoconAirconActivity.this, CustomIRcodeActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPw", userPw);
        intent.putExtra("deviceName", devInfo.getName());
        startActivity(intent);
    }

}
