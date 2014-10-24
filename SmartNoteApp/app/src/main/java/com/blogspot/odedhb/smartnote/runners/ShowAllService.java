package com.blogspot.odedhb.smartnote.runners;

import android.app.IntentService;
import android.content.Intent;

import com.blogspot.odedhb.smartnote.model.Item;

/**
 * Created by oded on 10/1/14.
 * Show notifications for all items
 */
public class ShowAllService extends IntentService {

    public ShowAllService() {
        super("ShowAllService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        Item.popAllOverDue();
        Item.popAll();
    }
}
