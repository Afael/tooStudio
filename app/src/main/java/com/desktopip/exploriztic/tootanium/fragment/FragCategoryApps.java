package com.desktopip.exploriztic.tootanium.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.FragCategoryAppAdapter;
import com.desktopip.exploriztic.tootanium.models.ModAppList;

import java.util.List;

public class FragCategoryApps extends Fragment {

    private Context context;
    private List<ModAppList.APPS> appsList;

    RecyclerView recyclerViewApp;
    View view;

    public FragCategoryApps() {
        // Required empty public constructor
    }

    public static FragCategoryApps newInstance(Context context, List<ModAppList.APPS> appsList){
        FragCategoryApps fragCategoryApps = new FragCategoryApps();
        fragCategoryApps.context = context;
        fragCategoryApps.appsList = appsList;
        return fragCategoryApps;
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
            view = inflater.inflate(R.layout.fragment_frag_category_apps, container, false);
            recyclerViewApp = view.findViewById(R.id.rc_category_apps);
            FragCategoryAppAdapter adapter = new FragCategoryAppAdapter(context, appsList);
            recyclerViewApp.setLayoutManager(new GridLayoutManager(context, 3));
            recyclerViewApp.setAdapter(adapter);
        }

        return view;
    }

}
