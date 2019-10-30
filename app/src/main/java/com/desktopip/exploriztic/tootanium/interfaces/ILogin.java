package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Jayus on 23/07/2018.
 */

public interface ILogin {
    void onSuccess(JSONObject result);
    void onFailed(JSONObject failed);
    void onError(VolleyError error);
}
