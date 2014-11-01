package snooze.ninja.model;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import snooze.ninja.App;
import snooze.ninja.R;
import snooze.ninja.runners.ShowAllService;

/**
 * Created by oded on 9/19/14.
 * A notification for creating items
 */
public class CreateNotification extends Dictification {

    public static final String CREATE_TITLE = "SmartNote";

    public CreateNotification() {
        super(getTitle(), "Create", new String[]{"buy apples", "call Steve"});
//        notification.flags |= Notification.FLAG_ONGOING_EVENT;
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
                new NotificationCompat.Action.Builder(R.drawable.show_all_icon, "Show all", showAllPendingIntent()).build();
        return wearableExtender.addAction(action);

    }


    static Intent showAllIntent() {
        return new Intent(App.getContext(), ShowAllService.class);
    }

    PendingIntent showAllPendingIntent() {
        return PendingIntent.getService(App.getContext(), SHOW_ALL_PENDING_INTENT_ID, showAllIntent(),
                PendingIntent.FLAG_ONE_SHOT);
    }

    @Override
    String getContentText() {
        return "";
    }

    @Override
    int getIcon() {
        return R.drawable.ic_logo_monochrome;
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
        return null;
    }


/*    public static boolean isCreateNotificationVisible() {
        PendingIntent test = PendingIntent.getActivity(App.getContext(), SHOW_ALL_PENDING_INTENT_ID,
                showAllIntent(), PendingIntent.FLAG_NO_CREATE);
        return test != null;
    }*/

    static int SHOW_ALL_PENDING_INTENT_ID = 1982;
}
