package com.shra1.routine.asynctasks;

import android.os.AsyncTask;

import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.TiffinEntry;

import java.util.ArrayList;
import java.util.List;

public class TiffinEntriesSynchronizingTask extends AsyncTask<Object, Void, Void> {

    TiffinEntriesSynchronizingTaskCallback c;

    public TiffinEntriesSynchronizingTask(TiffinEntriesSynchronizingTaskCallback c) {
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

        List<TiffinEntry> localEntries = (List<TiffinEntry>) objects[0];
        List<TiffinEntry> firebaseEntries = (List<TiffinEntry>) objects[1];
        MyDatabase db = (MyDatabase) objects[2];

        List<TiffinEntry> temp = new ArrayList<>();

        for (TiffinEntry e :
                localEntries) {
            temp.add(e);
        }
        for (TiffinEntry e :
                firebaseEntries) {
            temp.add(e);
        }


        for (TiffinEntry e : temp) {
            if (!localEntries.contains(e)) {
                TiffinEntry.DBCommands.addTiffinEntry(e, db.getWritableDatabase());
            }

            if (!firebaseEntries.contains(e)) {
                TiffinEntry.FirebaseCommands.addTiffinEntry(e, new TiffinEntry.FirebaseCommands.AddTiffinEntryCallback() {
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

    public interface TiffinEntriesSynchronizingTaskCallback {
        public void onProcessStart();
        public void onProcessEnd();
    }
}
