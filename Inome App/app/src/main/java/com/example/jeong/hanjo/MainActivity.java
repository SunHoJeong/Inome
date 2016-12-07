package com.example.jeong.hanjo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jeong.hanjo.utility.HttpHandler;
import com.example.jeong.hanjo.utility.Server;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    /* true일 경우 레코 비콘만 스캔하며, false일 경우 모든 비콘을 스캔합니다.
    RECOBeaconManager 객체 생성 시 사용합니다.*/
    public static final boolean SCAN_RECO_ONLY = true;
    public static final String RECO_UUID = "24DDF411-8CF1-440C-87CD-E368DAF9C93E";

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
//    CheckBox autoLogin;
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

//    private BluetoothService btService = null;

//    private final Handler mHandler = new Handler() {
//
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//        }
//
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputId = (EditText)findViewById(R.id.editText_id);
        inputPw = (EditText)findViewById(R.id.editText_pw);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "--token:"+token);


    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void logInClicked(View v){// clicked login button

        String id = inputId.getText().toString();
        String pw = inputPw.getText().toString();
        String res = null;


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


//    private boolean logInCheck(String id, String pw, int logInMode){
//        String mode = null;
//        if(logInMode == MODE_USER){
//            mode = "user";
//        }
//        else{
//            mode = "family";
//        }
//
//        if(pref.getString(mode+"Id","").equals(id) && pref.getString(mode+"Pw","").equals(pw)) {
//            // login success
//
//            Toast.makeText(MainActivity.this, mode+"로그인 성공", Toast.LENGTH_LONG).show();
//            return true;
//        } else if (pref.getString(mode+"Id","").equals(null)){
//            // sign in first
//            Toast.makeText(MainActivity.this, "먼저 등록을 해주세요", Toast.LENGTH_LONG).show();
//            return false;
//        } else {
//            // login failed
//            Toast.makeText(MainActivity.this, mode+"로그인 실패"+id+"/"+pref.getString(mode+"Id","")+"///"+pw+"/"+pref.getString(mode+"Pw",""), Toast.LENGTH_LONG).show();
//            return false;
//        }
//    }


//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        switch (requestCode) {
//            case REQUEST_CONNECT_DEVICE:
//                // When DeviceListActivity returns with a device to connect
//                if (resultCode == Activity.RESULT_OK) {
//                    btService.getDeviceInfo(data);
//                }
//                break;
//
//            case REQUEST_ENABLE_BT:
//                // When the request to enable Bluetooth returns
//                if (resultCode == Activity.RESULT_OK) {
//                    // 확인 눌렀을 때
//                    //Next Step
//                    btService.scanDevice();
//                } else {
//                    // 취소 눌렀을 때
//                    Log.d(TAG, "Bluetooth is not enabled");
//                }
//                break;
//        }
//    }
//
//    public void btn_bluetoothClicked(View v){
//        if(btService.getDeviceState()) {
//            // 블루투스가 지원 가능한 기기일 때
//            btService.enableBluetooth();
//        } else {
//            finish();
//        }
//
//    }

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

    public void addClicked(View v){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String uri = Server.uriMaker(Server.METHOD_addIRcode, "hanjo", "hanjo");
        //Log.d("--Main--", result);

        try {
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

}
