package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;

public class NewTabPresenterImp implements NewTabPresenter {

    NewTabView newTabView;

    Context context;

    public  NewTabPresenterImp(Context context, NewTabView newTabView){
        this.context = context;
    }

    @Override
    public void createNewTab(String param, String fileName) {
        newTabView.createNewTab(param, fileName);
    }
}
