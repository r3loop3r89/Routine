package com.shra1.routine.asynctasks;

import android.os.AsyncTask;

import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.DailyExpenseEntry;

import java.util.ArrayList;
import java.util.List;

public class DailyExpenseEntriesSynchronizingTask extends AsyncTask<Object, Void, Void> {

    DailyExpenseEntriesSynchronizingTaskCallback c;

    public DailyExpenseEntriesSynchronizingTask(DailyExpenseEntriesSynchronizingTaskCallback c) {
        this.c = c;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        c.onProcessStart();
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        c.onProcessEnd();
    }

    @Override
    protected Void doInBackground(Object... objects) {
        List<DailyExpenseEntry> localEntries = (List<DailyExpenseEntry>) objects[0];
        List<DailyExpenseEntry> firebaseEntries = (List<DailyExpenseEntry>) objects[1];
        MyDatabase db = (MyDatabase) objects[2];

        List<DailyExpenseEntry> temp = new ArrayList<>();

        for (DailyExpenseEntry e : localEntries) {
            temp.add(e);
        }
        for (DailyExpenseEntry e : firebaseEntries) {
            temp.add(e);
        }

        for (DailyExpenseEntry e : temp) {
            if (!localEntries.contains(e)) {
                DailyExpenseEntry.DBCommands.addExpenseEntry(e, db.getWritableDatabase());
            }

            if (!firebaseEntries.contains(e)) {
                DailyExpenseEntry.FirebaseCommands.addExpenseEntry(e, new DailyExpenseEntry.FirebaseCommands.AddExpenseEntryCallback() {
                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(String error) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
            }
        }

        return null;
    }


    public interface DailyExpenseEntriesSynchronizingTaskCallback {
        public void onProcessStart();

        public void onProcessEnd();
    }
}
