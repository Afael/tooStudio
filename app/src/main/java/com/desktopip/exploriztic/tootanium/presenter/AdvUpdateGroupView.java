package com.desktopip.exploriztic.tootanium.presenter;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface AdvUpdateGroupView {
    /**
     * Menampilkan notifikasi validation error
     */
    void showValidationError();

    /**
     * Update group success
     */
    void updateGroupSuccess(JSONObject result);

    /**
     * Update group failed
     */
    void updateGroupFailed(JSONObject failed);

    /**
     * Menampilkan notifikasi Update group error
     */
    void updateGroupError(VolleyError volleyError);
}
