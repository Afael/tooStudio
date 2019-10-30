package com.desktopip.exploriztic.tootanium.utilities;

import android.app.Application;

/**
 * Created by Jayus on 05/07/2018.
 */

public class AppController extends Application {

    private static AppController mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        TypefaceUtil.overrideFont(getApplicationContext(), "SAN", "font/Roboto-Regular.ttf");

    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(CheckInternetConnection.ConnectivityReceiverListener listener) {
        CheckInternetConnection.connectivityReceiverListener = listener;
    }

}
