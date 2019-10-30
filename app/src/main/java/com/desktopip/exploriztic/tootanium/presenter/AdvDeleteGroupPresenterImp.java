package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;

import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONObject;

public class AdvDeleteGroupPresenterImp implements AdvDeleteGroupPresenter {

    AdvDeleteGroupView advDeleteGroupView;
    Context context;

    public AdvDeleteGroupPresenterImp(Context context, AdvDeleteGroupView advDeleteGroupView) {
        this.context = context;
        this.advDeleteGroupView = advDeleteGroupView;
    }

    @Override
    public void deleteGroup(String userName, String groupId) {
        GroupServices.deleteGroupAdvertise(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                advDeleteGroupView.deleteGroupSuccess(response);
            }

            @Override
            public void onFailed(JSONObject failed) {
                advDeleteGroupView.deleteGroupFailed(failed);
            }

            @Override
            public void onError(VolleyError error) {
                advDeleteGroupView.deleteGroupError(error);
            }
        }, context, "deleteGroupAdvertise", userName, groupId);
    }
}
