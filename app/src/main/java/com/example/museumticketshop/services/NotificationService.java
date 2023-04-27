package com.example.museumticketshop.services;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.museumticketshop.MainActivity;
import com.example.museumticketshop.R;

public class NotificationService {
    private static final String CHANNEL_ID = "ticket_purchase_channel";
    private final int NOTIFICATION_ID = 0;
    private final NotificationManager notificationManager;
    private final Context context;

    public NotificationService(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        initializeChannel();
    }

    private void initializeChannel() {
        // don't think it's required but whatever
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return;

        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Museum Tickets",
                NotificationManager.IMPORTANCE_HIGH);

        channel.enableVibration(true);
        channel.setDescription("Museum Ticket Shop notifications");

        notificationManager.createNotificationChannel(channel);
    }

    public void sendNotification(String message) {
        PendingIntent intent = PendingIntent.getActivity(context, NOTIFICATION_ID,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle("Museum Tickets")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(message)
                .setContentIntent(intent);

        // When targeting Android 13 or higher, posting a permission requires holding the
        // POST_NOTIFICATIONS permission
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
