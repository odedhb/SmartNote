package com.blogspot.odedhb.smartnote.runners;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.blogspot.odedhb.smartnote.model.CreateNotification;
import com.blogspot.odedhb.smartnote.model.Item;

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
        Log.d("Periodic", "" + System.currentTimeMillis());
    }
}
