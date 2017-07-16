package com.alexsantos.careergoalsetting.utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.alexsantos.careergoalsetting.R;
import com.alexsantos.careergoalsetting.activity.MainActivity;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Alex on 16/07/2017.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    RemoteMessage remoteMessage;
    @Override
    public void onReceive(Context context, Intent intent) {



        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            showNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("date"));
        }
    }


    private void showNotification(String title, String date) {
        Intent intent = new Intent(String.valueOf(this));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);




    }



}
