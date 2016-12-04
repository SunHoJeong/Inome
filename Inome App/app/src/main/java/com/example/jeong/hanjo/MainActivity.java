package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeong.hanjo.utility.BluetoothService;
import com.example.jeong.hanjo.utility.Server;

public class MainActivity extends AppCompatActivity {
    /* true일 경우 레코 비콘만 스캔하며, false일 경우 모든 비콘을 스캔합니다.
    RECOBeaconManager 객체 생성 시 사용합니다.*/
    public static final boolean SCAN_RECO_ONLY = true;
    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";
//    public static final String PROPERTY_REG_ID = "registration_id";
//    public static final String PROPERTY_APP_VERSION = "appVersion";
//    static String SENDER_ID = "1033891114261";


    /* 백그라운드 ranging timeout을 설정합니다.
       true일 경우, 백그라운드에서 입장한 region에서 ranging이 실행 되었을 때, 10초 후 자동으로 정지합니다.
       false일 경우, 계속 ranging을 실행합니다. (배터리 소모율에 영향을 끼칩니다.)
       RECOBeaconManager 객체 생성 시 사용합니다.*/
    public static final boolean ENABLE_BACKGROUND_RANGING_TIMEOUT = true;

    // Debugging
    private static final String TAG = MainActivity.class.getSimpleName();
    static final int MODE_ADD = 1;
    static final int MODE_REVISE = 2;
    private int MODE_USER = 1;
    private int MODE_FAMILY = 2;
    EditText inputId, inputPw;
    CheckBox autoLogin;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    private BluetoothService btService = null;

    private final Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }

    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputId = (EditText)findViewById(R.id.editText_id);
        inputPw = (EditText)findViewById(R.id.editText_pw);
        //autoLogin = (CheckBox)findViewById(R.id.checkBox_login);



        pref = getSharedPreferences("loginInfo",Activity.MODE_PRIVATE);
        editor = pref.edit();
        editor.putString("userId", "user1");
        editor.putString("userPw", "user1");
        editor.putString("familyId", "_id");
        editor.putString("familyPw","_pw");
        editor.commit();

        if(pref.getBoolean("autoLogin", false)){
            inputId.setText(pref.getString("id", ""));
            inputPw.setText(pref.getString("pw", ""));
            autoLogin.setChecked(true);
        }

//        if(btService == null) {
//            btService = new BluetoothService(this, mHandler);
//        }

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

//        autoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if(isChecked){
//                    String id = inputId.getText().toString();
//                    String pw = inputPw.getText().toString();
//
//                    editor.putString("id", id);
//                    editor.putString("pw", pw);
//                    editor.putBoolean("autoLogin", true);
//                    editor.commit();
//                }else{
//			        editor.remove("id");
//			        editor.remove("pw");
//			        editor.remove("autoLogin");
//                    //editor.clear();
//                    editor.commit();
//                }
//            }
//        });
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

//    public void signUpClicked(View v){
//        //signUp button clicked
//        String temp1 = inputId.getText().toString();
//        String temp2 = inputPw.getText().toString();
//
//        editor.putString("id", temp1);
//        editor.putString("pw", temp2);
//        editor.commit();
//    }

    public void logInClicked(View v){// clicked login button

        String id = inputId.getText().toString();
        String pw = inputPw.getText().toString();
        String res = null;
        int mode = 0;

        res = Server.login(id, pw);
        if(res.equals("\"family\"")){
            Toast.makeText(MainActivity.this, res+"모드입니다", Toast.LENGTH_SHORT).show();
            Log.i(TAG, "가족 로그인 성공");
            Intent intent = new Intent(MainActivity.this, FamilyIdActivity.class);
            intent.putExtra("familyId", id);
            intent.putExtra("familyPw", pw);
            startActivity(intent);
        }
        else if(res.equals("\"user\"")){
            Log.i(TAG,"유저 로그인 성공");
            res = Server.login(id, pw);
            Toast.makeText(MainActivity.this, res+"모드입니다", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, UserIdActivity.class);
            intent.putExtra("id", id);
            intent.putExtra("pw", pw);

            startActivity(intent);
        }
        else{
            Log.i(TAG,"로그인 실패");
            Toast.makeText(MainActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
        }

    }


    private boolean logInCheck(String id, String pw, int logInMode){
        String mode = null;
        if(logInMode == MODE_USER){
            mode = "user";
        }
        else{
            mode = "family";
        }

        if(pref.getString(mode+"Id","").equals(id) && pref.getString(mode+"Pw","").equals(pw)) {
            // login success

            Toast.makeText(MainActivity.this, mode+"로그인 성공", Toast.LENGTH_LONG).show();
            return true;
        } else if (pref.getString(mode+"Id","").equals(null)){
            // sign in first
            Toast.makeText(MainActivity.this, "먼저 등록을 해주세요", Toast.LENGTH_LONG).show();
            return false;
        } else {
            // login failed
            Toast.makeText(MainActivity.this, mode+"로그인 실패"+id+"/"+pref.getString(mode+"Id","")+"///"+pw+"/"+pref.getString(mode+"Pw",""), Toast.LENGTH_LONG).show();
            return false;
        }
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    btService.getDeviceInfo(data);
                }
                break;

            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // 확인 눌렀을 때
                    //Next Step
                    btService.scanDevice();
                } else {
                    // 취소 눌렀을 때
                    Log.d(TAG, "Bluetooth is not enabled");
                }
                break;
        }
    }

    public void btn_bluetoothClicked(View v){
        if(btService.getDeviceState()) {
            // 블루투스가 지원 가능한 기기일 때
            btService.enableBluetooth();
        } else {
            finish();
        }

    }

    public void testClicked(View v){
//        Intent intent = new Intent(MainActivity.this, UserIdActivity.class);
//        intent.putExtra("familyId","_id");
//        intent.putExtra("familyPw","_pw");
//        startActivity(intent);
        Server.remoteDevice("user1","user1","tv1","up");

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
