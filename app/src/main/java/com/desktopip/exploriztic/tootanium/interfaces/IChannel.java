package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Jayus on 12/07/2018.
 */

public interface IChannel {

    void onSuccess(JSONObject response);
    void onError(VolleyError error);
}
