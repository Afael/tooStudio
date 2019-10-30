package com.desktopip.exploriztic.tootanium.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Jayus on 20/08/2018.
 */

public class GroupServices {

    private static final String TAG = "GroupServices";

    final static String[] token = new String[1];
    static String errorToken;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String urlAPI;
    private static String baseUrl;

    public static void loadAdvertise(final IGroup iGroup, final Context context, String apiName, String userName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userName);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        //alertDialog.dismiss();
                        iGroup.onError(volleyError);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);

    }

    public static void createGroupAdvertise(final IGroup iGroup, final Context context, final String apiName, final String userName, final String groupName, final String groupDesc) {

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
                JSONObject rawJson = new JSONObject();
                JSONObject param = new JSONObject();

                try {
                    byte[] bytGroupName = groupName.getBytes("UTF-8");
                    String groupNameBase64 = Base64.encodeToString(bytGroupName, Base64.DEFAULT);
                    byte[] bytGroupDesc = groupDesc.getBytes("UTF-8");
                    String groupDescBase64 = Base64.encodeToString(bytGroupDesc, Base64.DEFAULT);

                    rawJson.put("name", apiName);
                    param.put("user", userName);
                    param.put("groupName", groupNameBase64);
                    param.put("groupDesc", groupDescBase64);
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
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);


    }

    public static void addInvitationGroupAdvertise(final IGroup iGroup, final Context context, String apiName, String userName, ArrayList<String> userInvitation, String groupId) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);
        String paramUserInvitation = userInvitation.toString();
        paramUserInvitation = paramUserInvitation.replaceAll(", ", ",").replace("[", "").replace("]", "");

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
//            byte[] bytGroupName = groupName.getBytes("UTF-8");
//            String groupNameBase64 = Base64.encodeToString(bytGroupName, Base64.DEFAULT);
//            byte[] bytGroupDesc = groupDesc.getBytes("UTF-8");
//            String groupDescBase64 = Base64.encodeToString(bytGroupDesc, Base64.DEFAULT);

            Log.d(TAG, "addInvitationGroupAdvertise: "+paramUserInvitation+"\n"+groupId);
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("advUserId", paramUserInvitation);
            param.put("advGroupId", groupId);
            rawJson.put("param", param);
            Log.d(TAG, "addInvitationGroupAdvertise: "+rawJson);
        } catch (JSONException e) {
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void loadAdvertiseGroupUser(final IGroup iGroup, final Context context, String apiName, String userName, String sGroupId) {

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
            rawJson.put("name", apiName);
            param.put("groupId", sGroupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void joinGroup(final IGroup iGroup, final Context context, String apiName, String userName, String groupId) {

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
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("group_id", groupId);
            rawJson.put("param", param);
            Log.d(TAG, "addInvitationGroupAdvertise: "+rawJson);
        } catch (JSONException e) {
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
                            }else {
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
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void loadRequestGroup(final IGroup iGroup, final Context context, String apiName, String userName, String groupId) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("groupId", groupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);

    }

    public static void confirmRequestGroup(final IGroup iGroup, final Context context, String apiName, String userName, String userId, String groupId) {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userId);
            param.put("groupId", groupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);
    }

    public static void requestGroup(final IGroup iGroup, final Context context, String apiName, String userName, String groupId) {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("groupId", groupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);
    }

    public static void loadGroupAccess(final IGroup iGroup, final Context context, String apiName, String userName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userName);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        //alertDialog.dismiss();
                        iGroup.onError(volleyError);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);

    }

    public static void loadGroupAccessUsers(final IGroup iGroup, final Context context, String apiName, String userName, String groupId) {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("groupId", groupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        //alertDialog.dismiss();
                        iGroup.onError(volleyError);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);
    }

    public static void updateGroupAdvertise(final IGroup iGroup, final Context context, String apiName, String userName, String groupId, String groupName, String groupDesc) {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            byte[] bytGroupName = groupName.getBytes("UTF-8");
            String groupNameBase64 = Base64.encodeToString(bytGroupName, Base64.DEFAULT);
            byte[] bytGroupDesc = groupDesc.getBytes("UTF-8");
            String groupDescBase64 = Base64.encodeToString(bytGroupDesc, Base64.DEFAULT);
            rawJson.put("name", apiName);
            param.put("user", groupId);
            param.put("groupId", groupId);
            param.put("groupName", groupNameBase64);
            param.put("groupDesc", groupDescBase64);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        //alertDialog.dismiss();
                        iGroup.onError(volleyError);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);
    }

    public static void deleteGroupAdvertise(final IGroup iGroup, final Context context, String apiName, String userName, String groupId) {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        // Base url API
        urlAPI = user.get(SessionManager.BASE_URL_API);
        baseUrl = user.get(SessionManager.BASE_URL);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        final JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();

        try {
            rawJson.put("name", apiName);
            param.put("user", userName);
            param.put("groupId", groupId);
            rawJson.put("param", param);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                Log.d(TAG, "generateToken: "+result);
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                alertDialog.dismiss();
                                Log.d(TAG, "loadGroup: "+token[0]);

                                JSONObject jsonObject = null;

                                String status = response.toString();
                                try {

                                    if (status.contains("error")) {
                                        jsonObject = new JSONObject(response.getString("error"));
                                        iGroup.onFailed(jsonObject);
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
                        //alertDialog.dismiss();
                        iGroup.onError(volleyError);
                    }
                }) { //no semicolon or coma
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + result);

                        return params;
                    }
                };
                MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
            }

            @Override
            public void onError(String error) {
                Log.d(TAG, "errorGenerateToken: "+error);
                errorToken = error;
            }
        }, context, userName);
    }

}
