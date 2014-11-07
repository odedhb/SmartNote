package snooze.ninja.runners;

import android.app.IntentService;
import android.content.Intent;

import snooze.ninja.App;
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
//        App.instance.showCreateNotification();

        Item.popAllOverDue();
    }
}
