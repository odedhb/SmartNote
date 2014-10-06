package com.blogspot.odedhb.smartnote.model;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.RemoteInput;

import com.blogspot.odedhb.smartnote.App;
import com.blogspot.odedhb.smartnote.runners.NotificationService;

import java.util.Random;

/**
 * Created by oded on 9/19/14.
 * Notification that can get dictation as input from a watch.
 */
public abstract class Dictification {
    public final static String EXTRA_VOICE_REPLY = "extra_voice_reply";
    public String notificationTitle;
    public Notification notification;

    public Dictification(String notificationTitle, String dictationTitle, String[] dictationList) {
        this.notificationTitle = notificationTitle;

        RemoteInput.Builder remoteInputBuilder = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(dictationTitle)
                .setChoices(dictationList);

        RemoteInput remoteInput = remoteInputBuilder.build();

        // Create an intent for the reply action
        Intent replyIntent = new Intent(App.getContext(), NotificationService.class);
        replyIntent.putExtra(SnoozeNotification.ORIGINAL_ITEM_DESC, notificationTitle);

//        replyIntent = addStringExtra(replyIntent);

        PendingIntent replyPendingIntent =
                PendingIntent.getService(App.getContext(), notificationTitle.hashCode(), replyIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        // Create the reply action and add the remote input
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(getIcon(),
                        dictationTitle, replyPendingIntent)
                        .addRemoteInput(remoteInput)
                        .build();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(App.getContext());
        notificationBuilder.setContentTitle(notificationTitle);
        notificationBuilder.setSmallIcon(getIcon());
        notificationBuilder.setContentText(getContentText());
        notificationBuilder.setGroup(getGroupName());
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
        wearableExtender.addAction(action);
        wearableExtender = addStuffToExtender(wearableExtender);
        notificationBuilder = notificationBuilder.extend(wearableExtender);
        notificationBuilder = addStuffToNotification(notificationBuilder);

        if (vibrate()) {
            notificationBuilder.setVibrate(new long[]{300, 300});
        }

        notificationBuilder.build();

        notification = notificationBuilder.build();
        notification.priority = getPriority();
//        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_ONLY_ALERT_ONCE;
    }

    abstract String getGroupName();

    protected abstract boolean vibrate();

    protected abstract NotificationCompat.Builder addStuffToNotification(NotificationCompat.Builder notificationBuilder);

    protected abstract NotificationCompat.WearableExtender addStuffToExtender(NotificationCompat.WearableExtender wearableExtender);

    public void show() {
        NotificationManager notificationManager =
                (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(getId(), notification);
    }

    abstract String getContentText();

    abstract int getIcon();

    int getId() {
        return new Random().nextInt();
    }

    int getPriority() {
        return Notification.PRIORITY_DEFAULT;
    }
}
