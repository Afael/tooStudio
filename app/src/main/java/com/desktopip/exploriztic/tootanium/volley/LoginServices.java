package com.desktopip.exploriztic.tootanium.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.view.View;

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
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.interfaces.ILogin;
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
 * Created by Jayus on 23/07/2018.
 */

public class LoginServices {

    final static String[] token = new String[1];
    static String errorToken;

    public static void loginUser(final ILogin login, final Context context, final String apiName
            , final String userName, final String password, final String ip, final String latitude, final String longitude) {

        SessionManager sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetails();
        // Base url API
        final String urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        try {
            TokenGenerate.generate(new IToken() {
                @Override
                public void onSuccess(final String result) {
                    Log.d("TOKEN ", result);
                    token[0] = result;
                    JSONObject rawJson = new JSONObject();
                    JSONObject param = new JSONObject();

                    try {
                        byte[] bytUser = userName.getBytes("UTF-8");
                        String userBase64 = Base64.encodeToString(bytUser, Base64.DEFAULT);
                        byte[] bytPassword = password.getBytes("UTF-8");
                        String passwordBase64 = Base64.encodeToString(bytPassword, Base64.DEFAULT);
                        byte[] bytIp = ip.getBytes("UTF-8");
                        String ipBase64 = Base64.encodeToString(bytIp, Base64.DEFAULT);
                        byte[] bytLatitude = latitude.getBytes("UTF-8");
                        String latitudeBase64 = Base64.encodeToString(bytLatitude, Base64.DEFAULT);
                        byte[] bytLongitude = longitude.getBytes("UTF-8");
                        String longitudeBase64 = Base64.encodeToString(bytLongitude, Base64.DEFAULT);

                        rawJson.put("name", apiName);
                        param.put("user", userBase64);
                        param.put("password", passwordBase64);
                        param.put("ip", ipBase64);
                        param.put("latitude", latitudeBase64);
                        param.put("longitude", longitudeBase64);
                        rawJson.put("param", param);
                    } catch(JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    Log.d("RAW JSON ", rawJson.toString());
                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("LOGIN RESPONSE", response.toString());
                            alertDialog.dismiss();
                            JSONObject jsonObject  = null;
                            String status = response.toString();

                            try {
                                if(status.contains("error")) {
                                    jsonObject = new JSONObject(response.getString("error"));
                                    login.onFailed(jsonObject);
                                } else {
                                    jsonObject = new JSONObject(response.getString("response"));
                                    login.onSuccess(jsonObject);
                                }

                            } catch (JSONException e) {
                                alertDialog.dismiss();
                                e.printStackTrace();
                                Log.d("CATCH N", e.toString());
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            alertDialog.dismiss();
                            login.onError(volleyError);
                            handlingError(volleyError);
                        }
                    }){
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put("Content-Type", "application/json");
                            params.put("Authorization", "Bearer " + result);

                            return params;
                        }
                    };
                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
                }

                @Override
                public void onError(String error) {
                    alertDialog.dismiss();
                    errorToken = error;
                    Log.d("ERROR TOKEN", error.toString());
                }
            }, context, userName);
        } catch (Exception e){
            Log.d("login_error", e.toString());
        }
//
//        TokenGenerate.generate(new IToken() {
//            @Override
//            public void onSuccess(final String result) {
//                Log.d("TOKEN ", result);
//                token[0] = result;
//                JSONObject rawJson = new JSONObject();
//                JSONObject param = new JSONObject();
//
//                try {
//                    byte[] bytUser = userName.getBytes("UTF-8");
//                    String userBase64 = Base64.encodeToString(bytUser, Base64.DEFAULT);
//                    byte[] bytPassword = password.getBytes("UTF-8");
//                    String passwordBase64 = Base64.encodeToString(bytPassword, Base64.DEFAULT);
//                    byte[] bytIp = ip.getBytes("UTF-8");
//                    String ipBase64 = Base64.encodeToString(bytIp, Base64.DEFAULT);
//                    byte[] bytLatitude = latitude.getBytes("UTF-8");
//                    String latitudeBase64 = Base64.encodeToString(bytLatitude, Base64.DEFAULT);
//                    byte[] bytLongitude = longitude.getBytes("UTF-8");
//                    String longitudeBase64 = Base64.encodeToString(bytLongitude, Base64.DEFAULT);
//
//                    rawJson.put("name", apiName);
//                    param.put("user", userBase64);
//                    param.put("password", passwordBase64);
//                    param.put("ip", ipBase64);
//                    param.put("latitude", latitudeBase64);
//                    param.put("longitude", longitudeBase64);
//                    rawJson.put("param", param);
//                } catch(JSONException e) {
//                    e.printStackTrace();
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Log.d("RAW JSON ", rawJson.toString());
//                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.d("LOGIN RESPONSE", response.toString());
//                        alertDialog.dismiss();
//                        JSONObject jsonObject  = null;
//                        String status = response.toString();
//
//                        try {
//                            if(status.contains("error")) {
//                                jsonObject = new JSONObject(response.getString("error"));
//                                login.onFailed(jsonObject);
//                            } else {
//                                jsonObject = new JSONObject(response.getString("response"));
//                                login.onSuccess(jsonObject);
//                            }
//
//                        } catch (JSONException e) {
//                            alertDialog.dismiss();
//                            e.printStackTrace();
//                            Log.d("CATCH N", e.toString());
//                        }
//                    }
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError volleyError) {
//                        alertDialog.dismiss();
//                        login.onError(volleyError);
//                        handlingError(volleyError);
//                    }
//                }){
//                    @Override
//                    public Map<String, String> getHeaders() throws AuthFailureError {
//                        Map<String, String> params = new HashMap<String, String>();
//                        params.put("Content-Type", "application/json");
//                        params.put("Authorization", "Bearer " + result);
//
//                        return params;
//                    }
//                };
//                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
//                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//
//                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
//            }
//
//            @Override
//            public void onError(String error) {
//                alertDialog.dismiss();
//                errorToken = error;
//                Log.d("ERROR TOKEN", error.toString());
//            }
//        }, context, userName);

    }

    private static void handlingError(VolleyError error) {
        if(error instanceof NoConnectionError){
            Log.d("VOLLEY_ERROR", "ERROR_CONNECTION_LOGIN_SERVICE");
        }else if (error instanceof TimeoutError) {
            Log.d("VOLLEY_ERROR", "ERROR_TIMEOUT_CONNECTION_LOGIN_SERVICE");
        } else if (error instanceof AuthFailureError) {
            Log.d("VOLLEY_ERROR","ERROR_AUTH_LOGIN_SERVICE");
        } else if (error instanceof ServerError) {
            Log.d("VOLLEY_ERROR", "ERROR_SERVER_LOGIN_SERVICE");
        } else if (error instanceof NetworkError) {
            Log.d("VOLLEY_ERROR", "ERROR_NETWORK_LOGIN_SERVICE");
        } else if (error instanceof ParseError) {
            Log.d("VOLLEY_ERROR", "ERROR_PARSE_LOGIN_SERVICE");
        }
    }

}
