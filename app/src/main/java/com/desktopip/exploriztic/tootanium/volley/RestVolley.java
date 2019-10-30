package com.desktopip.exploriztic.tootanium.volley;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RestVolley extends JsonObjectRequest {

    private SessionManager sessionManager;
    private HashMap<String, String> user;
    private String userId, token;

    public RestVolley(Context ctx, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(url, jsonRequest, listener, errorListener);

        sessionManager = new SessionManager(ctx);
        user = sessionManager.getUserDetails();
        userId = user.get(SessionManager.KEY_USERNAME);

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                Log.d("token_error", error);
            }
        }, ctx, userId);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put("Content-Type", "application/json");
        params.put("Authorization", "Bearer " + token);
        return params;
    }
}
