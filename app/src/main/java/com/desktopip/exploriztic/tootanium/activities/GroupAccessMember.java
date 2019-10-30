package com.desktopip.exploriztic.tootanium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.GroupAccessAdapter;
import com.desktopip.exploriztic.tootanium.adapters.GroupAccessUserAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModGroupAccess;
import com.desktopip.exploriztic.tootanium.models.ModGroupAccessUser;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GroupAccessMember extends AppCompatActivity {

    private static final String TAG = "GroupAccess";

    SessionManager sessionManager;

    // user session
    String userName, sGroupId, sGroupName;

    Toolbar memberSettingsToolBar;
    RecyclerView rcGroupAccess, rcGroupAccessUser;

    List<ModGroupAccess> groupAccessList;
    List<ModGroupAccessUser> groupAccessUserList;
    ModGroupAccess modGroupAccess;
    ModGroupAccessUser modGroupAccessUser;
    GroupAccessAdapter groupAccessAdapter;
    GroupAccessUserAdapter groupAccessUserAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_access_member_settings);

        sessionManager = new SessionManager(this);

        Map<String, String> userDetail = new HashMap();
        userDetail = sessionManager.getUserDetails();
        userName = userDetail.get(SessionManager.KEY_USERNAME);

        rcGroupAccess = findViewById(R.id.rcGroupAccess);
        rcGroupAccessUser = findViewById(R.id.rcGroupAccessUser);
        memberSettingsToolBar = findViewById(R.id.memberSettingsToolBar);
        memberSettingsToolBar.setTitle("Group Access Settings");
        memberSettingsToolBar.setSubtitle("Manage access here");
        setSupportActionBar(memberSettingsToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        memberSettingsToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        groupAccessList = new ArrayList<>();
        groupAccessUserList = new ArrayList<>();

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupName = getIntent().getStringExtra("groupName");

        loadGroupAccess();
        loadGroupAccessUser();
    }

    private void loadGroupAccess() {

        GroupServices.loadGroupAccess(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: "+response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    groupAccessList.clear();
                    for (int i = 0; i < message.length(); i++) {
                        modGroupAccess = new ModGroupAccess();
                        JSONObject data = message.getJSONObject(i);
                        modGroupAccess.setGroupAccessId(data.getString("group_access_id"));
                        modGroupAccess.setGroupAccessName(data.getString("group_access_name"));
                        modGroupAccess.setGroupAccessDescription(data.getString("group_access_description"));
                        modGroupAccess.setCreatedDate(data.getString("created_date"));
                        modGroupAccess.setCreatedBy(data.getString("created_by"));
                        groupAccessList.add(modGroupAccess);
                    }
                    setupRecyclerView(groupAccessList, sGroupId);
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(GroupAccessMember.this, "Error Json"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "getGroupAccess", userName);

    }

    private void loadGroupAccessUser() {
        GroupServices.loadGroupAccessUsers(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: Users "+response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    groupAccessUserList.clear();
                    for (int i = 0; i < message.length(); i++) {
                        modGroupAccessUser = new ModGroupAccessUser();
                        JSONObject data = message.getJSONObject(i);
                        modGroupAccessUser.setUserId(data.getString("user_id"));
                        modGroupAccessUser.setGroupAccessName(data.getString("group_access_name"));
                        groupAccessUserList.add(modGroupAccessUser);
                    }
                    setupRecyclerViewUserAccess(groupAccessUserList);
                    //handlingPage(advertiseList);
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(GroupAccessMember.this, "Error Json"+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "getGroupAccessUsers", userName, sGroupId);
    }

    private void setupRecyclerView(List<ModGroupAccess> advertiseList, String sGroupId) {
        groupAccessAdapter = new GroupAccessAdapter(this, advertiseList, sGroupId);
        rcGroupAccess.setLayoutManager(new LinearLayoutManager(this));
        rcGroupAccess.setAdapter(groupAccessAdapter);
    }

    private void setupRecyclerViewUserAccess(List<ModGroupAccessUser> groupAccessUserList) {
        groupAccessUserAdapter = new GroupAccessUserAdapter(this, groupAccessUserList);
        rcGroupAccessUser.setLayoutManager(new LinearLayoutManager(this));
        rcGroupAccessUser.setAdapter(groupAccessUserAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rcGroupAccess.setVisibility(View.GONE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, "" + R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(this, "" + R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(this, "" + R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, "" + R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, "" + R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }
}
