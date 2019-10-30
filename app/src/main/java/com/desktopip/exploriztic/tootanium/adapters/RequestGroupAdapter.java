package com.desktopip.exploriztic.tootanium.adapters;

import android.content.Context;
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
import com.desktopip.exploriztic.tootanium.interfaces.IGroup;
import com.desktopip.exploriztic.tootanium.models.ModRequestGroup;
import com.desktopip.exploriztic.tootanium.utilities.SessionManager;
import com.desktopip.exploriztic.tootanium.volley.GroupServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RequestGroupAdapter extends RecyclerView.Adapter<RequestGroupAdapter.RequestGroupViewHolder> {

    private static final String TAG = "RequestGroupAdapter";

    // Session manager
    SessionManager session;
    // username
    String userName;

    private Context context;
    private List<ModRequestGroup> requestGroupList;

    public RequestGroupAdapter(Context context, List<ModRequestGroup> requestGroupList) {
        this.context = context;
        this.requestGroupList = requestGroupList;

        session = new SessionManager(context);

        //Get user data from session
        HashMap<String, String> user = session.getUserDetails();
        userName = user.get(SessionManager.KEY_USERNAME);
    }

    @NonNull
    @Override
    public RequestGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.group_request_item, parent, false);
        return new RequestGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestGroupViewHolder holder, final int position) {
        holder.user_request_name.setText(requestGroupList.get(position).getUserId());
        String sIsApproved = requestGroupList.get(position).getIsApproved();

        String originDate = requestGroupList.get(position).getRequestDate();
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat formatOut = new SimpleDateFormat("dd MMM yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatIn.parse(originDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String newDate = formatOut.format(calendar.getTime());
        holder.request_date.setText("At "+newDate);

        if(sIsApproved.equals("1")) {
            holder.confirm_request_group.setText("Confirmed");
            holder.confirm_request_group.setEnabled(false);
        }

        holder.remove_request_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.confirm_request_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String confirmText = holder.confirm_request_group.getText().toString();
                if(!confirmText.equals("Confirmed")){
                    GroupServices.confirmRequestGroup(new IGroup() {
                        @Override
                        public void onSuccess(JSONObject response) {
                            Log.d(TAG, "onSuccess: "+response.toString());
                            try {
                                String message = response.getString("message");
                                Toast.makeText(context, ""+message, Toast.LENGTH_SHORT).show();
                                holder.confirm_request_group.setText("Confirmed");
                            } catch (JSONException e) {
                                Toast.makeText(context, "Error: "+e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailed(JSONObject failed) {

                        }

                        @Override
                        public void onError(VolleyError error) {
                            handlingError(error);
                        }
                    }, context, "confirmRequestGroup", userName, requestGroupList.get(position).getUserId(), requestGroupList.get(position).getGroupId());

                } else {
                    //Intent toAdvertiseGroupPage = new Intent(context, GroupActivity.class);
                    //toAdvertiseGroupPage.putExtra("groupName", requestGroupList.get(position).getGroupName());
                    //toAdvertiseGroupPage.putExtra("groupId", requestGroupList.get(position).getGroupId());
                    //context.startActivity(toAdvertiseGroupPage);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return requestGroupList.size();
    }

    public class RequestGroupViewHolder extends RecyclerView.ViewHolder {

        TextView user_request_name, request_date, remove_request_group, confirm_request_group;
        public RequestGroupViewHolder(View itemView) {
            super(itemView);

            user_request_name = itemView.findViewById(R.id.user_request_name);
            request_date = itemView.findViewById(R.id.request_date);
            remove_request_group = itemView.findViewById(R.id.remove_request_group);
            confirm_request_group = itemView.findViewById(R.id.confirm_request_group);
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

    public void searchRequester(List<ModRequestGroup> newRequestList) {
        requestGroupList = new ArrayList<>();
        requestGroupList.addAll(newRequestList);
        notifyDataSetChanged();
    }
}
