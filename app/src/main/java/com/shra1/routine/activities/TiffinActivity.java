package com.shra1.routine.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.TiffinEntry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static com.shra1.routine.utils.Constants.NAME_OF_DAY;
import static com.shra1.routine.utils.Constants.NAME_OF_MONTH;
import static com.shra1.routine.utils.Constants.TIFFIN_TAKEN_NO;
import static com.shra1.routine.utils.Constants.TIFFIN_TAKEN_YES;
import static com.shra1.routine.utils.Constants.getDateFormatted;

public class TiffinActivity extends AppCompatActivity {

    String dateFormatted;
    MyDatabase myDatabase;
    private Context mCtx;

    //ACTIVITY WIDGETS
    private TextView tvTADidYou;
    private Button bTAYes;
    private Button bTANo;
    private Button bTAViewAll;

    private View.OnClickListener yesButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TiffinEntry tiffinEntry = new TiffinEntry(dateFormatted, TIFFIN_TAKEN_YES, System.currentTimeMillis());
            TiffinEntry.DBCommands.addTiffinEntry(tiffinEntry, myDatabase.getWritableDatabase());
            disableBothButtons();
        }
    };

    private View.OnClickListener noButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TiffinEntry tiffinEntry = new TiffinEntry(dateFormatted, TIFFIN_TAKEN_NO, System.currentTimeMillis());
            TiffinEntry.DBCommands.addTiffinEntry(tiffinEntry, myDatabase.getWritableDatabase());
            disableBothButtons();
        }
    };

    private View.OnClickListener viewAllButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent viewAllIntent = new Intent(mCtx, TiffinEntryDetailsActivity.class);
            startActivity(viewAllIntent);
        }
    };


    private void disableBothButtons() {
        bTAYes.setEnabled(false);
        bTANo.setEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin);

        initViews();

        mCtx = this;
        myDatabase = MyDatabase.getInstance(mCtx);

        DateTime dt = new DateTime();
        dateFormatted = getDateFormatted(dt);

        String todaysSentence = "Its " + dateFormatted + " Today.\n" + mCtx.getResources().getString(R.string.did_you_take_the_tiffin_today);

        tvTADidYou.setText(todaysSentence);

        checkTodaysEntryDone();

        bTAYes.setOnClickListener(yesButtonClickListener);
        bTANo.setOnClickListener(noButtonClickListener);
        bTAViewAll.setOnClickListener(viewAllButtonClickListener);
    }

    private void checkTodaysEntryDone() {
        TiffinEntry tiffinEntry = TiffinEntry.DBCommands.getTiffinEntryByFormattedToday(dateFormatted, myDatabase.getReadableDatabase());

        if (tiffinEntry != null) {
            disableBothButtons();

            String todaysSentence = "Its " + dateFormatted + " Today.\n";

            if (tiffinEntry.getTiffinTaken().equals(TIFFIN_TAKEN_YES)) {
                todaysSentence = todaysSentence + "You have Taken Tiffin.";
            } else {
                todaysSentence = todaysSentence + "You have Not Taken Tiffin.";
            }

            tvTADidYou.setText(todaysSentence);
        }
    }

    private void initViews() {
        tvTADidYou = findViewById(R.id.tvTADidYou);
        bTAYes = (Button) findViewById(R.id.bTAYes);
        bTANo = (Button) findViewById(R.id.bTANo);
        bTAViewAll = (Button) findViewById(R.id.bTAViewAll);
    }

}
