package com.desktopip.exploriztic.tootanium.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IChannel;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Jayus on 12/07/2018.
 */

public class ChannelServices {

    final static String[] token = new String[1];
    static String errorToken;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String urlAPI;
    private static String baseUrl;

    public static void loadChannel(final IChannel iChannel, final Context context, String apiName
            , String userName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token[0] = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            byte[] bytUrl = baseUrl.getBytes("UTF-8");
            String urlBase64 = Base64.encodeToString(bytUrl, Base64.DEFAULT);
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("url", urlBase64);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertDialog.dismiss();

                        JSONObject jsonObject = null;

                        String status = response.toString();
                        try {

                            if (status.contains("error")) {
                                jsonObject = new JSONObject(response.getString("error"));
                                iChannel.onSuccess(jsonObject);
                            }
                            jsonObject = new JSONObject(response.getString("response"));
                            iChannel.onSuccess(jsonObject);

                        } catch (JSONException e) {
                            alertDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iChannel.onError(volleyError);
            }
        }) { //no semicolon or coma
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token[0]);

                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void setChannelActive(final IChannel iChannel, final Context context
            , String apiName, String id, String strDate, String userName){

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token[0] = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            byte[] primary = id.getBytes("UTF-8");
            byte[] bDate = strDate.getBytes("UTF-8");
            String primaryBase64 = Base64.encodeToString(primary, Base64.DEFAULT);
            String dateBase64 = Base64.encodeToString(bDate, Base64.DEFAULT);
            rawJson.put("name", apiName);
            param.put("id", primaryBase64);
            param.put("date", dateBase64);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iChannel.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iChannel.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iChannel.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token[0]);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void setChannelDelDeactExp(final IChannel iChannel, final Context context, String apiName, String id, String userName){

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token[0] = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {

            byte[] primary = id.getBytes("UTF-8");
            String primaryBase64 = Base64.encodeToString(primary, Base64.DEFAULT);
            rawJson.put("name", apiName);
            param.put("id", primaryBase64);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iChannel.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iChannel.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iChannel.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token[0]);
                return params;
            }
        };
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
