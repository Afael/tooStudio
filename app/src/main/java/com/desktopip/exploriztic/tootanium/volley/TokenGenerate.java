package com.desktopip.exploriztic.tootanium.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IRequestResult;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.NetworkManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jayus on 07/07/2018.
 */

public class TokenGenerate {

    private static String token;

    public static String generateToken(final Context context, String userId) {

        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        // Base url API
        String urlAPI = user.get(SessionManager.BASE_URL_API);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            rawJson.put("name", "generateToken");
            param.put("user_id", userId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject = null;
                String status = response.toString();
                try {
                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        String error = jsonObject.getString("message");
                        //iToken.onError(error);
                    } else {
                        jsonObject = new JSONObject(response.getString("response"));
                        JSONObject message = jsonObject.getJSONObject("message");

                        token = message.getString("token");
                        //iToken.onSuccess(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d("TOKEN ", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        NetworkManager.getInstance(context).addToRequestQueue(jsonObjectRequest);

        return token;
    }

    public static void generate(final IToken iToken, final Context context, String userId) {

        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        // Base url API
        String urlAPI = user.get(SessionManager.BASE_URL_API);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            rawJson.put("name", "generateToken");
            param.put("user_id", userId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONObject jsonObject = null;
                String status = response.toString();
                try {
                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        String error = jsonObject.getString("message");
                        iToken.onError(error);
                    } else {
                        jsonObject = new JSONObject(response.getString("response"));
                        JSONObject message = jsonObject.getJSONObject("message");

                        String token = message.getString("token");
                        iToken.onSuccess(token);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.d("TOKEN ", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    private static void handlingError(VolleyError error) {
        if(error instanceof NoConnectionError){
            Log.d("VOLLEY_ERROR", "ERROR_CONNECTION_TOKEN_SERVICE");
        }else if (error instanceof TimeoutError) {
            Log.d("VOLLEY_ERROR", "ERROR_TIMEOUT_CONNECTION_TOKEN_SERVICE");
        } else if (error instanceof AuthFailureError) {
            Log.d("VOLLEY_ERROR","ERROR_AUTH_TOKEN_SERVICE");
        } else if (error instanceof ServerError) {
            Log.d("VOLLEY_ERROR", "ERROR_SERVER_TOKEN_SERVICE");
        } else if (error instanceof NetworkError) {
            Log.d("VOLLEY_ERROR", "ERROR_NETWORK_TOKEN_SERVICE");
        } else if (error instanceof ParseError) {
            Log.d("VOLLEY_ERROR", "ERROR_PARSE_TOKEN_SERVICE");
        }
    }

}
