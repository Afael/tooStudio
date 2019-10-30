package com.desktopip.exploriztic.tootanium.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONObject;

public class AdvCreateGroupPresenterImp implements AdvCreateGroupPresenter {

    AdvCreateGroupView createGroupView;

    Context context;

    public AdvCreateGroupPresenterImp(AdvCreateGroupView createGroupView, Context context){
        this.createGroupView = createGroupView;
        this.context = context;
    }

    @Override
    public void createGroup(String username, String groupName, String groupDesc) {

        if(TextUtils.isEmpty(username) || TextUtils.isEmpty(groupName) || TextUtils.isEmpty(groupDesc)) {
            createGroupView.showValidationError();
        }
        else {
            GroupServices.createGroupAdvertise(new IGroup() {
                @Override
                public void onSuccess(JSONObject response) {
                    createGroupView.createGroupSuccess(response);
                }

                @Override
                public void onFailed(JSONObject failed) {
                    createGroupView.createGroupFailed(failed);
                }

                @Override
                public void onError(VolleyError error) {
                    createGroupView.createGroupError(error);
                }
            }, context, "createGroupAdvertise", username, groupName, groupDesc);
        }
    }
}
