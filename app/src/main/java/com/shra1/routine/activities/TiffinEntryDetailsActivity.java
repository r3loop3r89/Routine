package com.shra1.routine.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.adapters.TiffinEntryDetailsAdapter;
import com.shra1.routine.database.MyDatabase;
import com.shra1.routine.mymodels.TiffinEntry;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;

import static com.shra1.routine.utils.Constants.NAME_OF_MONTH;

public class TiffinEntryDetailsActivity extends AppCompatActivity {

    DateTime dateTime;
    MyDatabase myDatabase;
    private Context mCtx;
    //ACTIVITY WIDGETS
    private ImageButton ibTDAPrev;
    private TextView tvTDAMonth;
    private ImageButton ibTDANext;
    private ListView lvTDAList;
    private View.OnClickListener nextIButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = dateTime.plusMonths(1);
            showDateOnTV();
        }
    };
    private View.OnClickListener prevIButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = dateTime.minusMonths(1);
            showDateOnTV();
        }
    };
    private View.OnClickListener resetMonthTVClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dateTime = new DateTime();
            showDateOnTV();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiffin_details);

        mCtx = this;
        myDatabase = MyDatabase.getInstance(mCtx);

        initViews();

        dateTime = new DateTime();


        tvTDAMonth.setOnClickListener(resetMonthTVClickListener);

        ibTDANext.setOnClickListener(nextIButtonClickListener);

        ibTDAPrev.setOnClickListener(prevIButtonClickListener);

        showDateOnTV();

    }

    private void showDateOnTV() {
        DateTimeFormatter fmt = DateTimeFormat.forPattern(NAME_OF_MONTH);
        String monthName = fmt.print(dateTime);
        tvTDAMonth.setText(monthName);

        List<TiffinEntry> l = TiffinEntry.DBCommands.getTiffinEntriesForMonth(dateTime, myDatabase.getReadableDatabase());

        TiffinEntryDetailsAdapter adapter = new TiffinEntryDetailsAdapter(mCtx, l);

        lvTDAList.setAdapter(adapter);
    }

    private void initViews() {
        ibTDAPrev = (ImageButton) findViewById(R.id.ibPrev);
        tvTDAMonth = (TextView) findViewById(R.id.tvMonth);
        ibTDANext = (ImageButton) findViewById(R.id.ibNext);
        lvTDAList = (ListView) findViewById(R.id.lvTDAList);
    }


}
