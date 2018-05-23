package com.shra1.routine.recievers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.shra1.routine.services.UserPresentRecieverRegistrationService;
import com.shra1.routine.utils.Constants;
import com.shra1.routine.utils.SharedPreferencesManager;

public class ServiceRetainerReciever extends BroadcastReceiver {

    public static final String TAG="ShraX";
    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "Recieved", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onReceive: '");

        SharedPreferencesManager sharedPreferencesManager
                = SharedPreferencesManager.getInstance(context);

        if (sharedPreferencesManager.getEnableNotificationsForTiffinEntry()){
            if (Constants.isMyServiceRunning(context, UserPresentRecieverRegistrationService.class)) { }else{
                Intent forceFullIntent = new Intent(context, UserPresentRecieverRegistrationService.class);
                context.startService(forceFullIntent);
            }
        }
    }
}
