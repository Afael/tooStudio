package com.desktopip.exploriztic.tootanium.fragment;


import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.desktopip.exploriztic.tootanium.R;

public class FragFileExplorerMain extends BottomSheetDialogFragment{

    View view;

    Toolbar feMainToolbar;

    public FragFileExplorerMain() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(inflater != null){
            view = inflater.inflate(R.layout.fe_main_activity, container, false);
            init(view);
        }

        return view;
    }

    private void init(View view){
        feMainToolbar = view.findViewById(R.id.main_toolbar);
        feMainToolbar.setTitle(getString(R.string.app_name));
    }

}
