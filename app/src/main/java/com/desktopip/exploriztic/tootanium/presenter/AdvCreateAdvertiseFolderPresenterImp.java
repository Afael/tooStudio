package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.volley.FileExplorerAdvertiseFolderServices;

import org.json.JSONObject;

public class AdvCreateAdvertiseFolderPresenterImp implements AdvCreateAdvertiseFolderPresenter {

    AdvCreateAdvertiseFolderView createAdvertiseFolderView;
    Context context;

    public AdvCreateAdvertiseFolderPresenterImp(Context context, AdvCreateAdvertiseFolderView createAdvertiseFolderView){
        this.context = context;
        this.createAdvertiseFolderView = createAdvertiseFolderView;
    }

    @Override
    public void createAdvertise(String username, String password, String folderName, String groupId) {
        if(TextUtils.isEmpty(folderName)) {
            createAdvertiseFolderView.showValidationError();
        } else {
            FileExplorerAdvertiseFolderServices.createAdvertiseFolder(new IGroup() {
                @Override
                public void onSuccess(JSONObject response) {
                    createAdvertiseFolderView.createAdvertiseSuccess(response);
                }

                @Override
                public void onFailed(JSONObject failed) {
                    createAdvertiseFolderView.createAdvertiseFailed(failed);
                }

                @Override
                public void onError(VolleyError error) {
                    createAdvertiseFolderView.createAdvertiseError(error);
                }
            }, context, "createAdvertiseFolder", username, password, folderName, groupId);
        }
    }
}
