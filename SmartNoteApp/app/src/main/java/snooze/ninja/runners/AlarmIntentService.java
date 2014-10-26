package snooze.ninja.runners;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import snooze.ninja.App;
import snooze.ninja.model.CreateNotification;
import snooze.ninja.model.Item;

/**
 * Created by oded on 10/1/14.
 * The service that will run periodically and show notifications for all overdue items and the create notification.
 */
public class AlarmIntentService extends IntentService {

    public AlarmIntentService() {
        super("AlarmIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        new CreateNotification().show();
        Item.popAllOverDue();
        Log.d("Periodic", "" + System.currentTimeMillis() + " showing:"+ App.instance.showingNotifications.size());
    }
}