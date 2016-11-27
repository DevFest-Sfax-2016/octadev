package tn.example.asus_octadev.tunitour.Services;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import tn.example.asus_octadev.tunitour.MainActivity;
import tn.example.asus_octadev.tunitour.R;


/**
 * Created by ASUS-OCTADEV on 2016-11-27.
 */

public class MyFirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    NotificationCompat.Builder builder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        showNotification(remoteMessage.getData().get("message").toString());
    }
    private void showNotification(String message) {
       Intent i = new Intent (this, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        builder = new NotificationCompat.Builder(this)
                .setAutoCancel(false)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(message)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent)
                .setSound(soundUri)
                .setVibrate(new long[]{1000, 1000, 1000, 1000});

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }
    }

