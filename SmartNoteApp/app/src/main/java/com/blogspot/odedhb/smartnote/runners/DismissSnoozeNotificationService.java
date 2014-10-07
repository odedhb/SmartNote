package com.blogspot.odedhb.smartnote.runners;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.blogspot.odedhb.smartnote.App;
import com.blogspot.odedhb.smartnote.model.SnoozeNotification;

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
        App.instance.showingNotifications.remove(originalItemDesc);
    }
}
