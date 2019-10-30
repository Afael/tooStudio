package com.desktopip.exploriztic.tootanium.utilities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.Ping;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

/**
 * Created by Jayus on 17/07/2018.
 */

public class Utils {
    //String Values to be Used in App
    public static final String downloadDirectory = "DIPDownloads";

    CustomAlertDialogManager alert;

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    @Nullable
    public static String getNetworkType(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }

    public static String base64Helper(String obj){
        byte[] bytObj;
        String objBase64 = null;
        try {
            bytObj = obj.getBytes("UTF-8");
            objBase64 = Base64.encodeToString(bytObj, Base64.DEFAULT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return objBase64;
    }

    public static JSONObject makingJSON(String method, Map map){
        JSONObject body = new JSONObject();
        JSONObject params = new JSONObject(map);
        try {
            body.put("name", method);
            body.put("param", params);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return body;
    }

    public static boolean isAppAvailable(Context context, Intent intent) {
        final PackageManager mgr = context.getPackageManager();
        List<ResolveInfo> list = mgr.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    public static boolean isPackageInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("Tag", packageName + " not installed");
        }
        return false;
    }

    public static boolean pingServer(URL url, Context context){
        Ping r = new Ping();
        boolean status = true;
        if (isNetworkConnected(context)) {
            r.net = getNetworkType(context);
            try {
                String hostAddress;
                long start = System.currentTimeMillis();
                hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
                long dnsResolved = System.currentTimeMillis();
                Socket socket = new Socket(hostAddress, url.getPort());
                socket.close();
                long probeFinish = System.currentTimeMillis();
                r.dns = (int) (dnsResolved - start);
                r.cnt = (int) (probeFinish - dnsResolved);
                r.host = url.getHost();
                r.ip = hostAddress;
            }
            catch (Exception ex) {
                status = false;
            }
        }
        return status;
    }

    public static Ping ping(URL url, Context ctx) {
        AlertDialog alertDialog = new SpotsDialog(ctx);
        alertDialog.show();
        alertDialog.setMessage("ping connection to te server");
        Log.d("ping_url", url.toString());
        Ping r = new Ping();
        if (isNetworkConnected(ctx)) {
            r.net = getNetworkType(ctx);
            try {
                String hostAddress;
                long start = System.currentTimeMillis();
                hostAddress = InetAddress.getByName(url.getHost()).getHostAddress();
                long dnsResolved = System.currentTimeMillis();
                Socket socket = new Socket(hostAddress, url.getPort());
                socket.close();
                long probeFinish = System.currentTimeMillis();
                r.dns = (int) (dnsResolved - start);
                r.cnt = (int) (probeFinish - dnsResolved);
                r.host = url.getHost();
                r.ip = hostAddress;

                alertDialog.dismiss();
                Toast.makeText(ctx, "Connection success", Toast.LENGTH_LONG).show();
            }
            catch (Exception ex) {
                Log.d("ping", "Unable to ping address: "+ex.getMessage());
                alertDialog.dismiss();
                Toast.makeText(ctx, ex.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return r;
    }

    public static void handlingVolleyError(Context context, VolleyError error){
        CustomAlertDialogManager alert = new CustomAlertDialogManager();
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            alert.showAlertDialog(context, "Network Error", context.getApplicationContext().getString(R.string.volley_timeout_error), "Close");
        } else if (error instanceof AuthFailureError) {
            alert.showAlertDialog(context, "Authentication Error", context.getApplicationContext().getString(R.string.volley_auth_error) + error.toString(), "Close");
        } else if (error instanceof ServerError) {
            alert.showAlertDialog(context, "Server Error", context.getApplicationContext().getString(R.string.volley_server_error), "Close");
        } else if (error instanceof NetworkError) {
            alert.showAlertDialog(context, "Network Error", context.getApplicationContext().getString(R.string.volley_network_access_error) + error.toString(), "Close");
        } else if (error instanceof ParseError) {
            alert.showAlertDialog(context, "Parse Error", context.getApplicationContext().getString(R.string.volley_parse_error) + error.toString(), "Close");
        }
    }

}
