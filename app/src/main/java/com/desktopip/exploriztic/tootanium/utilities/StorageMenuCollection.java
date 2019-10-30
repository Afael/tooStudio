package com.desktopip.exploriztic.tootanium.utilities;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.StorageMenuAction;

import java.util.ArrayList;

public final class StorageMenuCollection {
    private static ArrayList<StorageMenuAction> actions;

    private StorageMenuCollection(){}

    public static ArrayList<StorageMenuAction> getActions(){
        if(actions == null){
            actions = new ArrayList<>();
            actions.add(new StorageMenuAction(R.drawable.ic_cloud_upload_black_24dp, "Upload Channel"));
            actions.add(new StorageMenuAction(R.drawable.ic_cloud_download_black_24dp, "Download Channel"));
            actions.add(new StorageMenuAction(R.drawable.ic_insert_drive_file_black_24dp, "Apps Channel"));
        }
        return actions;
    }
}
