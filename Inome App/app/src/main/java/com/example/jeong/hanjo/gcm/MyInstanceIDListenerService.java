package com.example.jeong.hanjo.gcm;

import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Jeong on 2016-12-05.
 */
public class MyInstanceIDListenerService extends FirebaseInstanceIdService {

    private static final String TAG = "MyInstanceIDLS";

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
            //token등록?

        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);
    }
}