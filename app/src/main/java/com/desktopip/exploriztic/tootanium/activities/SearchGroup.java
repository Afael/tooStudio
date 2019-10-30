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
import com.desktopip.exploriztic.tootanium.adapters.SearchGroupAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModGroup;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchGroup extends AppCompatActivity {

    private static final String TAG = "SearchGroup";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    Toolbar search_group_toolbar;
    RecyclerView rc_search_group;
    EditText group_search;

    List<ModGroup> searchGroupList;
    ModGroup modGroup;
    com.desktopip.exploriztic.tootanium.adapters.SearchGroupAdapter SearchGroupAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_group);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        search_group_toolbar = findViewById(R.id.search_group_toolbar);
        rc_search_group = findViewById(R.id.rc_search_group);
        group_search = findViewById(R.id.group_search);
        searchGroupList = new ArrayList<>();

        setSupportActionBar(search_group_toolbar);
        getSupportActionBar().setTitle("Search Group");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search_group_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadGroup();

        group_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String groupSearch = charSequence.toString().toLowerCase();
                List<ModGroup> newList = new ArrayList<>();
                if(searchGroupList.size()>0) {
                    for(ModGroup search : searchGroupList) {
                        if(search.getGroupName().toString().toLowerCase().contains(groupSearch)) {
                            newList.add(search);
                        }
                    }
                    SearchGroupAdapter.searchGroup(newList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void loadGroup() {
        GroupServices.loadAdvertise(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                Log.d(TAG, "onSuccess: " + response.toString());
                try {
                    JSONArray message = response.getJSONArray("message");
                    searchGroupList.clear();
                    for (int i = 0; i < message.length(); i++) {
                        modGroup = new ModGroup();
                        JSONObject data = message.getJSONObject(i);
                        modGroup.setGroupId(data.getString("group_id"));
                        modGroup.setGroupName(data.getString("group_name"));
                        modGroup.setGroupDescription(data.getString("group_description"));
                        modGroup.setCreatedDate(data.getString("created_date"));
                        modGroup.setCreatedBy(data.getString("created_by"));
                        modGroup.setIsActive(data.getString("isActive"));
                        modGroup.setIsJoin(data.getString("is_join"));
                        modGroup.setIsInvite(data.getString("is_invite"));
                        searchGroupList.add(modGroup);
                    }
                    setupRecyclerView(searchGroupList);
                    handlingPage(searchGroupList);
                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(SearchGroup.this, "Error" + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {

            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "getGroupSearch", userName);
    }

    private void setupRecyclerView(List<ModGroup> groupList) {
        SearchGroupAdapter = new SearchGroupAdapter(this, groupList);
        rc_search_group.setLayoutManager(new LinearLayoutManager(this));
        rc_search_group.setAdapter(SearchGroupAdapter);
    }

    private void handlingPage(List<ModGroup> advertiseList) {
        if (advertiseList.size() > 0) {
            //rl_empty.setVisibility(View.GONE);
            rc_search_group.setVisibility(View.VISIBLE);
        } else {
            //rl_empty.setVisibility(View.VISIBLE);
            rc_search_group.setVisibility(View.GONE);
        }
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_search_group.setVisibility(View.GONE);
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
