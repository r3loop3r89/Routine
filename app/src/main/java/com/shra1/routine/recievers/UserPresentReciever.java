package com.shra1.routine.recievers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;

import com.shra1.routine.R;
import com.shra1.routine.activities.TiffinActivity;
import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.TiffinEntry;
import com.shra1.routine.utils.Utils;

import org.joda.time.DateTime;

import static com.shra1.routine.utils.Constants.getDateFormatted;

public class UserPresentReciever extends BroadcastReceiver {

    public boolean isRegistered;
    private NotificationManager notificationManager;
    private String CHANNEL_ID = "myChannelId";
    private CharSequence CHANNEL_NAME = "shrawansChannel";

    @Override
    public void onReceive(Context context, Intent intent) {
        MyDatabase myDatabase = MyDatabase.getInstance(context);
        DateTime dt = new DateTime();
        String dateFormatted = getDateFormatted(dt);
        TiffinEntry tiffinEntry = TiffinEntry.DBCommands.getTiffinEntryByFormattedToday(dateFormatted, myDatabase.getReadableDatabase());

        if (tiffinEntry == null) {

            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(100);

            if (notificationManager == null) {
                notificationManager =
                        (NotificationManager)
                                context.getSystemService(Context.NOTIFICATION_SERVICE);
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(
                        CHANNEL_ID,
                        CHANNEL_NAME,
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(notificationChannel);
            }


            Intent notificationIntent = new Intent(context, TiffinActivity.class);

            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    101,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            );


            NotificationCompat.Builder builder
                    = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_lunchbox)
                    .setContentTitle("Tiffin Entry")
                    .setAutoCancel(true)
                    //.setContentIntent(pendingIntent)
                    .setContentText("You have not done todays Tiffin Entry. Kindly do that and dont forget to sync the data!")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT);


            if (Utils.checkUserNameNotSet(context)) {

            } else {
                builder.setContentIntent(pendingIntent);
            }

            notificationManager.notify(45, builder.build());
        }
    }

    /**
     * register receiver
     *
     * @param context - Context
     * @param filter  - Intent Filter
     * @return see Context.registerReceiver(BroadcastReceiver,IntentFilter)
     */
    public Intent register(Context context, IntentFilter filter) {
        try {
            // here I propose to create
            // a isRegistered(Contex) method
            // as you can register receiver on different context
            // so you need to match against the same one :)
            // example  by storing a list of weak references
            // see LoadedApk.class - receiver dispatcher
            // its and ArrayMap there for example
            return !isRegistered
                    ? context.registerReceiver(this, filter)
                    : null;
        } finally {
            isRegistered = true;
        }
    }

    /**
     * unregister received
     *
     * @param context - context
     * @return true if was registered else false
     */
    public boolean unregister(Context context) {
        // additional work match on context before unregister
        // eg store weak ref in register then compare in unregister
        // if match same instance
        return isRegistered
                && unregisterInternal(context);
    }

    private boolean unregisterInternal(Context context) {
        context.unregisterReceiver(this);
        isRegistered = false;
        return true;
    }
}
