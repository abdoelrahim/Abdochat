package com.example.abdelrahim.abdochat;

import android.app.NotificationManager;
import android.app.Service;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Abd Elrahim on 1/13/2018.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String user_id = mAuth.getCurrentUser().getUid();

        String deviceToken = FirebaseInstanceId.getInstance().getToken();


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("New Friend request From ")

                        .setContentText(user_id);

        // Sets an ID for the notification
        int mNotificationId = (int) System.currentTimeMillis();
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }
}
