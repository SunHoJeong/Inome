package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.jeong.hanjo.data.RequestUserInfo;
import com.example.jeong.hanjo.data.ResponseUserInfo;
import com.example.jeong.hanjo.utility.HttpHandler;
import com.example.jeong.hanjo.utility.Server;
import com.google.gson.Gson;

import java.util.concurrent.ExecutionException;

/**
 * Created by Jeong on 2016-11-15.
 */
public class UserInfo extends Activity {
    ViewHolder holder;
    int mode = 0;
    String beforeName = null;
    String familyId, familyPw;
    RequestUserInfo reqUserInfo;
    ResponseUserInfo resUserInfo;
    ArrayAdapter<String> adapter_authority;
    String input_authority;
    boolean flag = false;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);

        intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
        resUserInfo = (ResponseUserInfo)intent.getSerializableExtra("userInfo");

        holder = new ViewHolder();
        holder.editText_id = (EditText)findViewById(R.id.editText_userInfo_id);
        holder.editText_pw = (EditText)findViewById(R.id.editText_userInfo_pw);
        holder.editText_name = (EditText)findViewById(R.id.editText_userInfo_name);
        holder.editText_phone = (EditText)findViewById(R.id.editText_userInfo_phone);
        holder.spinner_authority = (Spinner)findViewById(R.id.spinner_userInfo_authority);

        final String[] str_authority = {"선택해주세요","send","receive"};
        adapter_authority = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str_authority);
        holder.spinner_authority.setAdapter(adapter_authority);
        holder.spinner_authority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    flag = false;
                    input_authority = null;
                }
                else{
                    flag = true;
                    input_authority = str_authority[position];

                    Log.i("SPINNER", input_authority+" id:"+id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("SPINNER","Nothing is clicked!");
            }
        });

        if(mode == MainActivity.MODE_REVISE){
            beforeName = resUserInfo.getName();
            holder.editText_id.setText(resUserInfo.getId());
            holder.editText_pw.setText(resUserInfo.getPw());
            holder.editText_name.setText(resUserInfo.getName());
            holder.editText_phone.setText(resUserInfo.getPhone());

            Log.i("UserInfo", "--Oncreate revise 모드 --");
        }
        else if(mode == MainActivity.MODE_ADD){
            beforeName = null;
            Log.i("UserInfo", "--Oncreate add 모드 --");
        }
        else{
            Log.i("UserInfo", "--Oncreate 모드 오류입니다--mode:"+mode);
        }

    }

    public void checkClicked(View v){
        //send RequestUserInfo to server

        String id,pw,name,phone,authority;
        String uri = null;

        if(!flag){
            Toast.makeText(UserInfo.this, "authority를 입력해 주세요!", Toast.LENGTH_SHORT).show();
            return;
        }

        id = holder.editText_id.getText().toString();
        pw = holder.editText_pw.getText().toString();
        name = holder.editText_name.getText().toString();
        phone = holder.editText_phone.getText().toString();
        authority = input_authority;

        reqUserInfo = new RequestUserInfo(familyId, familyPw, beforeName, id, pw, name, phone, authority);
        Log.i("--UserInfo--",reqUserInfo.getFamilyId()+" "+reqUserInfo.getFamilyPw()+" "+reqUserInfo.getOldUserName()+" "+reqUserInfo.getNewUserId()
                +" "+reqUserInfo.getNewUserPw()+" "+reqUserInfo.getNewUserName()+" "+reqUserInfo.getNewUserPhone()+" "+reqUserInfo.getNewUserAuthority());

        if(mode == MainActivity.MODE_REVISE){
            uri = Server.uriMaker(Server.METHOD_reviseUserInfo);

            Gson gson = new Gson();
            String json = gson.toJson(reqUserInfo);
            HttpHandler hp = new HttpHandler();
            String res = null;
            try {
                res = hp.execute(uri, "POST", json).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(res.equals("\"success\"")){
                resUserInfo.setId(id);
                resUserInfo.setPw(pw);
                resUserInfo.setName(name);
                resUserInfo.setPhone(phone);
                resUserInfo.setAuthority(authority);

                intent.putExtra("update", resUserInfo);
                setResult(RESULT_OK, intent);
                Log.i("--UserInfo--", "success");
                Toast.makeText(UserInfo.this, res, Toast.LENGTH_SHORT).show();
                finish();

            }
            else{
                Log.i("--UserInfo--", "failed");
                Toast.makeText(UserInfo.this, mode+"실패 ->"+res, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
        else if(mode == MainActivity.MODE_ADD){
            //uri = "http://192.168.137.28:8080/SWCD-war/webresources/userservice/addOneUserByFamily";
            uri = Server.uriMaker(Server.METHOD_addUserInfo);
            Gson gson = new Gson();
            String json = gson.toJson(reqUserInfo);
            HttpHandler hp = new HttpHandler();
            String res = null;
            try {
                res = hp.execute(uri, "POST", json).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            if(res.equals("\"success\"")){
//                resUserInfo.setId(id);
//                resUserInfo.setPw(pw);
//                resUserInfo.setName(name);
//                resUserInfo.setPhone(phone);
//                resUserInfo.setAuthority(authority);
//
//                intent.putExtra("update", resUserInfo);
//                setResult(RESULT_OK, intent);
                Log.i("--UserInfo--", "success");
                Toast.makeText(UserInfo.this, res, Toast.LENGTH_SHORT).show();
                finish();

            }
            else{
                Log.i("--UserInfo--", "failed");
                Toast.makeText(UserInfo.this, mode+"실패 ->"+res, Toast.LENGTH_SHORT).show();
                finish();
            }
        }


    }

    class ViewHolder{
        EditText editText_id, editText_pw, editText_name, editText_phone;
        Spinner spinner_authority;
    }
}


