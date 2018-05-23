package com.shra1.routine.utils;

import android.app.ActivityManager;
import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Constants {
    public static final String TIFFIN_TAKEN_YES = "YES";
    public static final String TIFFIN_TAKEN_NO = "NO";

    public static final String NAME_OF_DAY = "EEEE";
    public static final String NAME_OF_MONTH = "MMMM";


    public static boolean isMyServiceRunning(Context c , Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) c.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    public static String getDateFormatted(DateTime dt) {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(NAME_OF_DAY); // use 'E' for short abbreviation (Mon, Tues, etc)
        String strEnglish = fmt.print(dt);
        fmt = DateTimeFormat.forPattern(NAME_OF_MONTH);
        String name_of_month = fmt.print(dt);

        String todays_date = "" + dt.getDayOfMonth() + " " + name_of_month;
        return strEnglish + ", " + todays_date;
    }


}
