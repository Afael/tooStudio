package com.desktopip.exploriztic.tootanium.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.MainActivity;
import com.desktopip.exploriztic.tootanium.adapters.AppListAdapter;
import com.desktopip.exploriztic.tootanium.models.ModAppList;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragAppList extends BottomSheetDialogFragment {

    View view;
    private Context context;
    private List<FragCategoryApps> fragCategoryApps;
    private List<String> mTitle;
    private List<ModAppList.Template> templatesList;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ImageView iv_apps_desktop;
    private TextView tv_apps_desktop;

    private static FragAppList fragAppList;

    AppListAdapter appListAdapter;

    static SessionManager session;

    static String userName;

    static String password;

    String baseUrl;


    public FragAppList() {
        // Required empty public constructor
    }

    public static FragAppList newInstance(Context context, List<ModAppList.Template> templatesList){
        if(fragAppList == null){
            fragAppList = new FragAppList();
            fragAppList.context = context;
            fragAppList.templatesList = templatesList;
        }
        return fragAppList;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null){
            view = inflater.inflate(R.layout.frag_app_list, container, false);
            iv_apps_desktop = view.findViewById(R.id.iv_apps_desktop);
            tv_apps_desktop = view.findViewById(R.id.tv_apps_desktop);
            mTabLayout = view.findViewById(R.id.appsTab);
            mViewPager = view.findViewById(R.id.container_app_list);
            setupTabLayout();

            session = new SessionManager(context);
            HashMap<String, String> user = session.getUserDetails();
            userName = user.get(SessionManager.KEY_USERNAME);
            password = user.get(SessionManager.KEY_PASSWORD);
            baseUrl = user.get(SessionManager.BASE_URL);

            iv_apps_desktop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String baseParam = "apps-edit/?pt=";
                    FragWorkingStudio openTab = MainActivity.fragmentContainer;
                    openTab.openNewWebView(baseParam, tv_apps_desktop.getText().toString());
                }
            });
        }

        return view;
    }

    private void setupFragmentAppList(){
        fragCategoryApps = new ArrayList<>();
        mTitle = new ArrayList<>();
        if(templatesList != null && templatesList.size() > 0){
            for(int i = 0; i< templatesList.size(); i++){
                fragCategoryApps.add(FragCategoryApps.newInstance(context, templatesList.get(i).APPS_LIST));
                mTitle.add(templatesList.get(i).GROUP_NAME);
            }
        }
    }

    public void setupTabLayout(){
        setupFragmentAppList();
        appListAdapter = new AppListAdapter(context, fragCategoryApps, mTitle, getChildFragmentManager());
        mViewPager.setAdapter(appListAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

}
