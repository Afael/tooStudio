package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.os.Build;
import android.support.annotation.ColorRes;

public class ColorHelper {

    public static int fromResources(Context context, @ColorRes int colorResId){

        int colorValue;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            colorValue = context.getResources().getColor(colorResId, null);
        } else {
            colorValue = context.getResources().getColor(colorResId);
        }

        return colorValue;
    }
}
