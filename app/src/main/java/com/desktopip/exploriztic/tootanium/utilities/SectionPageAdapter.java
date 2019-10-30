package com.desktopip.exploriztic.tootanium.utilities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jayus on 17/08/2018.
 */

/**
 * Class that stored fragments for tag
 */
public class SectionPageAdapter extends FragmentPagerAdapter {

    private static final String TAG = "SectionPageAdapter";
    private final List<Fragment> fragmentList = new ArrayList<>();

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }
}
