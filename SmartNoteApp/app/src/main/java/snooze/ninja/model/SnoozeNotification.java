package snooze.ninja.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import snooze.ninja.App;
import snooze.ninja.R;
import snooze.ninja.runners.DeleteNotificationService;
import snooze.ninja.runners.DismissSnoozeNotificationService;

import java.util.List;

/**
 * Created by oded on 9/19/14.
 * a Notification that shows the item and can be snoozed
 */
public class SnoozeNotification extends Dictification {

    public static final String ORIGINAL_ITEM_DESC = "ORIGINAL_ITEM_DESC";

    public SnoozeNotification(String notificationTitle) {
        super(notificationTitle, "Snooze", new String[]{
                "5 minutes",
                "30 minutes",
                "1 hour",
                "noon time",
                "tonight",
                "tomorrow morning",
                "tomorrow noon",
                "tomorrow evening"});
    }


    @Override
    String getContentText() {

        List<Item> items = Item.getAll();

        for (Item item : items) {
            if (item.desc.equals(notificationTitle))
                return item.timeForDisplay();
        }

        return null;
    }

    @Override
    int getIcon() {
        return R.drawable.snooze_icon;
    }

    @Override
    int getId() {
        return notificationTitle.hashCode();
    }

    @Override
    protected NotificationCompat.Builder addStuffToNotification(NotificationCompat.Builder notificationBuilder) {
        notificationBuilder.addAction(R.drawable.delete_icon, "Remove", deletePendingIntent());
        notificationBuilder.setDeleteIntent(getDismissPendingIntent());
        return notificationBuilder;
    }

    private PendingIntent getDismissPendingIntent() {
        Intent intent = new Intent(App.getContext(), DismissSnoozeNotificationService.class);
        intent.putExtra(SnoozeNotification.ORIGINAL_ITEM_DESC, notificationTitle);

        return PendingIntent.getService(App.getContext(), notificationTitle.hashCode() - 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent deletePendingIntent() {

        Intent intent = new Intent(App.getContext(), DeleteNotificationService.class);
        intent.putExtra(SnoozeNotification.ORIGINAL_ITEM_DESC, notificationTitle);

        return PendingIntent.getService(App.getContext(), notificationTitle.hashCode() + 1, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    protected NotificationCompat.WearableExtender addStuffToExtender(NotificationCompat.WearableExtender wearableExtender) {

        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.drawable.delete_icon, "Delete", deletePendingIntent()).build();
        return wearableExtender.addAction(action);
    }


    @Override
    protected boolean vibrate() {
        return true;
    }

    @Override
    String getGroupName() {
        return "SNOOZE_GROUP";
    }

}