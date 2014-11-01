package snooze.ninja.runners;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import snooze.ninja.model.Item;
import snooze.ninja.model.SnoozeNotification;

/**
 * Created by oded on 10/7/14.
 */
public class DismissSnoozeNotificationService extends IntentService {


    public DismissSnoozeNotificationService() {
        super("DismissSnoozeNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String originalItemDesc = intent.getStringExtra(SnoozeNotification.ORIGINAL_ITEM_DESC);
        Log.d("testing-", "dismissed " + originalItemDesc);
        Item.getByName(originalItemDesc).setDismissed();
    }
}
