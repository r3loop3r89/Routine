package com.shra1.routine.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    public static SharedPreferencesManager instance = null;
    Context mCtx;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private SharedPreferencesManager(Context mCtx) {
        this.mCtx = mCtx;
        sharedPreferences = mCtx.getSharedPreferences(
                mCtx.getPackageName() +
                        "." +
                        getClass().getName(),
                Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public static SharedPreferencesManager getInstance(Context mCtx) {
        if (instance == null) {
            instance = new SharedPreferencesManager(mCtx);
        }
        return instance;
    }

    public String getUserName() {
        return sharedPreferences.getString("UserName", "UserName");
    }

    public void setUserName(String UserName) {
        editor.putString("UserName", UserName);
        editor.commit();
    }

    public boolean getEnableNotificationsForTiffinEntry() {
        return sharedPreferences.getBoolean("EnableNotificationsForTiffinEntry", false);
    }

    public void setEnableNotificationsForTiffinEntry(boolean EnableNotificationsForTiffinEntry) {
        editor.putBoolean("EnableNotificationsForTiffinEntry", EnableNotificationsForTiffinEntry);
        editor.commit();
    }

}
