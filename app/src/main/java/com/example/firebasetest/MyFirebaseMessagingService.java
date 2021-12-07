package com.example.firebasetest;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONObject;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public String TAG = "NEW_TOKEN";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //super.onMessageReceived(remoteMessage);
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        Log.d(TAG, "Time: " + remoteMessage.getSentTime());

        sendNotification(remoteMessage.getNotification().getBody());


        Log.d(TAG, "Notification: " + remoteMessage.getNotification());
        Log.d(TAG, "Data: " + remoteMessage.getData().toString());

    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        ///новое
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        ///

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(BitmapFactory.decodeResource(this.getResources(), R.drawable.ic_notification))
                    .setContentTitle(this.getString(R.string.app_name))
                    .setContentText(messageBody)
                    .setStyle(new NotificationCompat.BigPictureStyle())
                    .setAutoCancel(true)
                    //.setPriority(NotificationCompat.PRIORITY_DEFAULT)

                    .setSound(defaultSoundUri);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        ///новое
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        ////

        notificationManager.notify(0, notificationBuilder.build());

        Log.d(TAG, "Метод, создающий уведомления уже отработал");
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        //sendRegistrationToServer(token);


    }

}