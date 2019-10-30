package com.desktopip.exploriztic.tootanium.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.desktopip.exploriztic.tootanium.adapters.GroupAccessMemberSetupAdapter;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModInvitationUser;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GroupAccessMemberSetup extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "GroupAccessMemberSetup";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    TextView adv_invite_user_action, textAddMember,adv_invite_cancel_action, tvCounter;
    RecyclerView rc_invitation;
    EditText contact_search;
    List<ModInvitationUser> invitationUserList;
    GroupAccessMemberSetupAdapter groupAccessMemberSetupAdapter;

    String sGroupId, sGroupAccessId, sAccessName;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_user_invitation_list);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupAccessId = getIntent().getStringExtra("groupAccessId");
        sAccessName = getIntent().getStringExtra("accessName");
        invitationUserList = new ArrayList<>();

        textAddMember = findViewById(R.id.textAddMember);
        adv_invite_user_action = findViewById(R.id.adv_invite_user_action);
        adv_invite_cancel_action = findViewById(R.id.adv_invite_cancel_action);
        contact_search = findViewById(R.id.contact_search);
        tvCounter = findViewById(R.id.tvCounter);
        rc_invitation = findViewById(R.id.rc_invitation);

        textAddMember.setText(sAccessName);

        loadData();
        adv_invite_cancel_action.setOnClickListener(this);
        adv_invite_user_action.setOnClickListener(this);

        contact_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String userSearch = charSequence.toString().toLowerCase();
                List<ModInvitationUser> newList = new ArrayList<>();
                if(invitationUserList.size()>0) {
                    for(ModInvitationUser search : invitationUserList) {
                        if(search.getUserId().toString().toLowerCase().contains(userSearch)) {
                            newList.add(search);
                        }
                    }
                    groupAccessMemberSetupAdapter.searchContact(newList);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    public void loadData() {
        GroupServices.loadAdvertiseGroupUser(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray message = response.getJSONArray("message");
                    ModInvitationUser modInvitationUser;
                    invitationUserList.clear();
                    for(int i = 0; i < message.length(); i++) {
                        modInvitationUser = new ModInvitationUser();
                        JSONObject data = message.getJSONObject(i);
                        modInvitationUser.setUserId(data.getString("user_id"));
                        //modAdvertiseGroupUser.setCreatedBy(data.getString("count_member"));
                        invitationUserList.add(modInvitationUser);
                    }
                    setupRecyclerView(invitationUserList);

                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(GroupAccessMemberSetup.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
                Toast.makeText(GroupAccessMemberSetup.this, ""+failed.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }, this, "getGroupAccessSettingUser", userName, sGroupId);
    }

    private void handlingError(VolleyError error) {
        //adv_layout_not_found.setVisibility(View.VISIBLE);
        rc_invitation.setVisibility(View.GONE);
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, ""+R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(this, ""+R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(this, ""+R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, ""+R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, ""+R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }

    private void setupRecyclerView(List<ModInvitationUser> invitationUserList){
        groupAccessMemberSetupAdapter = new GroupAccessMemberSetupAdapter(this, invitationUserList);
        rc_invitation.setLayoutManager(new LinearLayoutManager(this));
        rc_invitation.setAdapter(groupAccessMemberSetupAdapter);
    }

    public void prepareSelected(View view, int position){
        if(((CheckBox)view).isChecked()){
            counter = counter+1;
            updateCounter(counter);
        } else {
            counter = counter-1;
            updateCounter(counter);
        }
    }

    public void updateCounter(int counter) {
        if(counter == 0) {
            tvCounter.setText("0 selected");
            adv_invite_user_action.setEnabled(false);
            adv_invite_user_action.setTextColor(getResources().getColor(R.color.color_white_grey));
        } else {
            tvCounter.setText(counter+ " selected");
            adv_invite_user_action.setEnabled(true);
            adv_invite_user_action.setTextColor(getResources().getColor(R.color.color_blue));
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adv_invite_cancel_action:
                finish();
                break;
        }
    }
}
