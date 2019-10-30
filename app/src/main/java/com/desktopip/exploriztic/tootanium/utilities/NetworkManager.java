package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.desktopip.exploriztic.tootanium.interfaces.IRequestResult;
import com.desktopip.exploriztic.tootanium.interfaces.IToken;
import com.desktopip.exploriztic.tootanium.volley.TokenGenerate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkManager {

    private static NetworkManager mInstance;
    private RequestQueue requestQueue;
    private static Context context;

    private static SessionManager sessionManager;
    private static HashMap<String, String> user;
    private static String baseUrl, urlAPI, uid, userId;

    private NetworkManager(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized NetworkManager getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new NetworkManager(context);
        }
        return mInstance;
    }

    public<T> void addToRequestQueue(Request<T> request) {
        requestQueue.add(request);
    }

    public void lazyJsonObjectRequest(final JSONObject param, final IRequestResult<JSONObject> listener)
    {
        sessionManager = new SessionManager(context);
        user = sessionManager.getUserDetails();
        uid = user.get(SessionManager.KEY_UID);
        userId = user.get(SessionManager.KEY_USERNAME);
        urlAPI = user.get(SessionManager.BASE_URL_API);

        TokenGenerate.generate(new IToken() {
            @Override
            public void onSuccess(final String token) {
                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, urlAPI, param,
                        new Response.Listener<JSONObject>()
                        {
                            @Override
                            public void onResponse(JSONObject response)
                            {
                                if(null != response.toString()){
                                    String status = response.toString();
                                    JSONObject jsonObject = null;
                                    try {

                                        if (status.contains("error")) {
                                            jsonObject = new JSONObject(response.getString("error"));
                                        }
                                        jsonObject = new JSONObject(response.getString("response"));

                                    } catch (JSONException e) {
                                    }

                                    listener.getResult(jsonObject);
                                }
                            }
                        },
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                if(error instanceof NoConnectionError){
                                    Log.d("VOLLEY_ERROR", "ERROR_CONNECTION_TOKEN_SERVICE");
                                    //listener.getError("ERROR_CONNECTION_TOKEN_SERVICE");
                                }else if (error instanceof TimeoutError) {
                                    Log.d("VOLLEY_ERROR", "ERROR_TIMEOUT_CONNECTION_TOKEN_SERVICE");
                                    //listener.getError("ERROR_TIMEOUT_CONNECTION_TOKEN_SERVICE");
                                } else if (error instanceof AuthFailureError) {
                                    Log.d("VOLLEY_ERROR","ERROR_AUTH_TOKEN_SERVICE");
                                    //listener.getError("ERROR_AUTH_TOKEN_SERVICE");
                                } else if (error instanceof ServerError) {
                                    Log.d("VOLLEY_ERROR", "ERROR_SERVER_TOKEN_SERVICE");
                                    //listener.getError("ERROR_SERVER_TOKEN_SERVICE");
                                } else if (error instanceof NetworkError) {
                                    Log.d("VOLLEY_ERROR", "ERROR_NETWORK_TOKEN_SERVICE");
                                    //listener.getError("ERROR_NETWORK_TOKEN_SERVICE");
                                } else if (error instanceof ParseError) {
                                    Log.d("VOLLEY_ERROR", "ERROR_PARSE_TOKEN_SERVICE");
                                    //listener.getError("ERROR_PARSE_TOKEN_SERVICE");
                                }
                            }
                        }){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("Content-Type", "application/json");
                        params.put("Authorization", "Bearer " + token);
                        return params;
                    }
                };
                request.setRetryPolicy(new DefaultRetryPolicy(0
                        , DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(request);
            }

            @Override
            public void onError(String error) {
                //errorToken = error;
            }
        }, context, userId);
    }
}
