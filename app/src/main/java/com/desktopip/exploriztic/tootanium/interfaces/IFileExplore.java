package com.desktopip.exploriztic.tootanium.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Jayus on 04/07/2018.
 */

public interface IFileExplore {
    void onSuccess(JSONObject result);
    void onError(VolleyError error);
}
