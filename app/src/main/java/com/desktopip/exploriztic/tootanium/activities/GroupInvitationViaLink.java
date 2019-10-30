package com.desktopip.exploriztic.tootanium.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

public class GroupInvitationViaLink extends AppCompatActivity {

    // Session manager
    SessionManager session;
    // Base URL
    String baseUrl;
    // password
    String password;

    String pathUrlJoinBase64;

    Button adv_share_via_link_button;
    TextView adv_invite_group_name_via_link, adv_invite_group_url_via_link;

    String sGroupId, sGroupName;
    String PART_URL_JOIN = "rest-api/join.php?";

    Toolbar shareLinkToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_advertise_group_invitation_via_link);

        session = new SessionManager(this);

        //Get user data from session
        HashMap<String, String> user =  session.getUserDetails();
        baseUrl = user.get(SessionManager.BASE_URL);
        password = user.get(SessionManager.KEY_PASSWORD);

        shareLinkToolbar = findViewById(R.id.main_toolbar);
        shareLinkToolbar.setTitle("Invite to Group via Link");
        setSupportActionBar(shareLinkToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        adv_share_via_link_button = findViewById(R.id.adv_share_via_link_button);
        adv_invite_group_name_via_link = findViewById(R.id.group_name_join);
        adv_invite_group_url_via_link = findViewById(R.id.adv_invite_group_url_via_link);

        sGroupId = getIntent().getStringExtra("groupId");
        sGroupName = getIntent().getStringExtra("groupName");
        adv_invite_group_name_via_link.setText(sGroupName);
        PART_URL_JOIN += sGroupId;

        try {
            byte[] bytPathUrlJoin = PART_URL_JOIN.getBytes("UTF-8");
            pathUrlJoinBase64 = Base64.encodeToString(bytPathUrlJoin, Base64.DEFAULT);
            adv_invite_group_url_via_link.setText(baseUrl+pathUrlJoinBase64+sGroupId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        adv_share_via_link_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String decodePathUrlJoinBase64 = new String(Base64.decode(pathUrlJoinBase64, Base64.DEFAULT));
                shareTextUrl(GroupInvitationViaLink.this, "Share Link to Join Group", decodePathUrlJoinBase64);
            }
        });

        shareLinkToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Method to share either text or URL.
    private void shareTextUrl(Context context, String titleShare, String urlShare) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

        share.putExtra(Intent.EXTRA_SUBJECT, titleShare);
        share.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(urlShare));

        context.startActivity(Intent.createChooser(share, "Share link!"));
    }
}
