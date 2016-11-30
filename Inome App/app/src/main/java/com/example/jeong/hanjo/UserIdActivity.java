package com.example.jeong.hanjo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.jeong.hanjo.beaconService.RecoBackgroundMonitoringService;
import com.example.jeong.hanjo.beaconService.RecoBackgroundRangingService;
import com.example.jeong.hanjo.utility.HttpHandler;

import java.util.concurrent.ExecutionException;

/**
 * Created by Jeong on 2016-11-18.
 */
public class UserIdActivity extends Activity {
    String familyId, familyPw;
    TextView beacon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        beacon = (TextView)findViewById(R.id.beacon);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("id");
        familyPw = intent.getStringExtra("pw");

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
        intent.putExtra("familyId", familyId);
        intent.putExtra("familyPw", familyPw);
        startActivity(intent);

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
            startService(intent);
        } else {
            Log.i("MainActivity", "onRangingToggleButtonClicked on to off");
            stopService(new Intent(this, RecoBackgroundRangingService.class));
        }
    }

    public void logoutClicked(View v){

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

    public void testClicked2(View v){
        HttpHandler hp = new HttpHandler();
        String res = null;
        try {
            res = hp.execute("http://192.168.137.28:8080/SWCD-war/webresources/IRservice/remoteControlByUser?userId="+"user1"+"&userPw=user1&deviceName=tv1&userInstruction=up","GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Log.i("USERidAc", res);
    }
}
