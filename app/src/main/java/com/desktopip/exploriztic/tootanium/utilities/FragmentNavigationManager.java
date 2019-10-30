package com.desktopip.exploriztic.tootanium.utilities;

import android.support.v4.app.FragmentManager;

import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.interfaces.INavigationManager;

public class FragmentNavigationManager implements INavigationManager {

    private static FragmentNavigationManager mInstance;

    private FragmentManager fragmentManager;

    private MainActivity mainActivity;

    public static FragmentNavigationManager getmInstance(MainActivity mainActivity) {
        if(mInstance == null)
             mInstance = new FragmentNavigationManager();
        mInstance.configure(mainActivity);
        return mInstance;
    }

    private void configure(MainActivity mainActivity) {
        mainActivity = mainActivity;
        fragmentManager = mainActivity.getSupportFragmentManager();
    }

    @Override
    public void showFragment(String title) {

    }
}
