package com.desktopip.exploriztic.tootanium.utilities;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

public class ActionBarHelper {

    public static void addUpButton(AppCompatActivity activity){
        ActionBar actionBar = activity.getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }
}
