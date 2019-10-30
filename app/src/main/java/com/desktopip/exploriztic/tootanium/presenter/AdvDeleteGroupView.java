package com.desktopip.exploriztic.tootanium.presenter;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface AdvDeleteGroupView {

    /**
     * Delete group success
     */
    void deleteGroupSuccess(JSONObject result);

    /**
     * Delete group failed
     */
    void deleteGroupFailed(JSONObject failed);

    /**
     * Menampilkan notifikasi Delete group error
     */
    void deleteGroupError(VolleyError volleyError);
}
