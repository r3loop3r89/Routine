package com.shra1.routine.mymodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import static com.shra1.routine.App.tiffin_entries_ref;

public class TiffinEntry {

    public static final String TABLE_NAME = "Tiffin";
    public static final String sFormattedDate = "FormattedDate";
    public static final String sTiffinTaken = "TiffinTaken";
    public static final String sEntryOnEpoch = "EntryOnEpoch";
    public static final String TAG = "shrax";
    String FormattedDate;
    String TiffinTaken;
    long EntryOnEpoch;

    public TiffinEntry() {

    }

    public TiffinEntry(String formattedDate, String tiffinTaken, long entryOnEpoch) {

        FormattedDate = formattedDate;
        TiffinTaken = tiffinTaken;
        EntryOnEpoch = entryOnEpoch;
    }

    @Override
    public boolean equals(Object obj) {
        TiffinEntry o = (TiffinEntry) obj;
        if (o.getTiffinTaken().equals(getTiffinTaken())
                &&
                o.getEntryOnEpoch() == getEntryOnEpoch()
                &&
                o.getFormattedDate().equals(getFormattedDate())
                ) {
            return true;
        } else {
            return false;
        }
    }

    public String getFormattedDate() {
        return FormattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        FormattedDate = formattedDate;
    }

    public String getTiffinTaken() {
        return TiffinTaken;
    }

    public void setTiffinTaken(String tiffinTaken) {
        TiffinTaken = tiffinTaken;
    }

    public long getEntryOnEpoch() {
        return EntryOnEpoch;
    }

    public void setEntryOnEpoch(long entryOnEpoch) {
        EntryOnEpoch = entryOnEpoch;
    }

    public static class DBCommands {
        public static final String CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME +
                        " ( " +
                        sFormattedDate + " TEXT, " +
                        sTiffinTaken + " TEXT, " +
                        sEntryOnEpoch + " NUMERIC " +
                        " ) ";

        public static void addTiffinEntry(TiffinEntry tiffinEntry, SQLiteDatabase db) {
            ContentValues v = new ContentValues();
            v.put(sFormattedDate, tiffinEntry.getFormattedDate());
            v.put(sTiffinTaken, tiffinEntry.getTiffinTaken());
            v.put(sEntryOnEpoch, tiffinEntry.getEntryOnEpoch());

            db.insert(TABLE_NAME, null, v);
        }

        public static TiffinEntry getTiffinEntryByFormattedToday(String dateFormatted, SQLiteDatabase db) {
            TiffinEntry tiffinEntry;

            Cursor cT = db.query(TABLE_NAME, null, sFormattedDate + " = ?", new String[]{dateFormatted}, null, null, null);

            if (cT.getCount() > 0) {
                cT.moveToFirst();

                tiffinEntry = new TiffinEntry(
                        cT.getString(cT.getColumnIndex(sFormattedDate)),
                        cT.getString(cT.getColumnIndex(sTiffinTaken)),
                        cT.getLong(cT.getColumnIndex(sEntryOnEpoch))
                );
                return tiffinEntry;
            }
            return null;
        }

        public static List<TiffinEntry> getTiffinEntriesForMonth(DateTime dateTime, SQLiteDatabase db) {
            List<TiffinEntry> l = new ArrayList<>();

            DateTime startOfMonth = dateTime.dayOfMonth().withMinimumValue();
            startOfMonth =
                    startOfMonth
                            .withHourOfDay(00)
                            .withMinuteOfHour(00)
                            .withSecondOfMinute(00);

            DateTime endOfMonth = dateTime.dayOfMonth().withMaximumValue();
            endOfMonth =
                    endOfMonth
                            .withHourOfDay(23)
                            .withMinuteOfHour(59)
                            .withSecondOfMinute(59);

            Log.d(TAG, "start : " + startOfMonth.getMillis() + "\n" +
                    "end : " + endOfMonth.getMillis());

            long lStartOfMonth = startOfMonth.getMillis();
            long lEndOfMonth = endOfMonth.getMillis();

            Cursor cT = db.query(
                    TABLE_NAME,
                    null,
                    sEntryOnEpoch + " >= ? AND " + sEntryOnEpoch + " <= ? ",
                    new String[]{"" + lStartOfMonth, "" + lEndOfMonth},
                    null, null, sEntryOnEpoch + " ASC");

            if (cT.getCount() > 0) {
                cT.moveToFirst();
                do {
                    TiffinEntry e = new TiffinEntry(
                            cT.getString(cT.getColumnIndex(sFormattedDate)),
                            cT.getString(cT.getColumnIndex(sTiffinTaken)),
                            cT.getLong(cT.getColumnIndex(sEntryOnEpoch))
                    );

                    l.add(e);
                } while (cT.moveToNext());
            }

            return l;
        }

        public static List<TiffinEntry> getAllTiffinEntries(SQLiteDatabase db) {
            List<TiffinEntry> l = new ArrayList<>();

            Cursor cT = db.query(TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cT.getCount() > 0) {
                cT.moveToFirst();
                do {
                    TiffinEntry entry = new TiffinEntry(
                            cT.getString(cT.getColumnIndex(sFormattedDate)),
                            cT.getString(cT.getColumnIndex(sTiffinTaken)),
                            cT.getLong(cT.getColumnIndex(sEntryOnEpoch))
                    );
                    l.add(entry);
                } while (cT.moveToNext());
            }

            return l;
        }
    }

    public static class FirebaseCommands {

        public static void getAllTiffinEntries(final GetAllTiffinEntriesCallback c) {
            final List<TiffinEntry> allTiffinEntries = new ArrayList<>();
            c.onPreExecute();

            tiffin_entries_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    c.onComplete();
                    for (DataSnapshot s : dataSnapshot.getChildren()) {
                        TiffinEntry t = s.getValue(TiffinEntry.class);
                        allTiffinEntries.add(t);
                    }
                    c.onSuccess(allTiffinEntries);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    c.onComplete();
                    c.onError(databaseError.toString());
                }
            });

        }

        public static void addTiffinEntry(TiffinEntry e, final AddTiffinEntryCallback c) {
            c.onPreExecute();
            tiffin_entries_ref.push().setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    c.onComplete();
                    if (task.isSuccessful()) {
                        c.onSuccess();
                    } else {
                        c.onError(task.getException().toString());
                    }
                }
            });
        }

        public interface AddTiffinEntryCallback {
            public void onPreExecute();

            public void onSuccess();

            public void onError(String error);

            public void onComplete();
        }

        public interface GetAllTiffinEntriesCallback {
            public void onPreExecute();

            public void onSuccess(List<TiffinEntry> firebaseTiffinEntries);

            public void onError(String error);

            public void onComplete();
        }
    }
}
