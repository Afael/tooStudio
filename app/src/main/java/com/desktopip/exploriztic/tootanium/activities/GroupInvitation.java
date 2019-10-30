package com.desktopip.exploriztic.tootanium.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.desktopip.exploriztic.tootanium.adapters.InvitationUserAdapter;
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

public class GroupInvitation extends AppCompatActivity implements View.OnClickListener{

    RecyclerView rc_invitation;
    RelativeLayout adv_invite_via_link;
    EditText contact_search;
    TextView adv_invite_user_action, tvCounter, adv_invite_cancel_action;
    List<ModInvitationUser> invitationUserList;
    InvitationUserAdapter invitationUserAdapter;

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;
    String sGroupId, sGroupName;
    int counter = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adv_user_invitation_list);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupName = getIntent().getStringExtra("groupName");

        invitationUserList = new ArrayList<>();
        rc_invitation = findViewById(R.id.rc_invitation);
        adv_invite_via_link = findViewById(R.id.adv_invite_via_link);
        adv_invite_user_action = findViewById(R.id.adv_invite_user_action);
        adv_invite_cancel_action = findViewById(R.id.adv_invite_cancel_action);
        contact_search = findViewById(R.id.contact_search);
        tvCounter = findViewById(R.id.tvCounter);

        loadData();
        adv_invite_cancel_action.setOnClickListener(this);
        adv_invite_user_action.setOnClickListener(this);
        adv_invite_via_link.setOnClickListener(this);

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
                    invitationUserAdapter.searchContact(newList);
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
                    Toast.makeText(GroupInvitation.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
                Toast.makeText(GroupInvitation.this, ""+failed.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError volleyError) {
                handlingError(volleyError);
            }
        }, this, "getAdvertiseInvitationUser", userName, sGroupId);
    }

    private void addInvitation() {
        GroupServices.addInvitationGroupAdvertise(new IGroup() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String message = response.getString("message");
                    Toast.makeText(GroupInvitation.this, ""+message, Toast.LENGTH_SHORT).show();
                    adv_invite_user_action.setEnabled(false);
                    adv_invite_user_action.setTextColor(getResources().getColor(R.color.color_white_grey));
                    counter = 0;
                    updateCounter(counter);
                    loadData();

                } catch (JSONException e) {
                    //layout_not_found.setVisibility(View.VISIBLE);
                    Toast.makeText(GroupInvitation.this, "Connection Error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(JSONObject failed) {
                Toast.makeText(GroupInvitation.this, ""+failed.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(VolleyError error) {
                handlingError(error);
            }
        }, this, "groupAdvertiseInvitation", userName, invitationUserAdapter.itemSelectedName, sGroupId);
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
        invitationUserAdapter = new InvitationUserAdapter(this, invitationUserList);
        rc_invitation.setLayoutManager(new LinearLayoutManager(this));
        rc_invitation.setAdapter(invitationUserAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.adv_invite_cancel_action:
                finish();
                break;
            case R.id.adv_invite_user_action:
                addInvitation();
                break;
            case R.id.adv_invite_via_link:
                Intent intent = new Intent(this, GroupInvitationViaLink.class);
                intent.putExtra("groupId", sGroupId);
                intent.putExtra("groupName", sGroupName);
                startActivity(intent);
                break;
        }
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
}
