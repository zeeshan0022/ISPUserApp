package com.joinhub.complaintprotaluser.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.joinhub.complaintprotaluser.R;

public class FirebasePushMessaging extends FirebaseMessagingService {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {

        String title= message.getNotification().getTitle();
        String text= message.getNotification().getBody();
        String channel_id= "HEAD_UP_NOTIFICATION";
        NotificationChannel channel= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channel_id," Friends Internet", NotificationManager.IMPORTANCE_HIGH);
        }
        getSystemService(NotificationManager.class).createNotificationChannel(channel);
        Notification.Builder builder= new Notification.Builder(this, channel_id)
                .setContentText(text)
                .setContentTitle(title).setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher);
        NotificationManagerCompat.from(this).notify(1,builder.build());
        super.onMessageReceived(message);
    }
}
