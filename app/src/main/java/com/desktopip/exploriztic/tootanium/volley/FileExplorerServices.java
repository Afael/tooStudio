package com.desktopip.exploriztic.tootanium.volley;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.desktopip.exploriztic.tootanium.interfaces.IFileExplore;
import com.desktopip.exploriztic.tootanium.interfaces.INewTab;
import com.desktopip.exploriztic.tootanium.interfaces.IRequestResult;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.utilities.MySingleton;
import com.desktopip.exploriztic.tootanium.utilities.NetworkManager;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.utilities.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Jayus on 13/07/2018.
 */

public class FileExplorerServices {

    static String token = null;
    static String errorToken;
    private static String urlAPI, uid;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;

    public static void load(final IFileExplore iFileExplore, final Context context, final String apiName
            ,  final String userName, final String password, final String path) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");
        //Log.d("uid_service", uid);

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String result) {
                //token[0] = result;
                //Log.d("tokenId", token[0]);
                JSONObject rawJson = new JSONObject();
                JSONObject param = new JSONObject();

                try {
                    String pathBase64 = Utils.base64Helper(path);
                    String passBase64 = Utils.base64Helper(password);

                    rawJson.put("name", apiName);
                    //param.put("user", userName);
                    param.put("id", pathBase64);
                    param.put("uid", uid);
                    param.put("password", passBase64);
                    rawJson.put("param", param);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("file", response.toString());
                        alertDialog.dismiss();
                        JSONObject jsonObject = null;
                        String status = response.toString();
                        try {

                            if (status.contains("error")) {
                                jsonObject = new JSONObject(response.getString("error"));
                                iFileExplore.onSuccess(jsonObject);
                            }
                            jsonObject = new JSONObject(response.getString("response"));
                            iFileExplore.onSuccess(jsonObject);

                        } catch (JSONException e) {
                            alertDialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        alertDialog.dismiss();
                        iFileExplore.onError(volleyError);
                    }
                }) {
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
                errorToken = error;
            }
        }, context, userName);

    }

    public static void copyFile(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String path, String type) {

//        sessionManager = new SessionManager(context);
//        user = sessionManager.getUserDetails();
//        uid = user.get(SessionManager.KEY_UID);
//        urlAPI = user.get(SessionManager.BASE_URL_API);
//
//        final AlertDialog alertDialog = new SpotsDialog(context);
//        alertDialog.show();
//        alertDialog.setMessage("Copying...");

        //String urlCopy = "http://spatial.desktopip.com/action/copy.php";

//        TokenGenerate.generate(new IToken() {
//            @Override
//            public void onSuccess(String result) {
//                token = result;
//            }
//
//            @Override
//            public void onError(String error) {
//                errorToken = error;
//            }
//        }, context, userName);

//        final JSONObject rawJson = new JSONObject();
//        JSONObject param = new JSONObject();
//        String caps = type.toUpperCase();
//
//        try {
//            String pathBase64 = Utils.base64Helper(path);
//            String typeBase64 = Utils.base64Helper(caps);
//
//            rawJson.put("name", apiName);
//            param.put("uid", uid);
//            param.put("path", pathBase64);
//            param.put("type", typeBase64);
//            rawJson.put("param", param);
//            //Log.d("copy", pathBase64+"-"+typeBase64);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

//        NetworkManager.getInstance(context).lazyJsonObjectRequest(rawJson, new IRequestResult<JSONObject>()
//        {
//            @Override
//            public void getResult(JSONObject object) {
//
//            }
//
//            @Override
//            public void getError(String object) {
//
//            }
//        });

//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                Log.d("result_copy_service", response.toString());
//                alertDialog.dismiss();
//                JSONObject jsonObject = null;
//                String status = response.toString();
//                try {
//
//                    if (status.contains("error")) {
//                        jsonObject = new JSONObject(response.getString("error"));
//                        iFileExplore.onSuccess(jsonObject);
//                    }
//                    jsonObject = new JSONObject(response.getString("response"));
//                    iFileExplore.onSuccess(jsonObject);
//
//                } catch (JSONException e) {
//                    alertDialog.dismiss();
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError volleyError) {
//                alertDialog.dismiss();
//                iFileExplore.onError(volleyError);
//            }
//        }) {
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                params.put("Authorization", "Bearer " + token);
//                return params;
//            }
//        };
//        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void paste(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String path, String password, String sessionId) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please waiting...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("locs", pathBase64);
            param.put("password", passwordBase64);
            param.put("session_id", sessionId);
            rawJson.put("param", param);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("paste", response.toString());
                alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                //String apake;
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void rename(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String oldName, String newName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Renaming file...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String oldNameBase64 = Utils.base64Helper(oldName);
            String newNameBase64 = Utils.base64Helper(newName);
            String passwordBase64 = Utils.base64Helper(password);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("oldname", oldNameBase64);
            param.put("newname", newNameBase64);
            param.put("password", passwordBase64);
            rawJson.put("param", param);
            //Log.d("rename", "OldPath: "+oldName+"\nNewPath: "+newName+"\nOldBase64: "+oldNameBase64+"\nNewBase64: "+newNameBase64);
            //old_name: ZGV4aXAxL0RvY3VtZW50cy9KYWphbmc=
            //new_name: ZGV4aXAxL0RvY3VtZW50cy9KYWphbmcx
        } catch (JSONException e) {
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void newFolder(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String path, String dirName) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Please wait...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);
            String dirNameBase64 = Utils.base64Helper(dirName);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("password", passwordBase64);
            param.put("path", pathBase64);
            param.put("dirname", dirNameBase64);
            rawJson.put("param", param);
            Log.d("createDirectory", "Path: "+pathBase64+"\nNewFolder: "+dirNameBase64);
            //old_name: ZGV4aXAxL0RvY3VtZW50cy9KYWphbmc=
            //new_name: ZGV4aXAxL0RvY3VtZW50cy9KYWphbmcx
        } catch (JSONException e) {
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void delete(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String path, String password) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Deleting file...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("path", pathBase64);
            param.put("password", passwordBase64);
            rawJson.put("param", param);
            Log.d("paste", pathBase64+"-"+password+"-"+userName);
        } catch (JSONException e) {
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void createDownloadChannel(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String date, String path, String url, String type) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Creating channel...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        String caps = type.toUpperCase();
        try {
            String dateBase64 = Utils.base64Helper(date);
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);
            String urlBase64 = Utils.base64Helper(url);
            String typeBase64 = Utils.base64Helper(caps);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("date", dateBase64);
            param.put("path", pathBase64);
            param.put("url", urlBase64);
            param.put("type", typeBase64);
            param.put("password", passwordBase64);
            rawJson.put("param", param);
            Log.d("DownloadChannel", "Date: "+dateBase64+"\nPath: "+pathBase64+"\nPassword: "+passwordBase64
                    +"\nURL: " +urlBase64+"\nType: "+typeBase64);
        } catch (JSONException e) {
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void createUploadChannel(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String date, String path, String url) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Creating channel...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String dateBase64 = Utils.base64Helper(date);
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);
            String urlBase64 = Utils.base64Helper(url);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("date", dateBase64);
            param.put("path", pathBase64);
            param.put("url", urlBase64);
            param.put("password", passwordBase64);
            rawJson.put("param", param);
            Log.d("UploadChannel", "Date: "+dateBase64+"\nPath: "+pathBase64+"\nPassword: "+passwordBase64
                    +"\nURL: " +urlBase64);
        } catch (JSONException e) {
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    else {
                        jsonObject = new JSONObject(response.getString("response"));
                        iFileExplore.onSuccess(jsonObject);
                    }

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void createAppsChannel(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String date, String path, String url) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Creating channel...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String dateBase64 = Utils.base64Helper(date);
            String pathBase64 = Utils.base64Helper(path);
            String passwordBase64 = Utils.base64Helper(password);
            String urlBase64 = Utils.base64Helper(url);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("password", passwordBase64);
            param.put("date", dateBase64);
            param.put("path", pathBase64);
            param.put("url", urlBase64);
            rawJson.put("param", param);
            //Log.d("AppsChannel", "Date: "+dateBase64+"\nPath: "+pathBase64+"\nPassword: "+passwordBase64 +"\nURL: " +urlBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                alertDialog.dismiss();
                //Log.d("url", response.toString());
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    }
                    else {
                        jsonObject = new JSONObject(response.getString("response"));
                        iFileExplore.onSuccess(jsonObject);
                        //Log.d("url1", jsonObject.toString());
                    }

                } catch (JSONException e) {
                    alertDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void download(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String path) {

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Processing request...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {

            byte[] bytPath = path.getBytes("UTF-8");
            String pathBase64 = Base64.encodeToString(bytPath, Base64.DEFAULT);
            byte[] bytPassword = password.getBytes("UTF-8");
            String passwordBase64 = Base64.encodeToString(bytPassword, Base64.DEFAULT);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("path", pathBase64);
            param.put("password", passwordBase64);
            rawJson.put("param", param);
            // Log.d("download", "Path: "+pathBase64+"\nPassword: "+passwordBase64);
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
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }

    public static void playAudio(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String path){

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Processing request...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String path64 = Utils.base64Helper(path);
            String pass64 = Utils.base64Helper(password);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("password", pass64);
            param.put("path", path64);
            rawJson.put("param", param);
            // Log.d("download", "Path: "+pathBase64+"\nPassword: "+passwordBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("play", response.toString());
                alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    } else {
                        jsonObject = new JSONObject(response.getString("response"));
                        iFileExplore.onSuccess(jsonObject);
                    }


                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void clearTempMusicPlayer(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String song){

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String songBase64 = Utils.base64Helper(song);

            rawJson.put("name", apiName);
            param.put("song", songBase64);
            rawJson.put("param", param);
            // Log.d("download", "Path: "+pathBase64+"\nPassword: "+passwordBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                //alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    } else {
                        jsonObject = new JSONObject(response.getString("response"));
                        iFileExplore.onSuccess(jsonObject);
                    }

                } catch (JSONException e) {
                    //alertDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                //alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }

    public static void openWord(final IFileExplore iFileExplore, final Context context, String apiName
            , String userName, String password, String file){

        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        final AlertDialog alertDialog = new SpotsDialog(context);
        alertDialog.show();
        alertDialog.setMessage("Processing request...");

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(String result) {
                token = result;
            }

            @Override
            public void onError(String error) {
                errorToken = error;
            }
        }, context, userName);

        JSONObject rawJson = new JSONObject();
        JSONObject param = new JSONObject();
        try {
            String passwordBase64 = Utils.base64Helper(password);
            String fileBase64 = Utils.base64Helper(file);

            rawJson.put("name", apiName);
            param.put("uid", uid);
            param.put("password", passwordBase64);
            param.put("file", fileBase64);
            rawJson.put("param", param);
            // Log.d("openWordParam", "name: "+apiName+"user"+userName+"\nPassword: "+passwordBase64+"file: "+fileBase64);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(urlAPI, rawJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("openWordResponse", response.toString());
                alertDialog.dismiss();
                JSONObject jsonObject = null;
                String status = response.toString();
                try {

                    if (status.contains("error")) {
                        jsonObject = new JSONObject(response.getString("error"));
                        iFileExplore.onSuccess(jsonObject);
                    }
                    jsonObject = new JSONObject(response.getString("response"));
                    iFileExplore.onSuccess(jsonObject);

                } catch (JSONException e) {
                    alertDialog.dismiss();
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.d("openWordResponse", volleyError.toString());
                alertDialog.dismiss();
                iFileExplore.onError(volleyError);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Authorization", "Bearer " + token);
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}