package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface INotifications {

    void onSuccess(JSONObject response);
    void onFailed(JSONObject failed);
    void onError(VolleyError error);

}
