package com.shra1.routine.mymodels;

import android.view.View;

public class MainMenuItem {
    String Title;
    int IconResource;
    View.OnClickListener OnClickListener;

    public MainMenuItem() {
    }

    public MainMenuItem(String title, int iconResource, View.OnClickListener onClickListener) {
        Title = title;
        IconResource = iconResource;
        OnClickListener = onClickListener;
    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getIconResource() {
        return IconResource;
    }

    public void setIconResource(int iconResource) {
        IconResource = iconResource;
    }

    public View.OnClickListener getOnClickListener() {
        return OnClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        OnClickListener = onClickListener;
    }
}
