package com.desktopip.exploriztic.tootanium.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.NotificationsAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.INotifications;
import com.desktopip.exploriztic.tootanium.models.ModNotificationList;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.NotificationsServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragGroupNotifications extends Fragment {

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    RecyclerView rc_group_notifications;
    List<ModNotificationList> notificationLists;
    NotificationsAdapter notificationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view;
        view = inflater.inflate(R.layout.frag_group_notifications_list, container, false);

        session = new SessionManager(getActivity());

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        rc_group_notifications = view.findViewById(R.id.rc_group_notifications);
        notificationLists = new ArrayList<>();

        loadNotifications();

        return view;
    }

    public void loadNotifications() {
        NotificationsServices.loadNotifications(new INotifications() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    ModNotificationList modNotificationList;
                    for(int i = 0; i < message.length(); i++) {
                        modNotificationList = new ModNotificationList();
                        JSONObject data = message.getJSONObject(i);
                        modNotificationList.setGroupId(data.getString("group_id"));
                        modNotificationList.setGroupName(data.getString("group_name"));
                        modNotificationList.setUserId(data.getString("user_id"));
                        modNotificationList.setTitle(data.getString("title"));
                        modNotificationList.setMessage(data.getString("message"));
                        modNotificationList.setCreateDate(data.getString("created_date"));
                        //modAdvertiseGroupUser.setCreatedBy(data.getString("count_member"));
                        notificationLists.add(modNotificationList);
                    }
                    setupRecyclerView(notificationLists);
                    //adv_count_member_page.setText("Members "+advertiseGroupUserList.size());
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(getActivity(), "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {

            }
        }, getActivity(), "getNotifications", userName);
    }

    private void setupRecyclerView(List<ModNotificationList> advertiseList){
        notificationsAdapter = new NotificationsAdapter(getActivity(), advertiseList);
        rc_group_notifications.setLayoutManager(new LinearLayoutManager(getActivity()));
        rc_group_notifications.setAdapter(notificationsAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_group_notifications.setVisibility(View.GONE);
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

}
