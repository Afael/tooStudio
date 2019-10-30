package com.desktopip.exploriztic.tootanium.fragment;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.SpatialAppCompat;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class FragMainOnlineStorage extends BottomSheetDialogFragment implements
        NavigationView.OnNavigationItemSelectedListener{

    View view;

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    Fragment fragment = null;

    Toolbar feMainToolbar;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if(inflater != null){

            CalligraphyConfig.initDefault(new CalligraphyConfig
                    .Builder()
                    .setDefaultFontPath("fonts/ClearSans-Medium.ttf")
                    .setFontAttrId(R.attr.fontPath)
                    .build());

            view = inflater.inflate(R.layout.fe_main_activity, container, false);

            init();
            if (savedInstanceState == null) {
                fragment = new FragFileExplore();
                callFragment(fragment);
            }

        }

        return view;
    }

    void init(){

        feMainToolbar = view.findViewById(R.id.fe_main_toolbar);
        feMainToolbar.setTitle("Exploriztic");

        SpatialAppCompat.setActionBarColor(feMainToolbar, getContext());

        ((AppCompatActivity) getActivity()).setSupportActionBar(feMainToolbar);
        setHasOptionsMenu(true);

        fragmentManager = getChildFragmentManager();
    }

    public void callFragment(Fragment fragment) {
        if (fragment != null) {
            fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.remove(fragment);
            fragmentTransaction.replace(R.id.fe_frame_container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Untuk handle onActivityResult dari fragment
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment != null) {
                //Log.d("thread", ""+fragment);
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_file_explorer:
                fragment = new FragUploadChannel();
                callFragment(fragment);
                //mainToolbar.setTitle("Upload Channel");
                break;
            case R.id.nav_upload_channel:
                fragment = new FragUploadChannel();
                callFragment(fragment);
                //mainToolbar.setTitle("Upload Channel");
                break;
            case R.id.nav_download_channel:
                fragment = new FragDownloadChannel();
                callFragment(fragment);
                //mainToolbar.setTitle("Download Channel");
                break;
            case R.id.nav_download:
                fragment = new FragFileExplorerDownloaded();
                callFragment(fragment);
                //mainToolbar.setTitle("Download Channel");
                break;
            case R.id.nav_app_channel:
                fragment = new FragAppsChannel();
                callFragment(fragment);
                //mainToolbar.setTitle("App Channel");
                break;
            case R.id.nav_advertise:
                fragment = new FragGroup();
                callFragment(fragment);
                //mainToolbar.setTitle("Download Channel");
                break;
        }
        //drawerLayout = view.findViewById(R.id.fe_drawer_layout);
        //drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        feMainToolbar.getMenu().clear();
        feMainToolbar.inflateMenu(R.menu.menu_main_fe);
        for (int i = 0; i < feMainToolbar.getMenu().size(); i++) {
            Drawable drawable = feMainToolbar.getMenu().getItem(i).getIcon();
            drawable.mutate();
            drawable.setColorFilter(getResources().getColor(R.color.color_white), PorterDuff.Mode.SRC_IN);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_file_explorer:
                fragment = new FragFileExplore();
                callFragment(fragment);
                return true;
            case R.id.nav_upload_channel:
                fragment = new FragUploadChannel();
                callFragment(fragment);
                return true;
            case R.id.nav_download_channel:
                fragment = new FragDownloadChannel();
                callFragment(fragment);
                return true;
            case R.id.nav_app_channel:
                fragment = new FragAppsChannel();
                callFragment(fragment);
                return true;
            case R.id.nav_download:
                fragment = new FragFileExplorerDownloaded();
                callFragment(fragment);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
