package com.desktopip.exploriztic.tootanium.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.desktopip.exploriztic.tootanium.presenter.AdvDeleteGroupPresenter;
import com.desktopip.exploriztic.tootanium.presenter.AdvDeleteGroupPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.AdvDeleteGroupView;
import com.desktopip.exploriztic.tootanium.presenter.AdvUpdateGroupPresenter;
import com.desktopip.exploriztic.tootanium.presenter.AdvUpdateGroupPresenterImp;
import com.desktopip.exploriztic.tootanium.presenter.AdvUpdateGroupView;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import org.json.JSONObject;

import java.util.HashMap;

public class GroupSettings extends AppCompatActivity implements View.OnClickListener
        , AdvUpdateGroupView, AdvDeleteGroupView {

    private static final String TAG = "GroupSettings";

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    Toolbar toolbar;
    TextView settings_admin, settings_group_name;
    RelativeLayout rlGroupAccessMemberSetting, rlNameAndDescriptionSetting, rlDeleteGroup;

    String sGroupId, sGroupName, sGroupDesc, sGroupAccessId;
    Dialog customDialog;
    TextView adv_close_dialog_group, dialog_title, dialog_confirm_message;
    Button adv_btn_save_group, dialog_confirm_cancel, dialog_confirm_ok;
    EditText adv_group_name, adv_group_desc;

    AdvUpdateGroupPresenter advUpdateGroupPresenter;
    AdvDeleteGroupPresenter advDeleteGroupPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_advertise_group_settings);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);

        toolbar = findViewById(R.id.settingsToolBar);
        toolbar.setTitle("Settings");
        toolbar.setSubtitle("Manage your group settings here");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        rlGroupAccessMemberSetting = findViewById(R.id.rlGroupAdminsSetting);
        settings_admin = findViewById(R.id.settings_admin);
        settings_group_name = findViewById(R.id.settings_group_name);
        rlNameAndDescriptionSetting = findViewById(R.id.rlNameAndDescriptionSetting);
        //rlLeaveGroup = findViewById(R.id.rlLeaveGroup);
        rlDeleteGroup = findViewById(R.id.rlDeleteGroup);

        rlGroupAccessMemberSetting.setOnClickListener(this);
        rlNameAndDescriptionSetting.setOnClickListener(this);
        //rlLeaveGroup.setOnClickListener(this);
        rlDeleteGroup.setOnClickListener(this);

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupName = getIntent().getStringExtra("groupName");
        sGroupAccessId = getIntent().getStringExtra("groupAccessId");
        sGroupDesc = getIntent().getStringExtra("groupDesc");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        customDialog = new Dialog(this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        advUpdateGroupPresenter = new AdvUpdateGroupPresenterImp(this, GroupSettings.this);
        advDeleteGroupPresenter = new AdvDeleteGroupPresenterImp(this, GroupSettings.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlGroupAdminsSetting:
                Intent toSettings = new Intent(GroupSettings.this, GroupAccessMemberSetup.class);
                toSettings.putExtra("groupId", sGroupId);
                toSettings.putExtra("groupName", sGroupName);
                toSettings.putExtra("groupAccessId", sGroupAccessId);
                toSettings.putExtra("accessName", settings_admin.getText().toString());
                startActivity(toSettings);
                break;

            case R.id.rlNameAndDescriptionSetting:
                showDialog();
                break;

            case R.id.rlDeleteGroup:
                showAlertDialog("Confirm Delete", "Are you sure want to delete this group?", "Yes Delete", true);
                break;
        }
    }

    public void showDialog() {
        customDialog.setContentView(R.layout.adv_create_group_dialog);
        adv_close_dialog_group = customDialog.findViewById(R.id.adv_close_dialog_group);
        dialog_title = customDialog.findViewById(R.id.dialog_setup_title);
        adv_group_name = customDialog.findViewById(R.id.adv_group_name);
        adv_group_desc = customDialog.findViewById(R.id.adv_group_desc);
        adv_btn_save_group = customDialog.findViewById(R.id.adv_btn_save_group);
        dialog_title.setText("Update Group");
        adv_group_name.setText(sGroupName);
        adv_group_desc.setText(sGroupDesc);

        //Get user setup data from session
        HashMap<String, String> setup = session.getUserDetails();
        final String userName = setup.get(SessionManager.KEY_USERNAME);

        customDialog.setCancelable(false);

        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        customDialog.show();

        adv_close_dialog_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customDialog.cancel();
            }
        });

        adv_btn_save_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String groupName = adv_group_name.getText().toString();
                String groupDesc = adv_group_desc.getText().toString();
                advUpdateGroupPresenter.updateGroup(userName, sGroupId, groupName, groupDesc);
            }
        });
    }

    @Override
    public void showValidationError() {
        Toast.makeText(this, "Please enter all valid requirement", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteGroupSuccess(JSONObject result) {
        Toast.makeText(this, "Group deleted", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void deleteGroupFailed(JSONObject failed) {
        Toast.makeText(this, "Failed "+failed.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void deleteGroupError(VolleyError error) {
        handlingError(error);
    }

    @Override
    public void updateGroupSuccess(JSONObject result) {
        customDialog.cancel();
        Toast.makeText(this, "Group updated", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateGroupFailed(JSONObject failed) {
        Toast.makeText(this, "Failed "+failed.toString(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateGroupError(VolleyError error) {
        handlingError(error);
    }

    private void handlingError(VolleyError error){
        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(this, "Request time-out, it seems like your network is broken or URL setting is wrong ", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(this, "Authentication Failed ", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(this, "Internal Server Error ", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(this, "Unable To Access The Network ", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(this, "There was a problem parsing the package ", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAlertDialog(String title, String message, String button, boolean status) {

        if (status) {

            customDialog.setContentView(R.layout.custom_popup_confirm);
            dialog_confirm_message = customDialog.findViewById(R.id.dialog_confirm_message);
            dialog_confirm_cancel = customDialog.findViewById(R.id.dialog_confirm_cancel);
            dialog_confirm_ok = customDialog.findViewById(R.id.dialog_confirm_ok);
            dialog_confirm_message.setText(message);
            dialog_confirm_ok.setText(button);

            customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customDialog.setCancelable(false);
            customDialog.show();

            dialog_confirm_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    customDialog.dismiss();
                }
            });

            dialog_confirm_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (customDialog.isShowing()) {
                        customDialog.cancel();
                        advDeleteGroupPresenter.deleteGroup(userName, sGroupId);
                    }
                }
            });

        }
    }
}
