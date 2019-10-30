package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.desktopip.exploriztic.tootanium.activities.GroupActivity;
import com.desktopip.exploriztic.tootanium.activities.GroupJoin;
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModGroup;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.SearchGroupViewHolder> {

    private static final String TAG = "SearchGroupAdapter";

    // Session manager
    SessionManager session;
    // username
    String userName;

    private Context context;
    private List<ModGroup> groupList;

    public SearchGroupAdapter(Context context, List<ModGroup> groupList) {
        this.context = context;
        this.groupList = groupList;

        session = new SessionManager(context);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
    }

    @NonNull
    @Override
    public SearchGroupAdapter.SearchGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.search_group_item, parent, false);
        return new SearchGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SearchGroupAdapter.SearchGroupViewHolder holder, final int position) {
        holder.search_group_name.setText(groupList.get(position).getGroupName());
        holder.search_group_desc.setText(groupList.get(position).getGroupDescription());
        String sIsJoin = groupList.get(position).getIsJoin();
        String sIsInvite = groupList.get(position).getIsInvite();

        if(sIsJoin.equals("1")) {
            holder.search_join_group.setText("View");
        }

        if(sIsInvite.equals("1")) {
            holder.search_join_group.setText("Accept");
        }

        holder.search_join_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.search_join_group.getText().equals("View")) {
                    Intent toAdvertiseGroupPage = new Intent(context, GroupActivity.class);
                    toAdvertiseGroupPage.putExtra("groupName", groupList.get(position).getGroupName());
                    toAdvertiseGroupPage.putExtra("groupId", groupList.get(position).getGroupId());
                    context.startActivity(toAdvertiseGroupPage);
                }
                else if(holder.search_join_group.getText().equals("Accept")){
                    Intent toAdvertiseGroupPage = new Intent(context, GroupJoin.class);
                    toAdvertiseGroupPage.putExtra("groupName", groupList.get(position).getGroupName());
                    toAdvertiseGroupPage.putExtra("groupId", groupList.get(position).getGroupId());
                    context.startActivity(toAdvertiseGroupPage);
                } else {
                    GroupServices.requestGroup(new IGroup() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, "onSuccess: "+response.toString());
                            try {
                                String message = response.getString("message");
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                holder.search_join_group.setText("View");
                            } catch (JSONException e) {
                                Toast.makeText(context, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailed(JSONObject failed) {
                            Log.d(TAG, "onFailed: "+failed.toString());
                            try {
                                String message = failed.getString("message");
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                Toast.makeText(context, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }
                    }, context, "requestJoinGroup", userName, groupList.get(position).getGroupId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public class SearchGroupViewHolder extends RecyclerView.ViewHolder{

        TextView search_group_name, search_group_desc, search_join_group;

        public SearchGroupViewHolder(View itemView) {
            super(itemView);

            search_group_name = itemView.findViewById(R.id.search_group_name);
            search_group_desc = itemView.findViewById(R.id.search_group_desc);
            search_join_group = itemView.findViewById(R.id.search_join_group);
        }
    }

    private void handlingError(VolleyError error) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(context, "" + R.string.error_timeout, Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "" + R.string.error_auth, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(context, "" + R.string.error_server, Toast.LENGTH_SHORT).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(context, "" + R.string.error_network, Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(context, "" + R.string.error_parse, Toast.LENGTH_SHORT).show();
        }
    }

    public void searchGroup(List<ModGroup> newInvitationUserList) {
        groupList = new ArrayList<>();
        groupList.addAll(newInvitationUserList);
        notifyDataSetChanged();
    }
}
