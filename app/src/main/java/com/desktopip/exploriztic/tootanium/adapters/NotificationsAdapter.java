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
import com.desktopip.exploriztic.tootanium.activities.GroupJoin;
import com.desktopip.exploriztic.tootanium.activities.GroupRequestList;
import com.desktopip.exploriztic.tootanium.models.ModNotificationList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>{

    Context context;
    List<ModNotificationList> notificationLists;

    public NotificationsAdapter(Context context, List<ModNotificationList> notificationLists) {
        this.context = context;
        this.notificationLists = notificationLists;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.frag_group_notifications_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, final int position) {
        holder.notification_title.setText(notificationLists.get(position).getTitle());
        holder.notification_message.setText(notificationLists.get(position).getMessage());

        final String sObjectId= notificationLists.get(position).getObjectId();

        String originDate = notificationLists.get(position).getCreateDate();
        SimpleDateFormat formatIn = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat formatOut = new SimpleDateFormat("dd MMM yyyy hh:mm");
        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(formatIn.parse(originDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String newDate = formatOut.format(calendar.getTime());
        holder.notification_date.setText(newDate);
        //DateFormat.getDateTimeInstance().format(notificationLists.get(position).getCreateDate());

        holder.rl_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sObjectId.equals("1")) {
                    Intent toJoinGroup = new Intent(context, GroupJoin.class);
                    toJoinGroup.putExtra("userId", notificationLists.get(position).getUserId());
                    toJoinGroup.putExtra("groupName", notificationLists.get(position).getGroupName());
                    toJoinGroup.putExtra("groupId", notificationLists.get(position).getGroupId());
                    toJoinGroup.putExtra("isJoin", notificationLists.get(position).getIsJoin());
                    context.startActivity(toJoinGroup);
                } else if(sObjectId.equals("2")) {
                    Intent toRequestGroupList = new Intent(context, GroupRequestList.class);
                    toRequestGroupList.putExtra("userId", notificationLists.get(position).getUserId());
                    toRequestGroupList.putExtra("groupName", notificationLists.get(position).getGroupName());
                    toRequestGroupList.putExtra("groupId", notificationLists.get(position).getGroupId());
                    context.startActivity(toRequestGroupList);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return notificationLists.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {

        LinearLayout rl_notification;
        TextView notification_title, notification_message, notification_date;

        public NotificationViewHolder(View itemView) {
            super(itemView);

            rl_notification = itemView.findViewById(R.id.rl_notification);
            notification_title = itemView.findViewById(R.id.notification_title);
            notification_message = itemView.findViewById(R.id.notification_message);
            notification_date = itemView.findViewById(R.id.notification_date);
        }
    }

}
