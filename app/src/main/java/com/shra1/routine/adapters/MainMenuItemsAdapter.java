package com.shra1.routine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.shra1.routine.R;
import com.shra1.routine.mymodels.MainMenuItem;

import java.util.List;

public class MainMenuItemsAdapter extends BaseAdapter {
    Context mCtx;
    List<MainMenuItem> l;

    public MainMenuItemsAdapter(Context mCtx, List<MainMenuItem> mainMenuItems) {
        this.mCtx = mCtx;
        this.l = mainMenuItems;
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
        MMIAViewHolder h;
        if (row == null) {
            row = LayoutInflater.from(mCtx).inflate(R.layout.main_menu_item, parent, false);
            h = new MMIAViewHolder(row);
            row.setTag(h);
        } else {
            h = (MMIAViewHolder) row.getTag();
        }

        MainMenuItem d = (MainMenuItem) getItem(position);

        h.ivMMIIcon.setImageDrawable(mCtx.getResources().getDrawable(d.getIconResource()));

        h.tvMMITitle.setText(d.getTitle());

        if (d.getOnClickListener()!=null){
            row.setOnClickListener(d.getOnClickListener());
        }else{
            row.setOnClickListener(null);
        }

        return row;
    }

    static class MMIAViewHolder {
        private ImageView ivMMIIcon;
        private TextView tvMMITitle;

        public MMIAViewHolder(View v) {
            ivMMIIcon = (ImageView) v.findViewById(R.id.ivMMIIcon);
            tvMMITitle = (TextView) v.findViewById(R.id.tvMMITitle);
        }
    }
}
