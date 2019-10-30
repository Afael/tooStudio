package com.desktopip.exploriztic.tootanium.models;

import android.content.Context;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.utilities.ColorHelper;

import java.util.HashMap;


public class SpartialColor {

    /*public static final String LIGHT_BLUE = "light blue";
    public static final String DARK_BLUE = "dark blue";
    public static final String ORANGE = "orange";
    public static final String GREEN = "green";*/

    private static HashMap<String, SpartialColor> predefineColor = new HashMap<>();


    private String idColor;
    private int color;
    private String colorAlpha;
    private Context context;
    private static final String alphaValue = "#C0";

//desktopip-dark-blue desktopip-blue
    public static HashMap<String, SpartialColor> getPredefineColor(Context context){

        predefineColor.put("desktopip-blue", new SpartialColor(R.color.light_blue, context, "desktopip-blue"));
        predefineColor.put("desktopip-orange", new SpartialColor(R.color.orange, context, "desktopip-orange"));
        predefineColor.put("desktopip-dark-blue", new SpartialColor(R.color.dark_blue, context, "desktopip-dark-blue"));
        predefineColor.put("desktopip-green", new SpartialColor(R.color.green, context, "desktopip-green"));

        return predefineColor;
    }

    public static SpartialColor getColor(Context context, String settingColor){
        HashMap<String, SpartialColor> color = getPredefineColor(context);

        if(color.containsKey(settingColor)){
            return color.get(settingColor);
        } else {
            return color.get("desktopip-blue");
        }
        //return color.get(settingColor);
    }

    public SpartialColor(int colorResId, Context context, String idColor) {
        this.idColor = idColor;
        this.color = ColorHelper.fromResources(context,colorResId);
        this.context = context;
        this.colorAlpha = getAlphaColor(colorResId);
    }

    /*private static String getHexCode(Context context, int resId){
        return "#"+Integer.toHexString(ContextCompat.getColor(context,resId));
    }*/

    private String getAlphaColor(int colorRes){
        String colorString = context.getResources().getString(colorRes);

        StringBuilder value = new StringBuilder(alphaValue);
        value.append(colorString.substring(3,colorString.length()));
        return value.toString();
    }

    public String getIdColor() {
        return idColor;
    }

    public int colorResId() {
        return color;
    }

    public String getColorAlpha() {
        return colorAlpha;
    }
}
