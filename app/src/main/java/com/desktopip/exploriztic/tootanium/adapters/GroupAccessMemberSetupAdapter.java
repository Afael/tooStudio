package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.GroupAccessMemberSetup;
import com.desktopip.exploriztic.tootanium.models.ModInvitationUser;

import java.util.ArrayList;
import java.util.List;

public class GroupAccessMemberSetupAdapter extends RecyclerView.Adapter<GroupAccessMemberSetupAdapter.GroupAccessMemberSetupViewHolder>{

    private static final String TAG = "GroupAccessMemberSetup";

    private Context context;
    private List<ModInvitationUser> invitationUserList;
    public ArrayList<Integer> checkBoxList;
    public ArrayList<String> itemSelectedName;
    GroupAccessMemberSetup groupInvitation;

    public GroupAccessMemberSetupAdapter(Context context, List<ModInvitationUser> invitationUserList){
        this.context = context;
        this.invitationUserList = invitationUserList;
        checkBoxList = new ArrayList<>();
        itemSelectedName = new ArrayList<>();
        this.groupInvitation = (GroupAccessMemberSetup) context;
    }

    @NonNull
    @Override
    public GroupAccessMemberSetupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adv_user_invitation_item, parent, false);
        GroupAccessMemberSetupAdapter.GroupAccessMemberSetupViewHolder groupAccessMemberSetupViewHolder = new GroupAccessMemberSetupAdapter.GroupAccessMemberSetupViewHolder(view, groupInvitation);
        return groupAccessMemberSetupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAccessMemberSetupViewHolder holder, final int position) {
        holder.adv_invite_user_name.setText(invitationUserList.get(position).getUserId());
        holder.adv_invite_cbCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    checkBoxList.add(position);
                    itemSelectedName.add(invitationUserList.get(position).getUserId());
                    Log.d(TAG, "onCheckedChanged: " + itemSelectedName);
                } else {
                    checkBoxList.remove(checkBoxList.indexOf(position));
                    itemSelectedName.remove(invitationUserList.get(position).getUserId());
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return invitationUserList.size();
    }

    public class GroupAccessMemberSetupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView adv_invite_user_name;
        CheckBox adv_invite_cbCheck;
        GroupAccessMemberSetup groupAccessMemberSetup;
        public GroupAccessMemberSetupViewHolder(View itemView, GroupAccessMemberSetup groupAccessMemberSetup) {
            super(itemView);
            adv_invite_user_name = itemView.findViewById(R.id.adv_invite_user_name);
            adv_invite_cbCheck = itemView.findViewById(R.id.adv_invite_cbCheck);
            this.groupAccessMemberSetup = groupAccessMemberSetup;
            adv_invite_cbCheck.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            groupAccessMemberSetup.prepareSelected(view, getAdapterPosition());
        }
    }

    public void searchContact(List<ModInvitationUser> newInvitationUserList) {
        invitationUserList = new ArrayList<>();
        invitationUserList.addAll(newInvitationUserList);
        notifyDataSetChanged();
    }
}
