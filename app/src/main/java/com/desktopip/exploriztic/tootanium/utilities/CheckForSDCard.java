package com.desktopip.exploriztic.tootanium.utilities;

import android.os.Environment;

/**
 * Created by Jayus on 17/07/2018.
 */

public class CheckForSDCard {
    //Check If SD Card is present or not method
    public boolean isSDCardPresent() {
        if (Environment.getExternalStorageState().equals(

                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
