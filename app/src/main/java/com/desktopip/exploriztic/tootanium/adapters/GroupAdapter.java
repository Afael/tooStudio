package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.GroupActivity;
import com.desktopip.exploriztic.tootanium.activities.GroupSettings;
import com.desktopip.exploriztic.tootanium.models.ModAdvertise;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jayus on 01/08/2018.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.AdvertiseViewHolder> {

    // Session manager
    SessionManager session;
    // username
    String userName;
    // password
    String password;

    private Context context;
    private List<ModAdvertise> advertiseList;

    // Use for fragment class
    Fragment fragment;

    public GroupAdapter(Context context, List<ModAdvertise> advertiseList, Fragment fragment){
        this.context = context;
        this.advertiseList = advertiseList;
        this.fragment = fragment;
        session = new SessionManager(context);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
        password = user.get(SessionManager.KEY_PASSWORD);
    }

    @NonNull
    @Override
    public AdvertiseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_advertise_item, parent, false);
        return new AdvertiseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertiseViewHolder holder, final int position) {
        holder.adv_group_name.setText(advertiseList.get(position).getAdvGroupName());
        holder.adv_group_date.setText(advertiseList.get(position).getAdvGroupCreateDate());
        holder.adv_group_description.setText(advertiseList.get(position).getAdvGroupDesc());

        String youAs = advertiseList.get(position).getAdvGroupCreatedBy();
        Log.d("youAs", "AS: "+youAs+"\nlogin AS: "+userName);
        if(youAs.equals(userName)) {
            holder.group_as.setText("You are Administrator");
        } else {
            holder.group_as.setText("You are member");
            holder.adv_setting_button.setVisibility(View.GONE);
        }

        // Reload list from fragment
        // ((FragGroup) fragment).loadData();

        holder.adv_setting_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAdvertiseGroupSettings = new Intent(context, GroupSettings.class);
                toAdvertiseGroupSettings.putExtra("groupName", advertiseList.get(position).getAdvGroupName());
                toAdvertiseGroupSettings.putExtra("groupId", advertiseList.get(position).getAdvGroupId());
                toAdvertiseGroupSettings.putExtra("groupDesc", advertiseList.get(position).getAdvGroupDesc());
                toAdvertiseGroupSettings.putExtra("groupAccessId", advertiseList.get(position).getGroupAccessId());
                context.startActivity(toAdvertiseGroupSettings);
            }
        });

        holder.adv_view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toAdvertiseGroupPage = new Intent(context, GroupActivity.class);
                toAdvertiseGroupPage.putExtra("groupName", advertiseList.get(position).getAdvGroupName());
                toAdvertiseGroupPage.putExtra("groupId", advertiseList.get(position).getAdvGroupId());
                toAdvertiseGroupPage.putExtra("groupAccessId", advertiseList.get(position).getGroupAccessId());
                context.startActivity(toAdvertiseGroupPage);
            }
        });

    }

    @Override
    public int getItemCount() {
        return advertiseList.size();
    }

    public static class AdvertiseViewHolder extends RecyclerView.ViewHolder {

        LinearLayout adv_content;

        TextView adv_group_name, adv_group_date, adv_group_description, adv_view_button
                , adv_invite_button, adv_setting_button, group_as;

        public AdvertiseViewHolder(View itemView) {
            super(itemView);

            adv_content = itemView.findViewById(R.id.adv_content);
            adv_group_name = itemView.findViewById(R.id.adv_group_name);
            adv_group_date = itemView.findViewById(R.id.adv_group_date);
            adv_group_description = itemView.findViewById(R.id.adv_group_description);
            adv_view_button = itemView.findViewById(R.id.adv_view_button);
            adv_invite_button = itemView.findViewById(R.id.group_as);
            adv_setting_button = itemView.findViewById(R.id.adv_setting_button);
            group_as = itemView.findViewById(R.id.group_as);
        }
    }

    public void searchContact(List<ModAdvertise> newAdvertiseList) {
        advertiseList = new ArrayList<>();
        advertiseList.addAll(newAdvertiseList);
        notifyDataSetChanged();
    }
}
