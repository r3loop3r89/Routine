package com.shra1.routine.utils;

import android.content.Context;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

public class Utils {
    public static boolean cantBeEmptyET(EditText etSAUserName) {
        if (etSAUserName.getText().toString().trim().length() == 0) {
            etSAUserName.setError("Can't be Empty!");
            return true;
        }
        return false;
    }
    public static boolean cantBeEmptyACTV(AutoCompleteTextView actv) {
        if (actv.getText().toString().trim().length() == 0) {
            actv.setError("Can't be Empty!");
            return true;
        }
        return false;
    }

    public static boolean checkUserNameNotSet(Context mCtx) {
        SharedPreferencesManager sharedPreferencesManager = SharedPreferencesManager.getInstance(mCtx);
        if (sharedPreferencesManager.getUserName().equals("UserName")){
            return true;
        }
        return false;
    }
}
