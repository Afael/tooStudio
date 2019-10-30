package com.desktopip.exploriztic.tootanium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.adapters.RequestGroupAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModRequestGroup;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupRequestList extends AppCompatActivity {

    private static final String TAG = "GroupRequestList";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    Toolbar request_group_toolbar;
    RecyclerView rc_request_group;
    EditText request_search;

    List<ModRequestGroup> requestGroupList;
    ModRequestGroup modGroup;
    RequestGroupAdapter requestGroupAdapter;

    String userId, groupName, groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.group_request_list);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        userId = getIntent().getStringExtra("userId");
        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("groupName");

        request_group_toolbar = findViewById(R.id.request_group_toolbar);
        rc_request_group = findViewById(R.id.rc_request_group);
        request_search = findViewById(R.id.request_search);
        requestGroupList = new ArrayList<>();

        setSupportActionBar(request_group_toolbar);
        getSupportActionBar().setTitle("Request Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        request_group_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadRequestGroup();

        request_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String groupSearch = charSequence.toString().toLowerCase();
                List<ModRequestGroup> newList = new ArrayList<>();
                if(requestGroupList.size()>0) {
                    for(ModRequestGroup search : requestGroupList) {
                        if(search.getUserId().toString().toLowerCase().contains(groupSearch)) {
                            newList.add(search);
                        }
                    }
                    requestGroupAdapter.searchRequester(newList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private void loadRequestGroup() {
        GroupServices.loadRequestGroup(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: "+response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    ModRequestGroup modRequestGroup;
                    for (int i = 0; i < message.length(); i++) {
                        modRequestGroup = new ModRequestGroup();
                        JSONObject data = message.getJSONObject(i);
                        modRequestGroup.setGroupId(data.getString("group_id"));
                        modRequestGroup.setGroupName(data.getString("group_name"));
                        modRequestGroup.setUserId(data.getString("user_id"));
                        modRequestGroup.setIsApproved(data.getString("is_approved"));
                        modRequestGroup.setRequestDate(data.getString("created_date"));
                        requestGroupList.add(modRequestGroup);
                    }
                    setupRecyclerView(requestGroupList);
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(GroupRequestList.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "getRequestGroup", userName, groupId);
    }

    private void setupRecyclerView(List<ModRequestGroup> advertiseList) {
        requestGroupAdapter = new RequestGroupAdapter(this, advertiseList);
        rc_request_group.setLayoutManager(new LinearLayoutManager(this));
        rc_request_group.setAdapter(requestGroupAdapter);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_request_group.setVisibility(View.GONE);
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
