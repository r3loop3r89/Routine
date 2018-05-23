package com.shra1.routine.mymodels;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.shra1.routine.App.daily_expense_entries_ref;

public class DailyExpenseEntry {
    public static final String TABLE_NAME = "DailyExpense";
    public static final String sFormattedDate = "FormattedDate";
    public static final String sSpentOnName = "SpentOnName";
    public static final String sSpentAmount = "SpentAmount";
    public static final String sEntryOnEpoch = "EntryOnEpoch";
    public static final String TAG = "Shrax";
    String FormattedDate;
    String SpentOnName;
    double SpentAmount;
    long EntryOnEpoch;

    public DailyExpenseEntry() {
    }

    public DailyExpenseEntry(String formattedDate, String spentOnName, double spentAmount, long entryOnEpoch) {
        FormattedDate = formattedDate;
        SpentOnName = spentOnName;
        SpentAmount = spentAmount;
        EntryOnEpoch = entryOnEpoch;
    }

    @Override
    public boolean equals(Object obj) {
        DailyExpenseEntry o = (DailyExpenseEntry) obj;
        if (
                o.getSpentAmount() == getSpentAmount()
                        &&
                        o.getEntryOnEpoch() == getEntryOnEpoch()
                        &&
                        o.getFormattedDate().equals(getFormattedDate())
                        &&
                        o.getSpentOnName().equals(getSpentOnName())
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

    public String getSpentOnName() {
        return SpentOnName;
    }

    public void setSpentOnName(String spentOnName) {
        SpentOnName = spentOnName;
    }

    public double getSpentAmount() {
        return SpentAmount;
    }

    public void setSpentAmount(double spentAmount) {
        SpentAmount = spentAmount;
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
                        sFormattedDate + " TEXT," +
                        sSpentOnName + " TEXT," +
                        sSpentAmount + " NUMERIC," +
                        sEntryOnEpoch + " NUMERIC" +
                        " ) ";

        public static void addExpenseEntry(DailyExpenseEntry e, SQLiteDatabase db) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(sFormattedDate, e.getFormattedDate());
            contentValues.put(sSpentOnName, e.getSpentOnName());
            contentValues.put(sSpentAmount, e.getSpentAmount());
            contentValues.put(sEntryOnEpoch, e.getEntryOnEpoch());

            db.insert(TABLE_NAME, null, contentValues);

        }

        public static List<DailyExpenseEntry> getDailyExpenseDetails(String s, SQLiteDatabase db) {
            List<DailyExpenseEntry> l = new ArrayList<>();

            Cursor cT = db.query(
                    TABLE_NAME, null,
                    sFormattedDate + " = ? ",
                    new String[]{s},
                    null, null, null);

            if (cT.getCount() > 0) {
                cT.moveToFirst();
                do {
                    DailyExpenseEntry e = new DailyExpenseEntry(
                            cT.getString(cT.getColumnIndex(sFormattedDate)),
                            cT.getString(cT.getColumnIndex(sSpentOnName)),
                            cT.getDouble(cT.getColumnIndex(sSpentAmount)),
                            cT.getLong(cT.getColumnIndex(sEntryOnEpoch))
                    );

                    l.add(e);
                } while (cT.moveToNext());
            }

            return l;
        }

        public static List<DailyExpenseEntry> getAllDailyExpenseEntries(SQLiteDatabase db) {
            List<DailyExpenseEntry> l = new ArrayList<>();

            Cursor cD = db.query(TABLE_NAME,
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cD.getCount() > 0) {
                cD.moveToFirst();
                do {
                    DailyExpenseEntry e = new DailyExpenseEntry(
                            cD.getString(cD.getColumnIndex(sFormattedDate)),
                            cD.getString(cD.getColumnIndex(sSpentOnName)),
                            cD.getDouble(cD.getColumnIndex(sSpentAmount)),
                            cD.getLong(cD.getColumnIndex(sEntryOnEpoch))
                    );
                    l.add(e);
                } while (cD.moveToNext());
            }


            return l;
        }

        public static void deleteDailyExpenseEntry(DailyExpenseEntry selectedDailyExpenseEntry, SQLiteDatabase db) {
            db.delete(TABLE_NAME, sEntryOnEpoch+" = ?", new String[]{""+selectedDailyExpenseEntry.getEntryOnEpoch()});
        }
    }

    public static class FirebaseCommands {

        public static void getAllDailyExpenseEntries(final GetAllDailyExpenseEntriesCallback c) {
            final List<DailyExpenseEntry> alldailyExpenseEntries = new ArrayList<>();
            c.onPreExecute();

            daily_expense_entries_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    c.onComplete();
                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        DailyExpenseEntry e = d.getValue(DailyExpenseEntry.class);
                        alldailyExpenseEntries.add(e);
                    }
                    c.onSuccess(alldailyExpenseEntries);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    c.onComplete();
                    c.onError(databaseError.toString());
                }
            });
        }

        public static void addExpenseEntry(DailyExpenseEntry e, final AddExpenseEntryCallback c) {
            c.onPreExecute();
            daily_expense_entries_ref.push().setValue(e).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    c.onComplete();
                    if (task.isSuccessful()){
                        c.onSuccess();
                    }else{
                        c.onError(task.getException().toString());
                    }
                }
            });
        }


        public interface AddExpenseEntryCallback{
            public void onPreExecute();

            public void onSuccess();

            public void onError(String error);

            public void onComplete();
        }

        public interface GetAllDailyExpenseEntriesCallback {
            public void onPreExecute();

            public void onSuccess(List<DailyExpenseEntry> firebaseDailyExpenseEntries);

            public void onError(String error);

            public void onComplete();
        }

    }
}
