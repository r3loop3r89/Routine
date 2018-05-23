package com.shra1.routine;

import android.app.Application;
import android.provider.Settings;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class App extends Application {

    public static DatabaseReference ref;
    public static DatabaseReference tiffin_entries_ref;
    public static DatabaseReference daily_expense_entries_ref;

    @Override
    public void onCreate() {
        super.onCreate();


        String clientUniqueId = Settings.Secure.getString(
                getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID
        );

        ref = FirebaseDatabase.getInstance().getReference("ROUTINE_APP").child("USERS").child(clientUniqueId);

        tiffin_entries_ref = ref.child("tiffin_entries");

        daily_expense_entries_ref = ref.child("daily_expense_entries");

    }
}
