package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface IAdvertise {

    void onSuccess(JSONObject result);
    void onError(VolleyError error);

}
