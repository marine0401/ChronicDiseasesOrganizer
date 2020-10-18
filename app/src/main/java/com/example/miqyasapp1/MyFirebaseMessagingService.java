package com.example.miqyasapp1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

//Notification class with Firebase Cloud Messaging
public class MyFirebaseMessagingService extends FirebaseMessagingService {

        private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

        @Override
        public void onMessageReceived(RemoteMessage remoteMessage) {

            Log.i(TAG, "from: " + "onMessageReceived");
            //validation
            if (remoteMessage.getNotification() != null) {
                sendNotificationPayload(remoteMessage.getNotification());
            }//end of if
        }//end of onMessageReceived()

        private void sendNotificationPayload(RemoteMessage.Notification notification) {
            Intent intent = new Intent(this, SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this,
                    0, intent, PendingIntent.FLAG_ONE_SHOT);

            String CHANNEL_ID = this.getString(R.string.notifi_string);

            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .setContentTitle(notification.getTitle())
                    .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                    .setContentText(notification.getBody())
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri)
                    .setChannelId(CHANNEL_ID);

            notificationBuilder.setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

            int notifyID = 1;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, importance);
                notificationManager.createNotificationChannel(mChannel);
            }//end of if

            notificationManager.notify(notifyID, notificationBuilder.build());
        }//end of sendNotificationPayload()
    }//end of class

