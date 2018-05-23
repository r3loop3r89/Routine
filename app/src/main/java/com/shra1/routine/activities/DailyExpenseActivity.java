package com.shra1.routine.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.adapters.DailyExpenseEntryAdapter;
import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.DailyExpenseEntry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.shra1.routine.utils.Constants.NAME_OF_DAY;
import static com.shra1.routine.utils.Constants.NAME_OF_MONTH;
import static com.shra1.routine.utils.Utils.cantBeEmptyACTV;
import static com.shra1.routine.utils.Utils.cantBeEmptyET;

public class DailyExpenseActivity extends AppCompatActivity {

    String dateFormatted;
    DateTime dateTime;
    List<DailyExpenseEntry> dailyExpenseEntryList;
    private Context mCtx;
    private MyDatabase myDatabase;
    //ACTIVITY WIDGETS
    private ImageButton ibPrev;
    private TextView tvDate;
    private ImageButton ibNext;
    private LinearLayout llDEAAddSpendEntry;
    private AutoCompleteTextView actvDEASpentOn;
    private EditText etDEASpentAmount;
    private TextView tvDEATotal;
    private Button bDEASave;
    private ListView lvDEAList;
    private ImageButton ibDEAExpandList;
    private View.OnClickListener expandIButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (llDEAAddSpendEntry.getVisibility() == View.VISIBLE) {
                llDEAAddSpendEntry.setVisibility(View.GONE);
            } else {
                llDEAAddSpendEntry.setVisibility(View.VISIBLE);
            }
        }
    };
    private View.OnClickListener prevIButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = dateTime.minusDays(1);
            showDateOnTv();
        }
    };
    private View.OnClickListener nextIButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = dateTime.plusDays(1);
            showDateOnTv();
        }
    };
    private View.OnClickListener todayTextViewClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = new DateTime();
            showDateOnTv();
        }
    };
    private View.OnClickListener saveButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (cantBeEmptyACTV(actvDEASpentOn)) return;
            if (cantBeEmptyET(etDEASpentAmount)) return;

            String SpentOnName = actvDEASpentOn.getText().toString().trim();

            String SpentAmount = etDEASpentAmount.getText().toString().trim();

            DailyExpenseEntry e = new DailyExpenseEntry(dateFormatted, SpentOnName, Double.parseDouble(SpentAmount), System.currentTimeMillis());

            DailyExpenseEntry.DBCommands.addExpenseEntry(e, myDatabase.getWritableDatabase());

            dailyExpenseEntryList.add(e);

            updateList();

            actvDEASpentOn.setText("");
            etDEASpentAmount.setText("");
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_expense);

        mCtx = this;
        myDatabase = MyDatabase.getInstance(mCtx);

        initViews();

        ibDEAExpandList.setOnClickListener(expandIButtonClickListener);

        dateTime = new DateTime();

        showDateOnTv();

        ibPrev.setOnClickListener(prevIButtonClickListener);
        ibNext.setOnClickListener(nextIButtonClickListener);
        tvDate.setOnClickListener(todayTextViewClickListener);


        bDEASave.setOnClickListener(saveButtonClickListener);

        lvDEAList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final DailyExpenseEntry selectedDailyExpenseEntry
                        = (DailyExpenseEntry) parent.getItemAtPosition(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(mCtx);

                builder.setTitle("Delete !");

                builder.setMessage("Do you want to delete this record ?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DailyExpenseEntry.DBCommands.deleteDailyExpenseEntry(
                                selectedDailyExpenseEntry, myDatabase.getWritableDatabase()
                        );
                        updateList();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                return true;
            }
        });
    }

    private void showDateOnTv()
    {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(NAME_OF_DAY); // use 'E' for short abbreviation (Mon, Tues, etc)
        String strEnglish = fmt.print(dateTime);
        fmt = DateTimeFormat.forPattern(NAME_OF_MONTH);
        String name_of_month = fmt.print(dateTime);

        String todays_date = "" + dateTime.getDayOfMonth() + " " + name_of_month;

        dateFormatted = strEnglish + ", " + todays_date;

        tvDate.setText(dateFormatted);


        updateList();

    }

    private void updateList() {

        dailyExpenseEntryList = DailyExpenseEntry.DBCommands.getDailyExpenseDetails(dateFormatted, myDatabase.getReadableDatabase());

        double total = 0;

        for (DailyExpenseEntry e : dailyExpenseEntryList) {
            total = total + e.getSpentAmount();
        }

        tvDEATotal.setText("Total : " + total + " " + getResources().getString(R.string.rupee_symbol));

        DailyExpenseEntryAdapter dailyExpenseEntryAdapter
                = new DailyExpenseEntryAdapter(mCtx, dailyExpenseEntryList);

        lvDEAList.setAdapter(dailyExpenseEntryAdapter);
    }

    private void initViews()
    {

        ibPrev = (ImageButton) findViewById(R.id.ibPrev);
        tvDate = (TextView) findViewById(R.id.tvMonth);
        ibNext = (ImageButton) findViewById(R.id.ibNext);

        llDEAAddSpendEntry = (LinearLayout) findViewById(R.id.llDEAAddSpendEntry);
        actvDEASpentOn = (AutoCompleteTextView) findViewById(R.id.actvDEASpentOn);
        etDEASpentAmount = (EditText) findViewById(R.id.etDEASpentAmount);
        bDEASave = (Button) findViewById(R.id.bDEASave);
        lvDEAList = (ListView) findViewById(R.id.lvDEAList);
        ibDEAExpandList = (ImageButton) findViewById(R.id.ibDEAExpandList);

        tvDEATotal = findViewById(R.id.tvDEATotal);

    }
}