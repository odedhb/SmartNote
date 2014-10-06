package com.blogspot.odedhb.smartnote.runners;

import android.app.IntentService;
import android.content.Intent;

import com.blogspot.odedhb.smartnote.model.CreateNotification;
import com.blogspot.odedhb.smartnote.model.Item;
import com.blogspot.odedhb.smartnote.model.SnoozeNotification;

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

        new CreateNotification().show();

    }

}
