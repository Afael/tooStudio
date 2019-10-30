package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.models.ModAdvertiseGroupUser;

import java.util.List;

public class GroupUserAdapter extends RecyclerView.Adapter<GroupUserAdapter.AdvertiseGroupUserViewHolder>{

    Context context;
    List<ModAdvertiseGroupUser> modAdvertiseGroupUserList;

    public GroupUserAdapter(Context context, List<ModAdvertiseGroupUser> modAdvertiseGroupUserList) {
        this.context = context;
        this.modAdvertiseGroupUserList = modAdvertiseGroupUserList;
    }

    @NonNull
    @Override
    public AdvertiseGroupUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.adv_group_user_item, parent, false);
        return new AdvertiseGroupUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdvertiseGroupUserViewHolder holder, int position) {
        holder.adv_user_group_name.setText(modAdvertiseGroupUserList.get(position).getUserId());
    }

    @Override
    public int getItemCount() {
        return modAdvertiseGroupUserList.size();
    }

    public static class AdvertiseGroupUserViewHolder extends RecyclerView.ViewHolder {

        TextView adv_user_group_name;

        public AdvertiseGroupUserViewHolder(View itemView) {
            super(itemView);

            adv_user_group_name = itemView.findViewById(R.id.adv_user_group_name);
        }
    }
}
