package com.desktopip.exploriztic.tootanium.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.desktopip.exploriztic.tootanium.utilities.ActionBarHelper;


public class SpatialSubAppCompatActivity extends SpatialMainAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        ActionBarHelper.addUpButton(this);
        super.onCreate(savedInstanceState);
    }
}
