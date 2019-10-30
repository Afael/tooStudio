package com.desktopip.exploriztic.tootanium.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IAdvertise;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class FileExplorerAdvertiseFolderServices {
    private static final String TAG = "FileExplorerAdvertise";

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String urlAPI;
    private static String baseUrl;
    final static String[] token = new String[1];
    static String errorToken;


    public static void getAdvertiseFolder(final IAdvertise iAdvertise, final Context context, final String apiName
            , final String userName, final String groupId, final String groupAccessId, String pathAdvertise) {

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                Log.d(TAG, "onSuccess: "+result.toString());
                JSONObject rawData = new JSONObject();
                JSONObject params = new JSONObject();
                try{
                    rawData.put("name", apiName);
                    params.put("groupId", groupId);
                    params.put("user", userName);
                    rawData.put("param", params);
                }catch(JSONException e) {
                    Log.d(TAG, "JSONException: ");
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawData, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        alertDialog.dismiss();

                        JSONObject jsonObject = null;

                        String status = response.toString();
                        try {

                            if (status.contains("error")) {
                                jsonObject = new JSONObject(response.getString("error"));
                                iAdvertise.onSuccess(jsonObject);
                            }
                            jsonObject = new JSONObject(response.getString("response"));
                            iAdvertise.onSuccess(jsonObject);

                        } catch (JSONException e) {
                            alertDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        iAdvertise.onError(volleyError);
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
                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "onError: "+error);
            }
        }, context, userName);

    }

    public static void createAdvertiseFolder(final IGroup iGroup, final Context context, final String apiName, final String userName, final String password, final String folderName, final String groupId){
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
            public void onSuccess(final String result) {
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
            byte[] bytFolderName = folderName.getBytes("UTF-8");
            String folderNameBase64 = Base64.encodeToString(bytFolderName, Base64.DEFAULT);
            byte[] bytPassword = password.getBytes("UTF-8");
            String passwordBase64 = Base64.encodeToString(bytPassword, Base64.DEFAULT);

            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("advertiseName", folderNameBase64);
            param.put("password", passwordBase64);
            param.put("groupId", groupId);
            rawJson.put("param", param);
            Log.d(TAG, "onSuccess: apiName: "+apiName+"\nuser: "+userName+"\npass: "+passwordBase64+"\nfolderName"+folderNameBase64+"\ngroupId: "+groupId);
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
                                iGroup.onSuccess(jsonObject);
                            } else {
                                jsonObject = new JSONObject(response.getString("response"));
                                iGroup.onSuccess(jsonObject);
                            }

                        } catch (JSONException e) {
                            alertDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iGroup.onError(volleyError);
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
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

}
