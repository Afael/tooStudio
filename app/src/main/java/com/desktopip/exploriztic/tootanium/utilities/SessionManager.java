package com.desktopip.exploriztic.tootanium.utilities;

/**
 * Created by Jayus on 09/07/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.Login;

import java.util.HashMap;

public class SessionManager {
    // LogCat tag
    private static String TAG = SessionManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    //Editor
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    static int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "DipOnlineStoragePref";
    // All Shared Preferences Keys
    private static final String IS_LOGGED_IN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_UID = "uid";

    // User name (make variable public to access from outside)
    public static final String KEY_USERNAME = "username";

    // Password (make variable public to access from outside)
    public static final String KEY_PASSWORD = "password";

    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";

    public static final String HOST_NAME = "192.168.90.50";
    public static final String PORT = "8080";
    public static final String SSL = "isSSl";
    public static final String BASE_URL = "BASE_URL";
    public static final String BASE_URL_API = "BASE_URL_API";
    public static final String BASE_URL_DOWNLOAD = "BASE_URL_DOWNLOAD";
    public static final String BASE_URL_UPLOAD = "BASE_URL_UPLOAD";

    public static void firstTimeAskingPermission(Context context, String permission, boolean isFirstTime){
        SharedPreferences sharedPreference = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        sharedPreference.edit().putBoolean(permission, isFirstTime).apply();
    }

    public static boolean isFirstTimeAskingPermission(Context context, String permission){
        return context.getSharedPreferences(PREF_NAME, PRIVATE_MODE).getBoolean(permission, true);
    }

    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void clearPreference() {
        pref.edit().clear().commit();
    }

    /**
     * create session intro
     */
    public void createSessionIntro() {
        editor.putString(_context.getString(R.string.my_preference_key), "INIT_OK");
        editor.commit();
    }

    public boolean checkSessionIntro() {
        boolean status = false;
        if(pref.getString(_context.getString(R.string.my_preference_key), "null").equals("null")) {
            status = false;
        } else {
            status = true;
        }
        return status;
    }

    /**
     * create session setup
     * @return
     */
    public void createSessionSetup(
            String hostName
            , String port
            , boolean ssl
    ) {
        Log.d("Param", hostName+"\n"+port+"\n"+ssl);
        editor.putString(HOST_NAME, hostName);
        editor.putString(PORT, port);
        editor.putBoolean(SSL, ssl);

        String http = "http:";
        if(ssl){
            http = "https:";
        }

        String ipAddress = hostName, onlineStorage = "";
        if(ipAddress.contains("/")) {
            ipAddress = StringHelper.substringBeforeLast(hostName, "/");
            onlineStorage = StringHelper.substringAfterLast(hostName, "/");
            onlineStorage = new StringBuffer(onlineStorage).insert(0, "/").toString();
            //onlineStorage = new StringBuffer(onlineStorage).insert(onlineStorage.length(), "/").toString();

        }

        // Storing base url
        editor.putString(BASE_URL, http+"//"+ipAddress+":"+port+onlineStorage+"/");

        // Storing url API
        editor.putString(BASE_URL_API, http+"//"+ipAddress+":"+port+onlineStorage+"/rest-api/");

        //Storing url download
        editor.putString(BASE_URL_DOWNLOAD, http+"//"+ipAddress+":"+port+onlineStorage+"/temp/");

        // Storing url upload
        editor.putString(BASE_URL_UPLOAD, http+"//"+ipAddress+":"+port+onlineStorage+"/rest-api/upload.php");

        // commit changes
        editor.commit();

    }

    /**
     * create session login
     * @return
     */
    public void createSessionLogin(
            String uid
            , String username
            , String password
            , String latitude
            , String longitude
    ) {

        // Storing login value as TRUE
        editor.putBoolean(IS_LOGGED_IN, true);

        // Storing uid in pref
        editor.putString(KEY_UID, uid);

        // Storing name in pref
        editor.putString(KEY_USERNAME, username);

        // Storing email in pref
        editor.putString(KEY_PASSWORD, password);

        // Storing latitude in pref
        editor.putString(KEY_LATITUDE, latitude);

        // Storing longitude in pref
        editor.putString(KEY_LONGITUDE, longitude);

        // commit changes
        editor.commit();
    }

    /**
     * Get stored session data
     * */
    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<String, String>();
        // user uid
        user.put(KEY_UID, pref.getString(KEY_UID, null));
        // user name
        user.put(KEY_USERNAME, pref.getString(KEY_USERNAME, null));

        // user password
        user.put(KEY_PASSWORD, pref.getString(KEY_PASSWORD, null));

        // user latitude
        user.put(KEY_LATITUDE, pref.getString(KEY_LATITUDE, null));

        // user longitude
        user.put(KEY_LONGITUDE, pref.getString(KEY_LONGITUDE, null));

        // user base url
        user.put(BASE_URL, pref.getString(BASE_URL, null));

        // user url api
        user.put(BASE_URL_API, pref.getString(BASE_URL_API, null));

        // user url download
        user.put(BASE_URL_DOWNLOAD, pref.getString(BASE_URL_DOWNLOAD, null));

        // user upload
        user.put(BASE_URL_UPLOAD, pref.getString(BASE_URL_UPLOAD, null));

        // return user
        return user;
    }

    /**
     * Get stored session setup data
     * */
    public HashMap<String, String> getSetupDetails(){
        HashMap<String, String> setup = new HashMap<String, String>();

        // user base url
        setup.put(HOST_NAME, pref.getString(HOST_NAME, null));

        // user port
        setup.put(PORT, pref.getString(PORT, null));

        // user ssl
        setup.put(SSL, String.valueOf(pref.getBoolean(SSL, false)));

        // return setup
        return setup;
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing user data from Shared Preferences
        // Storing login value as FALSE
        editor.putBoolean(IS_LOGGED_IN, false);

        // Clearing name in pref
        editor.putString(KEY_UID, null);

        // Clearing name in pref
        editor.putString(KEY_USERNAME, null);

        // Clearing email in pref
        editor.putString(KEY_PASSWORD, null);

        // Clearing latitude in pref
        editor.putString(KEY_LATITUDE, null);

        // Clearing longitude in pref
        editor.putString(KEY_LONGITUDE, null);

        // commit changes
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(_context, Login.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(i);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * @return
     */
    public boolean checkLogin() {
        boolean status = true;
        if(!this.isLoggedIn()) {
            status = false;
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, Login.class);
            // Closing all the Activities
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            // Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
        return status;
    }

    /**
     * Quick check for login
     * **/
    // Get Login State
    public boolean isLoggedIn(){
        boolean status = pref.getBoolean(IS_LOGGED_IN, false);
        return status;
    }

}

