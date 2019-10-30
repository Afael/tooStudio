package com.desktopip.exploriztic.tootanium.presenter;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface AdvCreateGroupView {

    /**
     * Menampilkan notifikasi validation error
     */
    void showValidationError();

    /**
     * Create group success
     */
    void createGroupSuccess(JSONObject result);

    /**
     * Create group failed
     */
    void createGroupFailed(JSONObject failed);

    /**
     * Menampilkan notifikasi Create group error
     */
    void createGroupError(VolleyError volleyError);

}
