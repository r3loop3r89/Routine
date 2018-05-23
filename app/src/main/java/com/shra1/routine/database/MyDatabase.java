package com.shra1.routine.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shra1.routine.mymodels.DailyExpenseEntry;
import com.shra1.routine.mymodels.TiffinEntry;

public class MyDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "routine.db";

    public static final int VERSION = 1;

    public static MyDatabase instance = null;

    private MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    public static MyDatabase getInstance(Context mCtx) {
        if (instance == null) {
            instance = new MyDatabase(mCtx);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TiffinEntry.DBCommands.CREATE_TABLE);
        db.execSQL(DailyExpenseEntry.DBCommands.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
