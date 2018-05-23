package com.shra1.routine.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.mymodels.TiffinEntry;

import org.joda.time.DateTime;

import java.util.List;

import static com.shra1.routine.utils.Constants.TIFFIN_TAKEN_YES;

public class TiffinEntryDetailsAdapter extends BaseAdapter {
    Context mCtx;
    List<TiffinEntry> l;

    public TiffinEntryDetailsAdapter(Context mCtx, List<TiffinEntry> l) {
        this.mCtx = mCtx;
        this.l = l;
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
        TEDAViewHolder h;
        if (row == null) {
            row = LayoutInflater.from(mCtx).inflate(R.layout.tiffin_entry_details_list_item_layout, parent, false);
            h = new TEDAViewHolder(row);
            row.setTag(h);
        } else {
            h = (TEDAViewHolder) row.getTag();
        }
        TiffinEntry d = (TiffinEntry) getItem(position);

        h.tvTEDLILDate.setText(d.getFormattedDate());

        h.tvTEDLILStatus.setText(d.getTiffinTaken());

        if (d.getTiffinTaken().equals(TIFFIN_TAKEN_YES)) {
            h.tvTEDLILStatus.setTextColor(Color.GREEN);
        } else {
            h.tvTEDLILStatus.setTextColor(Color.RED);
        }

        DateTime dateTime = new DateTime(d.getEntryOnEpoch());

        h.tvTEDLILEntryOn.setText(dateTime.toString("hh:mm:ss aa"));

        return row;
    }

    static class TEDAViewHolder {
        private TextView tvTEDLILDate;
        private TextView tvTEDLILStatus;
        private TextView tvTEDLILEntryOn;

        public TEDAViewHolder(View v) {
            tvTEDLILDate = (TextView) v.findViewById(R.id.tvTEDLILDate);
            tvTEDLILStatus = (TextView) v.findViewById(R.id.tvTEDLILStatus);
            tvTEDLILEntryOn = (TextView) v.findViewById(R.id.tvTEDLILEntryOn);
        }
    }
}
