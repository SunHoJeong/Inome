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

import com.example.jeong.hanjo.data.RequestIRDevice;
import com.example.jeong.hanjo.data.ResponseIRDevice;
import com.example.jeong.hanjo.utility.HttpHandler;
import com.example.jeong.hanjo.utility.Server;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by Jeong on 2016-11-17.
 */
public class IRDeviceInfo extends Activity {
    int mode = 0;
    int position_company;
    String familyId, familyPw;
    ResponseIRDevice resDevInfo;
    RequestIRDevice reqDevInfo;
    ViewHolder holder;
    String beforeName = null;
    String input_company, input_classification, input_model;
    ArrayAdapter<String> adapter_company;
    ArrayAdapter<String> adapter_class;
    ArrayAdapter<String> adapter_model;
    boolean flag_com = false;
    boolean flag_class = false;
    boolean flag_model = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_irdeviceinfo);

        Intent intent = getIntent();
        mode = intent.getIntExtra("mode", 0);
        familyId = intent.getStringExtra("familyId");
        familyPw = intent.getStringExtra("familyPw");
        resDevInfo = (ResponseIRDevice)intent.getSerializableExtra("ResponseIRDevice");

        holder = new ViewHolder();
        holder.editText_IRname = (EditText)findViewById(R.id.editText_IRdeviceInfo_name);
        holder.editText_Address = (EditText)findViewById(R.id.editText_IRdeviceInfo_address);
        holder.spinner_company = (Spinner)findViewById(R.id.spinner_company);
        final ArrayList<String> comList = getComList();

        adapter_company = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, comList);
        holder.spinner_company.setAdapter(adapter_company);
        holder.spinner_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    position_company = position;
                    flag_com = false;
                }
                else{
                    position_company = position;
                    input_company = comList.get(position);
                    flag_com = true;

                    Log.i("SPINNER", input_company+" id:"+id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("SPINNER","Nothing is clicked!");
            }
        });

        holder.spinner_classification = (Spinner)findViewById(R.id.spinner_classification);
        //final ArrayList<String> classList = getClassList(familyId, familyPw);

        String[] str_class = {"선택해주세요","tv","aircon","heater"};
        adapter_class = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str_class);
        holder.spinner_classification.setAdapter(adapter_class);
        holder.spinner_classification.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    input_classification = null;
                    flag_class = false;
                }
                else{
                    //input_classification = str_class[position];
                    input_classification = parent.getItemAtPosition(position).toString();
                    flag_class = true;
                    Log.i("SPINNER", input_classification+" id:"+id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("SPINNER","Nothing is clicked!");
            }
        });

        holder.spinner_model = (Spinner)findViewById(R.id.spinner_model);
        final String[] str_model = {"선택해주세요","55555555","66666666"};
        adapter_model = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str_model);
        holder.spinner_model.setAdapter(adapter_model);
        holder.spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    input_model = null;
                    flag_model = false;
                }
                else{
                    input_model = str_model[position];
                    flag_model = true;
                    Log.i("SPINNER", input_model+" id:"+id);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("SPINNER","Nothing is clicked!");
            }
        });


        if(mode == MainActivity.MODE_REVISE){
            beforeName = resDevInfo.getName();
            holder.editText_IRname.setText(resDevInfo.getName());
            holder.editText_IRname.setText(resDevInfo.getAdress());

            Log.i("IRdeviceInfo", "--Oncreate revise 모드 --");
        }
        else if(mode == MainActivity.MODE_ADD){
            beforeName = null;
            Log.i("IRdeviceInfo", "--Oncreate add 모드 --");
        }
        else{
            Log.i("IRdeviceInfo", "--Oncreate모드오류 --");
        }

    }

    public void checkClicked(View v){
        String name, address, company, classification, model;
        String uri = null;

        if(!flag_com && !flag_class && !flag_model){
            Toast.makeText(IRDeviceInfo.this, "선택하지 않은 것들이 있습니다!!", Toast.LENGTH_SHORT).show();
            return;
        }

        name = holder.editText_IRname.getText().toString();
        address = holder.editText_Address.getText().toString();
        //TODO:어떤방식으로할지? 결정하기 1.adapter 2.input_string
        company = adapter_company.getItem(position_company);
        classification = input_classification;
        model = input_model;

        Log.i("--CHECK--",name+"/"+address+"/"+company+"/"+classification+"/"+model);

        reqDevInfo = new RequestIRDevice(familyId, familyPw, beforeName, company, classification, model, name, address);
        Log.i("--DevInfo--",reqDevInfo.getFamilyId()+" "+reqDevInfo.getFamilyPw()+" "+reqDevInfo.getOldDeviceName()+" "+reqDevInfo.getNewDeviceAddress()
        +" "+reqDevInfo.getNewDeviceModel()+" "+reqDevInfo.getNewDeviceCompany()+" "+reqDevInfo.getNewDeviceClassification());

        if(mode == MainActivity.MODE_REVISE){
            //url = "http://192.168.137.28:8080/SWCD-war/webresources/IRservice/reviseOneDeviceByFamily";
            uri = Server.uriMaker(Server.METHOD_reviseIRdeviceInfo);
            resDevInfo.setName(name);

            Gson gson = new Gson();
            String json = gson.toJson(reqDevInfo);
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
                finish();
                Log.i("--IRDeviceInfo--", "success");
            }
            else{
                finish();
                Log.i("--IRDeviceInfo--", "failed");
            }
            //resUserInfo.setName(name);
        }
        else if(mode == MainActivity.MODE_ADD){
            //uri = "http://192.168.137.28:8080/SWCD-war/webresources/IRservice/addOneDeviceByFamily";
            uri = Server.uriMaker(Server.METHOD_addIRdeviceInfo);
            Gson gson = new Gson();
            String json = gson.toJson(reqDevInfo);
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
                finish();
                Log.i("--IRDeviceInfo--", "success");
            }
            else{
                finish();
                Log.i("--IRDeviceInfo--", "failed");
            }
        }

    }

    class ViewHolder{
        EditText editText_IRname, editText_Address;
        Spinner spinner_company, spinner_classification, spinner_model;
    }

    public ArrayList<String> getComList(){
        //TODO: 분류, 모델 추가하기!
        //getAllDeviceClassification~ (fid,fpw,company)
        //"          Model~(fid,fpw,company, classification)
        String result = null;
        HttpHandler hp = new HttpHandler();
        try {
            result = hp.execute("http://192.168.137.28:8080/SWCD-war/webresources/IRservice/getAllDeviceCompanyByFamily?familyId="+familyId+"&familyPw="+familyPw, "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String[] comList = gson.fromJson(result, String[].class);
        List<String> temp = Arrays.asList(comList);
        ArrayList<String> list = new ArrayList<String>(temp);
        list.add(0,"바보바보");

        return list;
    }

    public ArrayList<String> getClassList(String familyId, String familyPw, String company){
        String result = null;
        HttpHandler hp = new HttpHandler();
        try {
            result = hp.execute("http://192.168.137.28:8080/SWCD-war/webresources/IRservice/getAllDeviceClassification?" +
                    "familyId="+familyId+"&familyPw="+familyPw+"&company="+company, "GET").get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        String[] comList = gson.fromJson(result, String[].class);
        List<String> temp = Arrays.asList(comList);
        ArrayList<String> list = new ArrayList<String>(temp);
        list.add(0,"바보바보");

        return list;

    }
}
