package com.example.jeong.hanjo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jeong.hanjo.data.ResponseIRDevice;

/**
 * Created by Jeong on 2016-11-17.
 */
public class DetailIRdeviceActivity extends Activity{

    String familyId;
    String familyPw;
    ResponseIRDevice dev;
    ViewHolder holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_irdevice);

        Intent intent = getIntent();
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
        dev = (ResponseIRDevice)intent.getSerializableExtra("ResponseIRDevice");

        holder = new ViewHolder();
        holder.textView_name = (TextView)findViewById(R.id.textView_detail_IRdeviceName);
        holder.textView_address = (TextView)findViewById(R.id.textView_detail_IRdeviceAddress);
        holder.textView_company = (TextView)findViewById(R.id.textView_detail_company);
        holder.textView_classification = (TextView)findViewById(R.id.textView_detail_classification);
        holder.textView_model = (TextView)findViewById(R.id.textView_detail_model);

        holder.textView_name.setText(dev.getName());
        holder.textView_address.setText(dev.getAdress());
        holder.textView_company.setText(dev.getCompany());
        holder.textView_classification.setText(dev.getClassification());
        holder.textView_model.setText(dev.getModel());

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void reviseClicked(View v){
        //IR device 수정
        Intent intent = new Intent(DetailIRdeviceActivity.this, IRDeviceInfo.class);
        intent.putExtra("familyId", familyId);
        intent.putExtra("familyPw", familyPw);
        intent.putExtra("mode", MainActivity.MODE_REVISE);
        intent.putExtra("ResponseIRDevice", dev);
        startActivityForResult(intent, 2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if(resultCode == RESULT_OK){
            ResponseIRDevice updatedDeviceInfo = (ResponseIRDevice)intent.getSerializableExtra("update");
            dev = updatedDeviceInfo;
            holder.textView_name.setText(updatedDeviceInfo.getName());
            holder.textView_address.setText(updatedDeviceInfo.getAdress());
            holder.textView_company.setText(updatedDeviceInfo.getCompany());
            holder.textView_classification.setText(updatedDeviceInfo.getClassification());
            holder.textView_model.setText(updatedDeviceInfo.getModel());
            Toast.makeText(DetailIRdeviceActivity.this, updatedDeviceInfo.getId() + "/" + updatedDeviceInfo.getName(), Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(DetailIRdeviceActivity.this, "onResult else!", Toast.LENGTH_SHORT).show();
        }
    }

    class ViewHolder{
        TextView textView_name, textView_address, textView_company,textView_classification, textView_model;
    }
}
