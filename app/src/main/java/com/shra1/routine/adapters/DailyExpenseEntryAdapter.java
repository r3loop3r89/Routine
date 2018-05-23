package com.shra1.routine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.mymodels.DailyExpenseEntry;

import org.joda.time.DateTime;

import java.util.List;

public class DailyExpenseEntryAdapter extends BaseAdapter {
    Context mCtx;
    List<DailyExpenseEntry> l;

    public DailyExpenseEntryAdapter(Context mCtx, List<DailyExpenseEntry> dailyExpenseEntryList) {
        this.mCtx = mCtx;
        this.l = dailyExpenseEntryList;
    }

    @Override
    public int getCount() {
        return l.size();
    }

    @Override
    public Object getItem(int position) {
        return l.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        DEEAViewHolder h;
        if (row == null) {
            row = LayoutInflater.from(mCtx).inflate(R.layout.daily_expense_entry_details_list_item_layout, parent, false);
            h = new DEEAViewHolder(row);
            row.setTag(h);
        } else {
            h = (DEEAViewHolder) row.getTag();
        }

        DailyExpenseEntry d = (DailyExpenseEntry) getItem(position);

        h.tvDEEDLILSpentOn.setText(d.getSpentOnName());

        DateTime dateTime = new DateTime(d.getEntryOnEpoch());

        h.tvDEEDLILEntryDate.setText("" + dateTime.toString("EEEE, dd, MMMM hh:mm:sss aa"));

        h.tvDEEDLILSpentAmount.setText("" + d.getSpentAmount() + " " + mCtx.getResources().getString(R.string.rupee_symbol));

        return row;
    }

    static class DEEAViewHolder {
        private TextView tvDEEDLILSpentOn;
        private TextView tvDEEDLILEntryDate;
        private TextView tvDEEDLILSpentAmount;

        public DEEAViewHolder(View v) {
            tvDEEDLILSpentOn = (TextView) v.findViewById(R.id.tvDEEDLILSpentOn);
            tvDEEDLILEntryDate = (TextView) v.findViewById(R.id.tvDEEDLILEntryDate);
            tvDEEDLILSpentAmount = (TextView) v.findViewById(R.id.tvDEEDLILSpentAmount);
        }
    }
}
