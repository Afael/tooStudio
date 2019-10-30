package com.desktopip.exploriztic.tootanium.presenter;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface AdvCreateAdvertiseFolderView {

    /**
     * Menampilkan notifikasi validation error
     */
    void showValidationError();

    /**
     * Create advertise success
     */
    void createAdvertiseSuccess(JSONObject result);

    /**
     * Create advertise failed
     */
    void createAdvertiseFailed(JSONObject failed);

    /**
     * Menampilkan notifikasi Create advertise error
     */
    void createAdvertiseError(VolleyError volleyError);

}
