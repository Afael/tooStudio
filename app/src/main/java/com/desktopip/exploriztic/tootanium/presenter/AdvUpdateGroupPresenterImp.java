package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONObject;

public class AdvUpdateGroupPresenterImp implements AdvUpdateGroupPresenter {

    AdvUpdateGroupView advUpdateGroupView;
    Context context;

    public  AdvUpdateGroupPresenterImp(Context context, AdvUpdateGroupView advUpdateGroupView) {
        this.context = context;
        this.advUpdateGroupView = advUpdateGroupView;
    }

    @Override
    public void updateGroup(String username, String groupId, String groupName, String groupDesc) {
        if(TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupDesc)) {
            advUpdateGroupView.showValidationError();
        }
        else {
            GroupServices.updateGroupAdvertise(new IGroup() {
                @Override
                public void onSuccess(JSONObject response) {
                    advUpdateGroupView.updateGroupSuccess(response);
                }

                @Override
                public void onFailed(JSONObject failed) {
                    advUpdateGroupView.updateGroupFailed(failed);
                }

                @Override
                public void onError(VolleyError error) {
                    advUpdateGroupView.updateGroupError(error);
                }
            }, context, "updateGroupAdvertise", username, groupId, groupName, groupDesc);
        }
    }
}
