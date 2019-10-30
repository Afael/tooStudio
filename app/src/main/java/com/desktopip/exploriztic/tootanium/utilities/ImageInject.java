package com.desktopip.exploriztic.tootanium.utilities;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.desktopip.exploriztic.tootanium.R;

import java.io.File;

public class ImageInject {

    public static final int DEFAULT_PLACEHOLDER = -222;
    public static final int DEFAULT_ERROR_PLACEHOLDER = -333;
    public static final int DEFAULT_PLACEHOLDER_WALLPAPER = 222;
    public static final int DEFAULT_ERROR_PLACEHOLDER_WALLPAPER = 333;

    private static final int PLACEHOLDER_INDEX = 0;
    private static final int ERROR_PLACEHOLDER_INDEX = 1;


    public static void setWithGlide(Context context, int resId, ImageView target){
        Glide.with(context).load(resId).into(target);
    }

    public static void setWithGlide(Context context, File file, ImageView target){
        Glide.with(context).load(file).into(target);
    }

    public static void setWithGlide(Context context, String path, ImageView target, int[] size){

        int width = size[0];
        int height = size[1];

        Glide.with(context)
                .load(path)
                .apply(new RequestOptions()
                        .optionalFitCenter()
                        .override(width,height))
                .into(target);
    }

    private static int[] getResId(int placeholderResId, int errorPicHolderResId){
        int[] resId = new int[2];
        if(placeholderResId == DEFAULT_PLACEHOLDER)
            resId[PLACEHOLDER_INDEX] = R.drawable.profil_pic_placeholder;
        else if(placeholderResId == DEFAULT_PLACEHOLDER_WALLPAPER)
            resId[PLACEHOLDER_INDEX] = placeholderResId;

            if(errorPicHolderResId == DEFAULT_ERROR_PLACEHOLDER)
                resId[ERROR_PLACEHOLDER_INDEX] = R.drawable.profil_pic_placeholder;
            else if(errorPicHolderResId == DEFAULT_ERROR_PLACEHOLDER_WALLPAPER)
                resId[ERROR_PLACEHOLDER_INDEX] = errorPicHolderResId;

            return resId;
    }
}
