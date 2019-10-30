package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

/**
 * Created by Jayus on 31/07/2018.
 */

public class SetupPresenterImp implements SetupPresenter {

    /**
     * The session manager
     */
    SessionManager session;

    /**
     * The Setup View
     */
    SetupView setupView;

    /**
     * The Context
     */
    Context context;

    /**
     *
     * @param setupView
     * @param context
     */
    public SetupPresenterImp(SetupView setupView, Context context){
        this.setupView = setupView;
        this.context = context;
        session = new SessionManager(context);
    }

    @Override
    public void setup(String hostName, String port, boolean ssl) {

        if(TextUtils.isEmpty(hostName) || TextUtils.isEmpty(port)) {
            setupView.showValidationSetupError();
        }
        else {
            session.createSessionSetup(hostName, port, ssl);
            setupView.setupSuccess("Your configuration have been saved");
        }
    }
}
