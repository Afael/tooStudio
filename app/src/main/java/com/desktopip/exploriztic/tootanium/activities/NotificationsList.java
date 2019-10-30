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

public class NotificationsList extends AppCompatActivity {

    private static final String TAG = "NotificationsList";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    RecyclerView rc_notifications;
    List<ModNotificationList> notificationLists;
    NotificationsAdapter notificationsAdapter;

    Toolbar notification_toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_notifications_list);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        notification_toolbar = findViewById(R.id.notification_toolbar);
        notification_toolbar.setTitle("Notifications");
        setSupportActionBar(notification_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rc_notifications = findViewById(R.id.rc_notifications);
        notificationLists = new ArrayList<>();

        loadNotifications();

        notification_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void loadNotifications() {
        NotificationsServices.loadNotifications(new INotifications() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: "+response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    ModNotificationList modNotificationList;
                    for (int i = 0; i < message.length(); i++) {
                        modNotificationList = new ModNotificationList();
                        JSONObject data = message.getJSONObject(i);
                        modNotificationList.setGroupId(data.getString("group_id"));
                        modNotificationList.setGroupName(data.getString("group_name"));
                        modNotificationList.setUserId(data.getString("user_id"));
                        modNotificationList.setTitle(data.getString("title"));
                        modNotificationList.setMessage(data.getString("message"));
                        modNotificationList.setCreateDate(data.getString("created_date"));
                        modNotificationList.setObjectId(data.getString("object_id"));
                        modNotificationList.setIsJoin(data.getString("is_join"));
                        notificationLists.add(modNotificationList);
                    }
                    setupRecyclerView(notificationLists);
                    //adv_count_member_page.setText("Members "+advertiseGroupUserList.size());
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(NotificationsList.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "getNotifications", userName);
    }

    private void setupRecyclerView(List<ModNotificationList> advertiseList) {
        notificationsAdapter = new NotificationsAdapter(this, advertiseList);
        rc_notifications.setLayoutManager(new LinearLayoutManager(this));
        rc_notifications.setAdapter(notificationsAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_notifications.setVisibility(View.GONE);
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
