package com.desktopip.exploriztic.tootanium.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.desktopip.exploriztic.tootanium.R;
import com.desktopip.exploriztic.tootanium.MainActivity;

/**
 * Created by Jayus on 13/08/2018.
 */

public class NotificationGenerator {

    private static final int NOTIFICATION_ID_OPEN_ACTIVITY = 9;

    public static void openActivityNotification(Context context) {
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(context, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSmallIcon(R.mipmap.ic_folder);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentTitle("Notification of advertise");
        notificationBuilder.setContentText("Open here");
        notificationManager.notify(NOTIFICATION_ID_OPEN_ACTIVITY, notificationBuilder.build());
    }

}
