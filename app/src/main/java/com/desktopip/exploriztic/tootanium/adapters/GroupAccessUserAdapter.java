package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.ModGroupAccessUser;

import java.util.List;

public class GroupAccessUserAdapter  extends RecyclerView.Adapter<GroupAccessUserAdapter.GroupAccessUserViewHolder>{

    private Context context;
    private List<ModGroupAccessUser> groupAccessUserList;

    public GroupAccessUserAdapter(Context context, List<ModGroupAccessUser> groupAccessUserList) {
        this.context = context;
        this.groupAccessUserList = groupAccessUserList;
    }

    @NonNull
    @Override
    public GroupAccessUserAdapter.GroupAccessUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.group_access_user_item,parent, false);
        return new GroupAccessUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAccessUserAdapter.GroupAccessUserViewHolder holder, int position) {
        holder.groupAccessUserName.setText(groupAccessUserList.get(position).getUserId());
        holder.groupAccessUserDescription.setText(groupAccessUserList.get(position).getGroupAccessName());
    }

    @Override
    public int getItemCount() {
        return groupAccessUserList.size();
    }

    public class GroupAccessUserViewHolder extends RecyclerView.ViewHolder {
        LinearLayout groupAccessUserItem;
        TextView groupAccessUserName, groupAccessUserDescription;
        public GroupAccessUserViewHolder(View itemView) {
            super(itemView);

            groupAccessUserItem = itemView.findViewById(R.id.groupAccessUserItem);
            groupAccessUserName = itemView.findViewById(R.id.groupAccessUserName);
            groupAccessUserDescription = itemView.findViewById(R.id.groupAccessUserDescription);

        }
    }
}
