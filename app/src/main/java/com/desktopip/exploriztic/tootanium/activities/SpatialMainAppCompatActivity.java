package com.desktopip.exploriztic.tootanium.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.SpartialColor;

@SuppressLint("Registered")
public class SpatialMainAppCompatActivity extends AppCompatActivity {

    protected int mColorActionBarId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        updateUI();
        super.onCreate(savedInstanceState);
    }



    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    protected void onResume() {
        updateUI();
        super.onResume();
    }

    protected SharedPreferences getMyPreference(){

        return getSharedPreferences(getString(R.string.share_preference_key),Context.MODE_PRIVATE);
    }

    protected void updateUI(){
        updateToolBar();
        updateWallpaper();
    }

    protected void updateToolBar(){

        SpartialColor spartialColor = SpartialColor.getColor(getBaseContext(), SpatialAppCompat.getValueContext(R.string.default_action_bar_color_key, R.string.system_default_action_bar_color_key, getBaseContext()));
        mColorActionBarId =spartialColor.colorResId();
        if(getSupportActionBar() != null){
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(mColorActionBarId));
        }
    }

    protected void updateWallpaper(){

    }

    protected void getProfileFromServer(){

    }

    protected void getPersonalizationFromServer(){

    }

    protected int getActionBarColor(){
        return mColorActionBarId;
    }

}
