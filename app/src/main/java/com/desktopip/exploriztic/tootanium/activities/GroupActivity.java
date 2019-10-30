package com.desktopip.exploriztic.tootanium.activities;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupAdvertiseFolder;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupChatRoom;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupNotifications;
import com.desktopip.exploriztic.tootanium.fragment.FragGroupUser;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
//import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.HashMap;

public class GroupActivity extends AppCompatActivity implements AHBottomNavigation.OnTabSelectedListener
        , View.OnClickListener {

    private static final String TAG = "GroupActivity";
    private static final int ACTIVITY_NUM = 0;

    AHBottomNavigation bottomNavigation;
    FragmentManager fragmentManager;

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;
    String sGroupId;
    String sGroupName;
    String sGroupAccessId;

    TextView group_user_cancel_action, adv_group_name_page;

    private Context advertiseGroupContext = GroupActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_group);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupName = getIntent().getStringExtra("groupName");
        sGroupAccessId = getIntent().getStringExtra("groupAccessId");

        group_user_cancel_action = findViewById(R.id.group_user_cancel_action);
        adv_group_name_page = findViewById(R.id.adv_group_name_page);

        group_user_cancel_action.setOnClickListener(this);

        bottomNavigation = findViewById(R.id.bottomBar);
        bottomNavigation.setOnTabSelectedListener(this);
        fragmentManager = getSupportFragmentManager();

        //setupViewPager();
        createNavItems();

        adv_group_name_page.setText(sGroupName);

    }

    /**
     * Responsive adding 3 tabs
     */
    /*private void setupViewPager() {
        SectionPageAdapter sectionPageAdapter = new SectionPageAdapter(getSupportFragmentManager());
        sectionPageAdapter.addFragment(new FragGroupUser(sGroupId, sGroupName));
        sectionPageAdapter.addFragment(new FragGroupAdvertiseFolder());
        sectionPageAdapter.addFragment(new FragGroupChatRoom());
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(sectionPageAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_group_user);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_folder_black_24dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat);
    }*/

    /**
     * BottomNavigationView setup
     */
    private void createNavItems() {
        //CREATE ITEMS
        AHBottomNavigationItem users = new AHBottomNavigationItem("Members", R.drawable.ic_group_user);
        AHBottomNavigationItem fileExplorer = new AHBottomNavigationItem("File Explorer", R.drawable.ic_folder_black_24dp);
        AHBottomNavigationItem chat = new AHBottomNavigationItem("Chat", R.drawable.ic_chat);
        AHBottomNavigationItem notification = new AHBottomNavigationItem("Notification", R.drawable.ic_notification);

        //ADD THEM TO BAR
        bottomNavigation.addItem(users);
        bottomNavigation.addItem(fileExplorer);
        bottomNavigation.addItem(chat);
        //bottomNavigation.addItem(notification);

        //SET PROPERTIES
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));

        //SET CURRENT ITEM
        bottomNavigation.setCurrentItem(0);
    }

//    private void setupBottomNavigationView() {
//        Log.d(TAG, "setupBottomNavigationView: setting up");
//        BottomNavigationViewEx bottomNavigationViewEx = (BottomNavigationViewEx) findViewById(R.id.bottomNavViewBar);
//        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
//        BottomNavigationViewHelper.enableNavigation(advertiseGroupContext, bottomNavigationViewEx);
//        Menu menu = bottomNavigationViewEx.getMenu();
//        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
//        menuItem.setChecked(true);
//    }

    @Override
    public void onTabSelected(int position, boolean wasSelected) {
        if (position == 0) {
            FragGroupUser fragGroupUser = new FragGroupUser(sGroupId, sGroupName);
            fragmentManager.beginTransaction().replace(R.id.relLayoutContain, fragGroupUser).commit();
        } else if (position == 1) {
            FragGroupAdvertiseFolder fragGroupAdvertiseFolder = new FragGroupAdvertiseFolder(sGroupId, sGroupAccessId);
            fragmentManager.beginTransaction().replace(R.id.relLayoutContain, fragGroupAdvertiseFolder).commit();
        } else if (position == 2) {
            FragGroupChatRoom fragGroupChatRoom = new FragGroupChatRoom();
            fragmentManager.beginTransaction().replace(R.id.relLayoutContain, fragGroupChatRoom).commit();
        } else if (position == 3) {
            FragGroupNotifications fragGroupNotifications = new FragGroupNotifications();
            fragmentManager.beginTransaction().replace(R.id.relLayoutContain, fragGroupNotifications).commit();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.group_user_cancel_action:
                finish();
                break;
        }
    }
}
