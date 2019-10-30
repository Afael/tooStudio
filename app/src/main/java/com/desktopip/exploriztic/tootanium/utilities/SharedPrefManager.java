package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.desktopip.exploriztic.tootanium.R;

public class SharedPrefManager {

    private static volatile SharedPreferences sharedPreferences;
    private static volatile SharedPreferences.Editor sharedPrefEditor ;

    public static void saveString(Context context, String keyValue, String value){
        SharedPreferences.Editor editor = getEditorInstance(context);
        editor.putString(keyValue,value);
        editor.commit();
    }

    public static void saveInt(Context context, String keyValue, int value){
        SharedPreferences.Editor editor = getEditorInstance(context);
        editor.putInt(keyValue,value);
        editor.commit();
    }

    public static String getString(Context context, String keyValue, String defaultValue){

        return getSharPrefInstance(context).getString(keyValue,defaultValue);
    }

    public static int getInt(Context context, String keyValue, int defaultValue){
        return getSharPrefInstance(context).getInt(keyValue,defaultValue);
    }

    private static SharedPreferences getSharPrefInstance(Context context){

        if(sharedPrefEditor == null){
            synchronized (SharedPreferences.class){
                 sharedPreferences = context.getSharedPreferences(context.getString(R.string.share_preference_key), Context.MODE_PRIVATE);
            }
        }

        return sharedPreferences;
    }

    private static SharedPreferences.Editor getEditorInstance(Context context){

        if(sharedPreferences == null){
            sharedPreferences = getSharPrefInstance(context);
        }

        if(sharedPrefEditor == null){
            synchronized (SharedPreferences.Editor.class){
                sharedPrefEditor = sharedPreferences.edit();
            }
        }
        return sharedPrefEditor;
    }

}
