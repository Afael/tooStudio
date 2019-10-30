package com.desktopip.exploriztic.tootanium.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.GroupInvitation;
import com.desktopip.exploriztic.tootanium.adapters.GroupUserAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModAdvertiseGroupUser;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressLint("ValidFragment")
public class FragGroupUser extends Fragment implements View.OnClickListener{

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;
    String sGroupId;
    String sGroupName;

    TextView adv_count_member_page;
    RecyclerView rc_adv_group_member;
    RelativeLayout add_user_group;
    List<ModAdvertiseGroupUser> advertiseGroupUserList;
    GroupUserAdapter groupUserAdapter;

    @SuppressLint("ValidFragment")
    public FragGroupUser(String sGroupId, String sGroupName) {
        this.sGroupId = sGroupId;
        this.sGroupName = sGroupName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_adv_user_group, container, false);

        rc_adv_group_member = view.findViewById(R.id.rc_adv_group_member);
        adv_count_member_page = view.findViewById(R.id.adv_count_member_page);
        add_user_group = view.findViewById(R.id.add_user_group);

        session = new SessionManager(getActivity());

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        advertiseGroupUserList = new ArrayList<>();
        loadMemberList();

        add_user_group.setOnClickListener(this);
        //group_user_cancel_action.setOnClickListener(this);
        return view;
    }

    private void loadMemberList() {

        GroupServices.loadAdvertiseGroupUser(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    ModAdvertiseGroupUser modAdvertiseGroupUser = null;
                    advertiseGroupUserList.clear();
                    for(int i = 0; i < message.length(); i++) {
                        modAdvertiseGroupUser = new ModAdvertiseGroupUser();
                        JSONObject data = message.getJSONObject(i);
                        modAdvertiseGroupUser.setUserId(data.getString("user_id"));
                        modAdvertiseGroupUser.setGroupName(data.getString("group_name"));
                        modAdvertiseGroupUser.setCreatedBy(data.getString("created_by"));
                        advertiseGroupUserList.add(modAdvertiseGroupUser);
                    }
                    setupRecyclerView(advertiseGroupUserList);
                    adv_count_member_page.setText("Members "+advertiseGroupUserList.size());
                    if(userName.equals(modAdvertiseGroupUser.getCreatedBy())) {
                        add_user_group.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }, getActivity(), "getAdvertiseGroupUser", userName, sGroupId);
    }

    private void setupRecyclerView(List<ModAdvertiseGroupUser> advertiseList){
        groupUserAdapter = new GroupUserAdapter(getActivity(), advertiseList);
        rc_adv_group_member.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_adv_group_member.setAdapter(groupUserAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_adv_group_member.setVisibility(View.GONE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(getActivity(), ""+R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getActivity(), ""+R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getActivity(), ""+R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(getActivity(), ""+R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getActivity(), ""+R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_user_group:
                Intent toInvitationList = new Intent(getActivity(), GroupInvitation.class);
                toInvitationList.putExtra("groupId", sGroupId);
                toInvitationList.putExtra("groupName", sGroupName);
                startActivity(toInvitationList);
                break;
        }
    }
}
