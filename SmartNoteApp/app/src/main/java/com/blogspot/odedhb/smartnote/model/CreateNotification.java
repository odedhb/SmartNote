package com.blogspot.odedhb.smartnote.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.blogspot.odedhb.smartnote.App;
import com.blogspot.odedhb.smartnote.runners.ShowAllService;

/**
 * Created by oded on 9/19/14.
 * A notification for creating items
 */
public class CreateNotification extends Dictification {

    public static final String CREATE_TITLE = "SmartNote";

    public CreateNotification() {
        super(getTitle(), "Create", new String[]{"buy apples", "call Steve"});
    }

    static String getTitle() {
        return CREATE_TITLE + " : " + Item.getAllOverDue().size() + " / " + Item.getAll().size();
    }

    @Override
    protected NotificationCompat.Builder addStuffToNotification(NotificationCompat.Builder notificationBuilder) {
        return notificationBuilder;
    }

    @Override
    protected NotificationCompat.WearableExtender addStuffToExtender(NotificationCompat.WearableExtender wearableExtender) {

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(android.R.drawable.ic_menu_agenda, "Show all", showAllPendingIntent()).build();
        return wearableExtender.addAction(action);

    }


    PendingIntent showAllPendingIntent() {

        Intent intent = new Intent(App.getContext(), ShowAllService.class);
        intent.putExtra(SnoozeNotification.ORIGINAL_ITEM_DESC, notificationTitle);

        return PendingIntent.getService(App.getContext(), 1982, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    String getContentText() {
        return "";
    }

    @Override
    int getIcon() {
        return android.R.drawable.ic_menu_add;
    }

    @Override
    int getId() {
        return 123;
    }

    @Override
    protected boolean vibrate() {
        return false;
    }

    @Override
    String getGroupName() {
        return "CREATE_GROUP";
    }

}
