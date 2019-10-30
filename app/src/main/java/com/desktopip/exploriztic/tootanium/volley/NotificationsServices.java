package com.desktopip.exploriztic.tootanium.volley;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.INotifications;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NotificationsServices {

    final static String[] token = new String[1];
    static String errorToken;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String urlAPI;
    private static String baseUrl;

    public static void loadNotifications(final INotifications iNotifications, final Context context, String apiName
            , String userName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        //final AlertDialog alertDialog = new SpotsDialog(context);
        //alertDialog.show();
        //alertDialog.setMessage("Please waiting...");

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
            rawJson.put("name", apiName);
            param.put("user", userName);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //alertDialog.dismiss();

                        JSONObject jsonObject = null;

                        String status = response.toString();
                        try {

                            if (status.contains("error")) {
                                jsonObject = new JSONObject(response.getString("error"));
                                iNotifications.onSuccess(jsonObject);
                            }
                            jsonObject = new JSONObject(response.getString("response"));
                            iNotifications.onSuccess(jsonObject);

                        } catch (JSONException e) {
                            //alertDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //alertDialog.dismiss();
                iNotifications.onError(volleyError);
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
}
