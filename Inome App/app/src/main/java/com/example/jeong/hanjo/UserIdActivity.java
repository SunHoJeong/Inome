package com.example.jeong.hanjo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.jeong.hanjo.beaconService.RecoBackgroundMonitoringService;
import com.example.jeong.hanjo.beaconService.RecoBackgroundRangingService;

/**
 * Created by Jeong on 2016-11-18.
 */
public class UserIdActivity extends Activity {
    String userId, userPw;
    TextView beacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        //beacon = (TextView)findViewById(R.id.beacon);

        Intent intent = getIntent();
        userId = intent.getStringExtra("id");
        userPw = intent.getStringExtra("pw");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(this.isBackgroundMonitoringServiceRunning(this)) {
            ToggleButton toggle = (ToggleButton)findViewById(R.id.backgroundMonitoringToggleButton);
            toggle.setChecked(true);
        }

        if(this.isBackgroundRangingServiceRunning(this)) {
            ToggleButton toggle = (ToggleButton)findViewById(R.id.backgroundRangingToggleButton);
            toggle.setChecked(true);
        }
    }

    public void remoteIRDeviceClicked(View v){
        //request IRDevice list
        Intent intent = new Intent(UserIdActivity.this, RemoteDeviceActivity.class);
        intent.putExtra("userId", userId);
        intent.putExtra("userPw", userPw);
        startActivity(intent);

    }

    public void logoutClicked(View v){
        Toast.makeText(UserIdActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void onMonitoringToggleButtonClicked(View v) {
        ToggleButton toggle = (ToggleButton)v;
        if(toggle.isChecked()) {
            Log.i("MainActivity", "onMonitoringToggleButtonClicked off to on");
            Intent intent = new Intent(this, RecoBackgroundMonitoringService.class);
            startService(intent);
        } else {
            Log.i("MainActivity", "onMonitoringToggleButtonClicked on to off");
            stopService(new Intent(this, RecoBackgroundMonitoringService.class));
        }
    }

    public void onRangingToggleButtonClicked(View v) {
        ToggleButton toggle = (ToggleButton)v;
        if(toggle.isChecked()) {
            Log.i("MainActivity", "onRangingToggleButtonClicked off to on");
            Intent intent = new Intent(this, RecoBackgroundRangingService.class);
            intent.putExtra("userId",userId);
            intent.putExtra("userPw",userPw);
            startService(intent);
        } else {
            Log.i("MainActivity", "onRangingToggleButtonClicked on to off");
            stopService(new Intent(this, RecoBackgroundRangingService.class));
        }
    }

    private boolean isBackgroundMonitoringServiceRunning(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo runningService : am.getRunningServices(Integer.MAX_VALUE)) {
            if(RecoBackgroundMonitoringService.class.getName().equals(runningService.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private boolean isBackgroundRangingServiceRunning(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo runningService : am.getRunningServices(Integer.MAX_VALUE)) {
            if(RecoBackgroundRangingService.class.getName().equals(runningService.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
