package com.desktopip.exploriztic.tootanium.activities;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.IntroViewPagerAdapter;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class Introduction extends AppCompatActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {

    private ViewPager introViewPager;
    private int[] slideLayout = {R.layout.a_slide_intro_1, R.layout.a_slide_intro_2, R.layout.a_slide_intro_3, R.layout.a_slide_intro_4};

    private IntroViewPagerAdapter viewPagerAdapter;

    private LinearLayout introDotsLayout;
    private ImageView[] dots;

    private Button btnIntroSkip, btnIntroNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission();
        if (new SessionManager(this).checkSessionIntro()) {
            finishToLogin();
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.a_introduction);

        introViewPager = findViewById(R.id.introViewPager);
        introDotsLayout = findViewById(R.id.introDotsLayout);
        btnIntroSkip = findViewById(R.id.btnIntroSkip);
        btnIntroNext = findViewById(R.id.btnIntroNext);
        viewPagerAdapter = new IntroViewPagerAdapter(slideLayout, this);
        introViewPager.setAdapter(viewPagerAdapter);

        createDots(0);

        introViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position == slideLayout.length - 1) {
                    btnIntroNext.setText("Start");
                    btnIntroSkip.setVisibility(View.INVISIBLE);
                } else {
                    btnIntroNext.setText("Next");
                    btnIntroSkip.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        btnIntroNext.setOnClickListener(this);
        btnIntroSkip.setOnClickListener(this);
    }

    private void createDots(int currentPosition) {
        if (introDotsLayout != null) {
            introDotsLayout.removeAllViews();
        }

        dots = new ImageView[slideLayout.length];

        for (int i = 0; i < slideLayout.length; i++) {
            dots[i] = new ImageView(this);
            if (i == currentPosition) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.intro_active_dot));
            } else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.intro_default_dot));
            }

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT
                    , ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(4, 0, 4, 0);
            introDotsLayout.addView(dots[i], layoutParams);
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        switch (id) {
            case R.id.btnIntroNext:
                nextIntroSlide();
                break;
            case R.id.btnIntroSkip:
                finishToLogin();
                new SessionManager(this).createSessionIntro();
                break;
        }
    }

    private void finishToLogin() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void nextIntroSlide() {
        int nextSlide = introViewPager.getCurrentItem() + 1;
        if(nextSlide < slideLayout.length) {
            introViewPager.setCurrentItem(nextSlide);
        } else {
            finishToLogin();
            new SessionManager(this).createSessionIntro();
        }
    }

    @AfterPermissionGranted(123)
    private boolean checkPermission() {
        String[] perms = {
                Manifest.permission.ACCESS_COARSE_LOCATION
                , Manifest.permission.ACCESS_FINE_LOCATION
                , Manifest.permission.ACCESS_NETWORK_STATE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.CAMERA
        };
        if (EasyPermissions.hasPermissions(this, perms)) {
            return true;
        } else {
            EasyPermissions.requestPermissions(this, "We need permissions because this apps should be access your local storage",
                    123, perms);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }
}
