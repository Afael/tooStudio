package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.desktopip.exploriztic.tootanium.R;

/**
 * Created by Jayus on 04/07/2018.
 */

public class PreferenceConfig {

    private SharedPreferences sharedPreferences;
    private static Context context;
    public static final String BASE_URL = "http://192.168.90.50/online-storage/";
    public static final String BASE_URL_API = "http://192.168.90.50/online-storage/rest-api/";
    public static final String BASE_URL_DOWNLOAD = "http://192.168.90.50/online-storage/temp/";
    public static final String BASE_URL_UPLOAD = "http://192.168.90.50/online-storage/rest-api/upload.php";

    public PreferenceConfig(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref_file), Context.MODE_PRIVATE);
    }

    public void writeLoginStatus(boolean status) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(context.getString(R.string.pref_login_status), status);
        editor.commit();
    }

    public boolean readLoginStatus() {
        return sharedPreferences.getBoolean(context.getString(R.string.pref_login_status), false);
    }

    public void writeName(String name) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.pref_user_name), name);
        editor.commit();
    }

    public String readName() {
        return sharedPreferences.getString(context.getString(R.string.pref_user_name), "User");
    }

    public static void displayToast(String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
