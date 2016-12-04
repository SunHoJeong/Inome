package com.example.jeong.hanjo.utility;

import android.net.Uri;
import android.util.Log;

import com.example.jeong.hanjo.data.DoorlockLog;
import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.data.ResponseUserInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jeong on 2016-11-17.
 */
public class Server {
    public static final int METHOD_login = 1;
    public static final int METHOD_showUserInfo = 2;
    public static final int METHOD_showIRdeviceInfo = 3;
    public static final int METHOD_addUserInfo = 4;
    public static final int METHOD_reviseUserInfo = 5;
    public static final int METHOD_addIRdeviceInfo = 6;
    public static final int METHOD_reviseIRdeviceInfo = 7;
    public static final int METHOD_showRemoteInfo = 8;
    public static final int METHOD_showLogInfo = 9;
    public static final int METHOD_remoteDevice = 10;
    //remoteIRdevice
    //userAc, showIRdevicelist
    //getComList
    //getClassList
    //getModelList

    public static String login(String id, String pw){
        String result= null;
        String uri = uriMaker(METHOD_login, id, pw);
        HttpHandler hp = new HttpHandler();

        try {
            result = hp.execute(uri, "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static ArrayList<ResponseUserInfo> showUserInfo(String familyId, String familyPw){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String uri = uriMaker(METHOD_showUserInfo, familyId, familyPw);
        try {
//            result ="["+ hp.execute("http://192.168.137.28:8080/SWCD-war/webresources/userservice/showAllUserByFamily?" +
//                    "familyId="+familyId+"&familyPw="+familyPw,"GET").get();
            //TODO: 확인해보기
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ResponseUserInfo[] array = gson.fromJson(result, ResponseUserInfo[].class);
        List<ResponseUserInfo> list = Arrays.asList(array);
        ArrayList<ResponseUserInfo> userList = new ArrayList<ResponseUserInfo>(list);

        for(int i=0; i<list.size(); i++) {
            Log.i("--Server.showUserInfo--", "count:" + i + "-" + list.get(i).getId());
        }

        return userList;
    }

    public static ArrayList<ResponseIRDevice> showIRdeviceInfo(String familyId, String familyPw){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String uri = uriMaker(METHOD_showIRdeviceInfo, familyId, familyPw);
        try {
            //result ="["+ hp.execute("http://192.168.137.28:8080/SWCD-war/webresources/IRservice/showAllDeviceByFamily?" +
            //        "familyId="+familyId+"&familyPw="+familyPw,"GET").get();
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ResponseIRDevice[] array = gson.fromJson(result, ResponseIRDevice[].class);
        List<ResponseIRDevice> list = Arrays.asList(array);
        ArrayList<ResponseIRDevice> ResponseIRDeviceList = new ArrayList<ResponseIRDevice>(list);

        for(int i=0; i<list.size(); i++) {
            //Log.i("--Server.showUserInfo--", "count:" + i + "-" + list.get(i).getId());
        }

        return ResponseIRDeviceList;
    }

    public static String addUserInfo(){
        String result = null;

        return result;
    }

    public static String reviseUserInfo(){
        String result = null;

        return result;
    }

    public static ArrayList<DoorlockLog> showLogInfo(String familyId, String familyPw){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String uri = uriMaker(METHOD_showLogInfo, familyId, familyPw);
        try {
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        DoorlockLog[] array = gson.fromJson(result, DoorlockLog[].class);
        List<DoorlockLog> list = Arrays.asList(array);
        ArrayList<DoorlockLog> logList = new ArrayList<DoorlockLog>(list);

        for(int i=0; i<list.size(); i++) {
            //Log.i("--Server.showUserInfo--", "count:" + i + "-" + list.get(i).getId());
        }

        return logList;
    }

    public static ArrayList<ResponseIRDevice> showRemoteInfo(String userId, String userPw){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String uri = uriMaker(METHOD_showRemoteInfo, userId, userPw);
        try {
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        ResponseIRDevice[] array = gson.fromJson(result, ResponseIRDevice[].class);
        List<ResponseIRDevice> list = Arrays.asList(array);
        ArrayList<ResponseIRDevice> ResponseIRDeviceList = new ArrayList<ResponseIRDevice>(list);

        for(int i=0; i<list.size(); i++) {
            //Log.i("--Server.showUserInfo--", "count:" + i + "-" + list.get(i).getId());
        }

        return ResponseIRDeviceList;
    }

    public static String remoteDevice(String userId, String userPw, String deviceName, String instruction){
        HttpHandler hp = new HttpHandler();
        String result = null;
        String tempUri = uriMaker(METHOD_remoteDevice, userId, userPw);
        Uri.Builder tempUri2 = Uri.parse(tempUri).buildUpon();
        String uri = tempUri2.appendQueryParameter("deviceName",deviceName).appendQueryParameter("userInstruction",instruction)
        .build().toString();

        try {
            result = hp.execute(uri,"GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return result;
    }

    public static String uriMaker(int mode, String id, String pw){
        Uri.Builder baseUri = Uri.parse("http://192.168.137.28:8080/SWCD-war").buildUpon();
        String makedUri = null;
        switch (mode){
            case METHOD_login: //GET: id, pw
                makedUri = baseUri.appendPath("webresources").appendPath("userservice").appendPath("logIn")
                        .appendQueryParameter("id", id).appendQueryParameter("pw", pw).build().toString();
                break;
            case METHOD_showUserInfo: //GET: familyId, familyPw
                makedUri = baseUri.appendPath("webresources").appendPath("userservice").appendPath("showAllUserByFamily")
                        .appendQueryParameter("familyId", id).appendQueryParameter("familyPw", pw).build().toString();
                break;
            case METHOD_showIRdeviceInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("showAllDeviceByFamily")
                        .appendQueryParameter("familyId", id).appendQueryParameter("familyPw", pw).build().toString();
                break;
            case METHOD_showRemoteInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("showAllDeviceByUser")
                        .appendQueryParameter("userId",id).appendQueryParameter("userPw",pw).build().toString();
                break;
            case METHOD_showLogInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("showAllDoorlocklogByFamily")
                        .appendQueryParameter("familyId",id).appendQueryParameter("familyPw",pw).build().toString();
                break;
            case METHOD_remoteDevice:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("remoteControlByUser")
                        .appendQueryParameter("userId",id).appendQueryParameter("userPw",pw).build().toString();

        }
        return makedUri;
    }

    public static String uriMaker(int mode){
        Uri.Builder baseUri = Uri.parse("http://192.168.137.28:8080/SWCD-war").buildUpon();
        String makedUri = null;

        switch(mode){
            case METHOD_addUserInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("userservice").appendPath("addOneUserByFamily")
                        .build().toString();
                break;
            case METHOD_reviseUserInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("userservice").appendPath("reviseOneUserByFamily")
                        .build().toString();
                break;
            case METHOD_addIRdeviceInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("addOneDeviceByFamily")
                        .build().toString();
                break;
            case METHOD_reviseIRdeviceInfo:
                makedUri = baseUri.appendPath("webresources").appendPath("IRservice").appendPath("reviseOneDeviceByFamily")
                        .build().toString();
                break;
        }

        return makedUri;
    }
}
