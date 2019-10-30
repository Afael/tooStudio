package com.desktopip.exploriztic.tootanium.presenter;

/**
 * Created by Jayus on 23/07/2018.
 */

public interface LoginPresenter {
    /**
     *
     * @param username
     * @param password
     */
    void login(
            String username
            , String password
            , String ipaddress
            , String latitude
            , String longitude
            , String hostName
            , String port
            , boolean ssl
    );
}
