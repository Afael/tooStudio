package com.desktopip.exploriztic.tootanium.customisable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomTab extends TabLayout implements TabLayout.OnTabSelectedListener {

    int pos = 0;
    int viewPos;
    private FragmentActivity myContext;
    private ViewPager viewPager;


    public CustomTab(Context context) {
        super(context);
        myContext = (FragmentActivity) context;
    }

    public CustomTab(Context context, AttributeSet attrs) {
        super(context, attrs);
        myContext = (FragmentActivity) context;
    }

    public CustomTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        myContext = (FragmentActivity) context;
    }

    @Override
    public void addTab(@NonNull Tab tab) {
        setTab(tab);
        super.addTab(tab);
    }

    @Override
    public void addTab(@NonNull Tab tab, int position) {
        setTab(tab);
        super.addTab(tab, position);
    }

    @Override
    public void addTab(@NonNull Tab tab, boolean setSelected) {
        setTab(tab);
        super.addTab(tab, setSelected);
    }

    @Override
    public void addTab(@NonNull Tab tab, int position, boolean setSelected) {
        setTab(tab);
        super.addTab(tab, position, setSelected);
    }

    private void setTab(final Tab tab) {

        if (tab == null) {
            return;
        }

        final String myTitle = tab.getText().toString();
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = inflater.inflate(R.layout.tab_custom_view, this, false);
        TextView title = view.findViewById(R.id.title);
        TextView tab_close = view.findViewById(R.id.tab_close);
        title.setText(myTitle);
        tab.setCustomView(view);

        tab_close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final int getTabPosition = tab.getPosition();



            }
        });

        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTabSelected(tab);
            }
        });

    }

    private void popupRemoveTab(View view) {

        String popText[] = {"Close"};
        int popImage[] = {R.drawable.ic_close_white_24dp};

        // SimpleAdapter list item key.
        final String LIST_ITEM_KEY_IMAGE = "image";
        final String LIST_ITEM_KEY_TEXT = "text";

        List<Map<String, Object>> itemList = new ArrayList<>();
        int itemLen = popText.length;
        for (int i = 0; i < itemLen; i++) {
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put(LIST_ITEM_KEY_IMAGE, popImage[i]);
            itemMap.put(LIST_ITEM_KEY_TEXT, popText[i]);

            itemList.add(itemMap);
        }

        // Create SimpleAdapter that will be used by short message list view.
        SimpleAdapter simpleAdapter = new SimpleAdapter(getContext()
                , itemList
                , R.layout.activity_popup_window_tab,
                new String[]{LIST_ITEM_KEY_IMAGE, LIST_ITEM_KEY_TEXT},
                new int[]{R.id.popListItemImage, R.id.popupListItemText});
        // Get short message popup view.
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.activity_popup_window_tab, null);
        ListView smsListView = popupView.findViewById(R.id.popupWindowMenuList);
        smsListView.setAdapter(simpleAdapter);
        smsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int itemIndex, long l) {

            }
        });

        // Create popup window.
        PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // Set popup window animation style.
        popupWindow.setAnimationStyle(R.style.popup_window_animation_tab_close);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));

        popupWindow.setFocusable(true);

        popupWindow.setOutsideTouchable(true);

        popupWindow.update();

        // Show popup window offset 1,1 to smsBtton.
        popupWindow.showAsDropDown(view, 1, 1);
    }


    public void setViewPager(ViewPager mViewPager) {
        this.viewPager = mViewPager;
    }

    @Override
    public void onTabSelected(Tab tab) {

        final int getTabPosition = tab.getPosition();
        final int getTabCount = getTabCount() - 1;
        viewPos = getTabCount - getTabPosition;
        viewPager.setCurrentItem(viewPos);
        pos = getTabPosition;
    }

    @Override
    public void onTabUnselected(Tab tab) {

    }

    @Override
    public void onTabReselected(Tab tab) {

    }
}
