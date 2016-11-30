package com.example.jeong.hanjo.utility;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * Created by Jeong on 2016-10-31.
 */
public class BluetoothService {
    // Debugging
    private static final String TAG = BluetoothService.class.getSimpleName();

    private int mState;

    // 상태를 나타내는 상태 변수
    private static final int STATE_NONE = 0; // we're doing nothing
    private static final int STATE_LISTEN = 1; // now listening for incoming connections
    private static final int STATE_CONNECTING = 2; // now initiating an outgoing connection
    private static final int STATE_CONNECTED = 3; // now connected to a remote device

    // Intent request code
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;

    // RFCOMM Protocol
    private static final UUID MY_UUID = UUID.fromString("00001107-0000-1000-8000-00805F9B34FB");
            //UUID.fromString("00001C00-D102-11E1-9B23-00025B00A5A5");
    //private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
            //UUID.fromString("00001C00-D102-11E1-9B23-00025B00A5A5");
            //UUID.fromString("C640A16C-833F-46DD-BAA8-F64C186B0261");

    private BluetoothAdapter btAdapter;
    private ConnectThread mConnectThread;
    private ConnectedThread mConnectedThread;

    private Activity mActivity;
    private Handler mHandler;

    // Constructors
    public BluetoothService(Activity ac, Handler h) {
        mActivity = ac;
        mHandler = h;

        // BluetoothAdapter 얻기
        btAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public boolean getDeviceState() {//지원여부확인
        Log.d(TAG, "--블루투스 지원여부 확인 테스트--");

        if(btAdapter == null) {
            Log.d(TAG, "X블루투스 지원X");

            return false;

        } else {
            Log.d(TAG, "O블루투스 지원O");

            return true;
        }
    }

    public void enableBluetooth() {
        Log.i(TAG, "Check the enabled Bluetooth");

        if(btAdapter.isEnabled()) {
            // 기기의 블루투스 상태가 On인 경우
            Log.d(TAG, "블루투스 On 상태");

            scanDevice();
        } else {
            // 기기의 블루투스 상태가 Off인 경우
            Log.d(TAG, "블루투스 Off 상태 -> Bluetooth Enable Request 요청");

            Intent i = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            mActivity.startActivityForResult(i, REQUEST_ENABLE_BT);
        }
    }

    public void scanDevice() {
        Log.d(TAG, "--디바이스 스캔--");

        Intent serverIntent = new Intent(mActivity, DeviceListActivity.class);
        mActivity.startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void getDeviceInfo(Intent data) {
        // Get the device MAC address
        String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
        // Get the BluetoothDevice object
        BluetoothDevice device = btAdapter.getRemoteDevice(address);

        Log.d(TAG, "장치정보 \n" + "address : " + address);

        connect(device);
    }

    // Bluetooth 상태 set
    private synchronized void setState(int state) {
        Log.d(TAG, "setState() " + mState + " -> " + state);
        mState = state;
    }

    // Bluetooth 상태 get
    public synchronized int getState() {
        return mState;
    }

    public synchronized void start() {
        Log.d(TAG, "start, connect와 connected thread cancel");

        // Cancel any thread attempting to make a connection
        if (mConnectThread == null) {

        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }
    }

    // ConnectThread 초기화 device의 모든 연결 제거
    public synchronized void connect(BluetoothDevice device) {
        Log.d(TAG, "----connect to: " + device);

        // Cancel any thread attempting to make a connection
        if (mState == STATE_CONNECTING) {
            if (mConnectThread == null) {

            } else {
                mConnectThread.cancel();
                mConnectThread = null;
            }
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to connect with the given device
        mConnectThread = new ConnectThread(device);
        mConnectThread.start();
        setState(STATE_CONNECTING);
    }

    // ConnectedThread 초기화
    public synchronized void connected(BluetoothSocket socket,
                                       BluetoothDevice device) {
        Log.d(TAG, "connected");

        // Cancel the thread that completed the connection
        if (mConnectThread == null) {

        } else {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        // Cancel any thread currently running a connection
        if (mConnectedThread == null) {

        } else {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        // Start the thread to manage the connection and perform transmissions
        mConnectedThread = new ConnectedThread(socket);
        mConnectedThread.start();

        setState(STATE_CONNECTED);
    }

    // 모든 thread stop
    public synchronized void stop() {
        Log.d(TAG, "stop");

        if (mConnectThread != null) {
            mConnectThread.cancel();
            mConnectThread = null;
        }

        if (mConnectedThread != null) {
            mConnectedThread.cancel();
            mConnectedThread = null;
        }

        setState(STATE_NONE);
    }

    // 값을 쓰는 부분(보내는 부분)
    public void write(byte[] out) { // Create temporary object
        ConnectedThread r; // Synchronize a copy of the ConnectedThread
        synchronized (this) {
            if (mState != STATE_CONNECTED)
                return;
            r = mConnectedThread;
        } // Perform the write unsynchronized r.write(out); }
    }

    // 연결 실패했을때
    private void connectionFailed() {
        setState(STATE_LISTEN);
    }

    // 연결을 잃었을 때
    private void connectionLost() {
        setState(STATE_LISTEN);

    }

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectThread(BluetoothDevice device) {
            mmDevice = device;
            BluetoothSocket tmp = null;

            // 디바이스 정보를 얻어서 BluetoothSocket 생성
//            try {
//                tmp = device.createRfcommSocketToServiceRecord(MY_UUID);
//
//                Log.e(TAG, "---temp socket생성!: "+MY_UUID.toString()+" deviceAddress:"+mmDevice.getAddress());
//            } catch (IOException e) {
//                Log.e(TAG, "create() failed", e);
//            }
            try {
                // call hidden method, see BluetoothDevice source code for more details:
                // https://android.googlesource.com/platform/frameworks/base/+/master/core/java/android/bluetooth/BluetoothDevice.java
                Method m = mmDevice.getClass().getMethod("createRfcommSocket", int.class);
                tmp = (BluetoothSocket) m.invoke(device, 1);
                Log.i(TAG, "setting socket to result of createRfcommSocket");
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }
            mmSocket = tmp;

//            try {
//                tmp = (BluetoothSocket) mmDevice.getClass().getMethod("createRfcommSocket", new Class[] {int.class}).invoke(mmDevice,1);
//                Log.d(TAG, " socket생성!!");
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            } catch (InvocationTargetException e) {
//                e.printStackTrace();
//            } catch (NoSuchMethodException e) {
//                e.printStackTrace();
//            }
            //mmSocket = tmp;

        }

        public void run() {
            Log.i(TAG, "----BEGIN mConnectThread----");
            setName("ConnectThread");

            // 연결을 시도하기 전에는 항상 기기 검색을 중지한다.
            // 기기 검색이 계속되면 연결속도가 느려지기 때문이다.
            btAdapter.cancelDiscovery();

            // BluetoothSocket 연결 시도
            try {
                // BluetoothSocket 연결 시도에 대한 return 값은 succes 또는 exception이다.

//                Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
//                if(pairedDevices ==null){
//                    Log.d(TAG, "NULL!!!");
//                }
//                else{
//                    Log.d(TAG, "-----"+pairedDevices.size());
//                    for(BluetoothDevice device : pairedDevices) {
//                        // 디바이스 이름과 MAC 주소
//                        Log.d(TAG,"이름:"+device.getName()+",주소:"+device.getAddress());
//                    }
//
//
//                }


                Log.d(TAG, "----mmSocket.connect()전줄"+mmDevice.getType());
                mmSocket.connect();
                Log.d(TAG, "----Connect Success----");
                //Toast.makeText(mActivity, "연결성공", Toast.LENGTH_SHORT).show();

            } catch (IOException e) {
                connectionFailed();		// 연결 실패시 불러오는 메소드
                Log.d(TAG, "----Connect Fail----"+e.getMessage().toString());
                //Toast.makeText(mActivity, "연결실패", Toast.LENGTH_SHORT).show();

                // socket을 닫는다.
                try {
                    mmSocket.close();
                } catch (IOException e2) {
                    Log.e(TAG,
                            "unable to close() socket during connection failure",
                            e2);
                }
                // 연결중? 혹은 연결 대기상태인 메소드를 호출한다.
                BluetoothService.this.start();
                return;
            }

            // ConnectThread 클래스를 reset한다.
            synchronized (BluetoothService.this) {
                mConnectThread = null;
            }

            // ConnectThread를 시작한다.
            connected(mmSocket, mmDevice);
            Log.e(TAG, "---connected실행");
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            Log.d(TAG, "---- ConnectedThread----");
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // BluetoothSocket의 inputstream 과 outputstream을 얻는다.
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "temp sockets not created", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.i(TAG, "----BEGIN mConnectedThread----");
            byte[] receivedBuf = new byte[1024];
            byte[] sendBuf = "FF 55 0D 00 43 00 00 00 00 01 41 00 00 01 00 00 6D".getBytes();
            int bytes;

            write(sendBuf);
            Log.d(TAG,"--보낸값--"+ new String(sendBuf,0,sendBuf.length));
            // Keep listening to the InputStream while connected
            while (true) {
                try {
                    // InputStream으로부터 값을 받는 읽는 부분(값을 받는다)
                    bytes = mmInStream.read(receivedBuf);
                    Log.d(TAG,"--받은값--"+ new String(receivedBuf,0,receivedBuf.length));
                    //Log.d(TAG,"--받은값2--"+ new String(receivedBuf,"UTF-8"));


                } catch (IOException e) {
                    Log.e(TAG, "disconnected", e);
                    connectionLost();
                    break;
                }
            }
        }

        /**
         * Write to the connected OutStream.
         * @param buffer  The bytes to write
         */
        public void write(byte[] buffer) {
            try {
                // 값을 쓰는 부분(값을 보낸다)
                mmOutStream.write(buffer);

            } catch (IOException e) {
                Log.e(TAG, "Exception during write", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "close() of connect socket failed", e);
            }
        }
    }


}
