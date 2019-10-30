package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.activities.GroupAccessMemberSetup;
import com.desktopip.exploriztic.tootanium.models.ModGroupAccess;

import java.util.List;

public class GroupAccessAdapter extends RecyclerView.Adapter<GroupAccessAdapter.GroupAccessViewHolder>{

    private Context context;
    private List<ModGroupAccess> groupAccessList;
    String sGroupId;

    public GroupAccessAdapter(Context context, List<ModGroupAccess> groupAccessList, String sGroupId) {
        this.context = context;
        this.groupAccessList = groupAccessList;
        this.sGroupId = sGroupId;
    }

    @NonNull
    @Override
    public GroupAccessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.group_access_item, parent, false);
        return new GroupAccessViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAccessViewHolder holder, final int position) {
        holder.groupAccessName.setText(groupAccessList.get(position).getGroupAccessName());
        holder.groupAccessDescription.setText(groupAccessList.get(position).getGroupAccessDescription());

        holder.groupAccessItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGroupAccessUser = new Intent(context, GroupAccessMemberSetup.class);
                toGroupAccessUser.putExtra("groupId", sGroupId);
                toGroupAccessUser.putExtra("groupAccessId", groupAccessList.get(position).getGroupAccessId());
                toGroupAccessUser.putExtra("accessName", groupAccessList.get(position).getGroupAccessName());
                context.startActivity(toGroupAccessUser);
            }
        });
    }

    public int getItemCount() {
        return groupAccessList.size();
    }

    public class GroupAccessViewHolder extends RecyclerView.ViewHolder{

        LinearLayout groupAccessItem;
        TextView groupAccessName, groupAccessDescription;
        public GroupAccessViewHolder(View itemView) {
            super(itemView);

            groupAccessItem = itemView.findViewById(R.id.groupAccessItem);
            groupAccessName = itemView.findViewById(R.id.groupAccessName);
            groupAccessDescription = itemView.findViewById(R.id.groupAccessDescription);
        }
    }
}
