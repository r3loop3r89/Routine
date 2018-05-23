package com.shra1.routine.services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.shra1.routine.recievers.UserPresentReciever;

import java.util.HashMap;
import java.util.Map;

public class UserPresentRecieverRegistrationService extends Service {

    public static final String TAG = "ShraX";
    IntentFilter intentFilter = new IntentFilter(Intent.ACTION_USER_PRESENT);
    UserPresentReciever broadcastReceiver = new UserPresentReciever();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        broadcastReceiver.register(this, intentFilter);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        broadcastReceiver.unregister(this);
        super.onDestroy();
        Intent unkill = new Intent("com.shra1.routine.YouWillNeverKillMe");
        sendBroadcast(unkill);
        Log.d(TAG, "onDestroy: ");
    }

}