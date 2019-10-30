package com.desktopip.exploriztic.tootanium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class GroupJoin extends AppCompatActivity {

    private static final String TAG = "GroupJoin";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    String groupId, groupName, userId, isJoin;
    TextView group_name_join;
    Button group_join_button;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_group_join);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");
        userId = getIntent().getStringExtra("userId");
        isJoin = getIntent().getStringExtra("isJoin");

        //Log.d(TAG, "groupId: "+groupId+"\ngroupName: "+groupName+"\nuserId: "+userId);

        toolbar = findViewById(R.id.main_toolbar);
        toolbar.setTitle("DIP Online Storage");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        group_name_join = findViewById(R.id.group_name_join);
        group_join_button = findViewById(R.id.group_join_button);

        group_name_join.setText(groupName);
        if(isJoin.equals("1")){
            group_join_button.setText("You have joined");
            group_join_button.setEnabled(false);
        }

        group_join_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isJoin.equals("0")) {
                    joinToTheGroup();
                }
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void joinToTheGroup() {
        GroupServices.joinGroup(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: "+response.toString());
                try {
                    String message = response.getString("message");
                    Toast.makeText(GroupJoin.this, ""+message, Toast.LENGTH_SHORT).show();
                    finish();

                } catch (JSONException e) {
                    Toast.makeText(GroupJoin.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
                Log.d(TAG, "onSuccess: "+failed.toString());
                try {
                    String message = failed.getString("message");
                    Toast.makeText(GroupJoin.this, ""+message, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    Toast.makeText(GroupJoin.this, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "joinGroup", userName, groupId);
    }

    private void handlingError(VolleyError error) {

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
