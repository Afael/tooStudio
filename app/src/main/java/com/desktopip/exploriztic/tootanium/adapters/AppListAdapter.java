package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.View;

import com.desktopip.exploriztic.tootanium.fragment.FragCategoryApps;

import java.util.List;

public class AppListAdapter extends FragmentPagerAdapter {

    private static List<String> mFragmentTitleList;

    private Context context;
    private List<FragCategoryApps> appLists;

    View view;

    public AppListAdapter(Context context, List<FragCategoryApps> appLists, List<String> mFragmentTitleList, FragmentManager fragmentManager){
        super(fragmentManager);
        this.context = context;
        this.appLists = appLists;
        this.mFragmentTitleList = mFragmentTitleList;

    }

    @Override
    public Fragment getItem(int position) {
        return appLists.get(position);
    }

    @Override
    public int getCount() {
        return appLists.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
