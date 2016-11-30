package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseUserInfo;

/**
 * Created by Jeong on 2016-11-15.
 */
public class DetailUserActivity extends Activity {
    ViewHolder holder;
    String familyId;
    String familyPw;
    ResponseUserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
        userInfo = (ResponseUserInfo)intent.getSerializableExtra("userInfo");

        holder = new ViewHolder();
        holder.textView_familyId = (TextView)findViewById(R.id.textView_detail_familyId);
        holder.textView_id = (TextView)findViewById(R.id.textView_detail_id);
        holder.textView_name = (TextView)findViewById(R.id.textView_detail_name);
        holder.textView_phone = (TextView)findViewById(R.id.textView_detail_phone);
        holder.textView_authority = (TextView)findViewById(R.id.textView_detail_authority);

        holder.textView_familyId.setText(familyId);
        holder.textView_id.setText(userInfo.getId());
        holder.textView_name.setText(userInfo.getName());
        holder.textView_phone.setText(userInfo.getPhone());
        holder.textView_authority.setText(userInfo.getAuthority());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void reviseClicked(View v){
        //회원정보수정 이벤트
        Intent intent = new Intent(DetailUserActivity.this, UserInfo.class);
        intent.putExtra("familyId",familyId);
        intent.putExtra("familyPw",familyPw);
        intent.putExtra("mode",MainActivity.MODE_REVISE);
        intent.putExtra("userInfo",userInfo);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK){
            ResponseUserInfo updatedUserInfo = (ResponseUserInfo)intent.getSerializableExtra("update");
            userInfo = updatedUserInfo;
            holder.textView_id.setText(updatedUserInfo.getId());
            holder.textView_name.setText(updatedUserInfo.getName());
            holder.textView_phone.setText(updatedUserInfo.getPhone());
            holder.textView_authority.setText(updatedUserInfo.getAuthority());
            Toast.makeText(DetailUserActivity.this, updatedUserInfo.getId()+"/"+updatedUserInfo.getName(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(DetailUserActivity.this, "onResult else!", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder{
        TextView textView_familyId,textView_id,textView_name,textView_phone,textView_authority;
    }
}
