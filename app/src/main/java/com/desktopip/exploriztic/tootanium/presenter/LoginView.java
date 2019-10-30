package com.desktopip.exploriztic.tootanium.presenter;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Jayus on 23/07/2018.
 */

public interface LoginView {
    /**
     * Menampilkan notifikasi validation error
     */
    void showValidationError();

    /**
     * Login success
     */
    void loginSuccess(JSONObject result);

    /**
     * Login failed
     */
    void loginFailed(JSONObject failed);

    /**
     * Menampilkan notifikasi login error
     */
    void loginError(VolleyError volleyError);
}
