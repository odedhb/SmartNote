package snooze.ninja.runners;

import android.app.IntentService;
import android.content.Intent;

import snooze.ninja.App;
import snooze.ninja.model.CreateNotification;
import snooze.ninja.model.Item;
import snooze.ninja.model.SnoozeNotification;

/**
 * Created by oded on 9/26/14.
 * Allowing deleting items by intent
 */
public class DeleteNotificationService extends IntentService {

    public DeleteNotificationService() {
        super("DeleteNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        processIntent(intent);
    }


    private void processIntent(Intent intent) {

        String originalItemDesc = intent.getStringExtra(SnoozeNotification.ORIGINAL_ITEM_DESC);

        Item.delete(originalItemDesc);

        App.instance.showCreateNotification();

    }

}
