package com.desktopip.exploriztic.tootanium.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class WorkingStudioAdapter extends FragmentStatePagerAdapter {


    private ArrayList<Fragment> fragmentArrayList;

    private ArrayList<String> fragmentTitle;

    private FragmentManager fragmentManager;

    public WorkingStudioAdapter(FragmentManager fm) {
        super(fm);
        this.fragmentManager = fm;
        fragmentArrayList = new ArrayList<>();
        fragmentTitle = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentArrayList.add(fragment);
        fragmentTitle.add(title);
        notifyDataSetChanged();
    }

    public boolean removeFragment(int position) {

//        if ((position < 0) || (position >= getCount()) || (getCount()<=1)) {
//            return false;
//        }
//        else {
//            if (position == FragWorkingStudio.mViewPager.getCurrentItem()) {
//                if(position == (getCount()-1)) {
//                    FragWorkingStudio.mViewPager.setCurrentItem(position-1);
//                }
//                else if (position == 0){
//                    FragWorkingStudio.mViewPager.setCurrentItem(1);
//                }
//            }
//            Fragment mFragment = fragmentArrayList.get(position);
//            this.fragmentArrayList.remove(position);
//            this.fragmentTitle.remove(position);
//            this.notifyDataSetChanged();
//            fragmentManager.beginTransaction().detach(mFragment).commit();
//
//        }
        return true;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentArrayList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentArrayList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }

}
