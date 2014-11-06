package snooze.ninja.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;

import snooze.ninja.App;
import snooze.ninja.R;
import snooze.ninja.runners.DeleteNotificationService;
import snooze.ninja.runners.DismissSnoozeNotificationService;

/**
 * Created by oded on 9/19/14.
 * a Notification that shows the item and can be snoozed
 */
public class SnoozeNotification extends Dictification {

    public static final String ORIGINAL_ITEM_DESC = "ORIGINAL_ITEM_DESC";

    public SnoozeNotification(String notificationTitle) {
        super(notificationTitle, "Snooze", getHardCodedSnoozeStrings());
    }


    private static String[] getHardCodedSnoozeStrings() {

        ArrayList<String> days = new ArrayList<String>();
        days.add("this");
        days.add("tomorrow");
        days.add("sunday");
        days.add("monday");
        days.add("tuesday");
        days.add("wednesday");
        days.add("thursday");
        days.add("friday");
        days.add("saturday");

        ArrayList<String> partsOfDay = new ArrayList<String>();
        partsOfDay.add("morning");
        partsOfDay.add("noon");
        partsOfDay.add("evening");
//        partsOfDay.add("night");


        ArrayList<String> hardcodedList = new ArrayList<String>();
//        hardcodedList.add("now");
        hardcodedList.add("in 5 minutes");
        hardcodedList.add("in 15 minutes");
        hardcodedList.add("in 30 minutes");
        hardcodedList.add("in 1 hour");
        hardcodedList.add("in 2 hours");
        hardcodedList.add("in 3 hours");
        hardcodedList.add("in 4 hours");
        hardcodedList.add("in 5 hours");
        hardcodedList.add("in 6 hours");
        hardcodedList.add("in 8 hours");
        hardcodedList.add("in 10 hours");
        hardcodedList.add("in 12 hours");
        hardcodedList.add("tonight");
        hardcodedList.add("in 18 hours");

        //add all day of week alternatives
        for (String dayName : days) {
            for (String pod : partsOfDay) {
                hardcodedList.add(dayName + " " + pod);
            }
        }

        hardcodedList.add("in 2 days");
        hardcodedList.add("in 3 days");
        hardcodedList.add("weekend");
        hardcodedList.add("in a week");

        return hardcodedList.toArray(new String[hardcodedList.size()]);
    }


    @Override
    String getContentText() {

        Item item = Item.getByName(notificationTitle);

        if (item.isNew()) {
            return App.getContext().getResources().getString(R.string.new_item_notification_message);
        }
        return item.timeForDisplay();
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
        notificationBuilder.addAction(R.drawable.check_selected_icon, App.instance.getResources().getString(R.string.delete_task), deletePendingIntent());
        notificationBuilder.setDeleteIntent(getDismissPendingIntent());
        notificationBuilder.setSound(Uri.parse("android.resource://"
                + App.instance.getPackageName() + "/" + R.raw.woman_clears_throat));
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
                new NotificationCompat.Action.Builder(R.drawable.delete_icon_hi_res, "Delete", deletePendingIntent()).build();
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


    @Override
    public void show() {

        Item item = Item.getByName(notificationTitle);

        Log.d("notify check:", "id: " + getId()
                + " | wasNotified: " + item
                .wasNotified());


        if (item.wasNotified()) {
            return;
        }

        super.show();

        item.setNotified();
    }
}