package com.blogspot.odedhb.smartnote;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateUtils;

import com.blogspot.odedhb.smartnote.runners.AlarmIntentService;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.Date;
import java.util.List;

/**
 * Created by oded on 9/21/14.
 * App context and holder of misc
 */
public class App extends Application {


    private static App instance;

    public App() {
        instance = this;
    }

    public static App getContext() {
        return instance;
    }


    public static void periodicCheckForOverdueItems() {
        Intent intent = new Intent(App.getContext(), AlarmIntentService.class);

        PendingIntent pendingIntent =
                PendingIntent.getService(App.getContext(), 1987, intent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                AlarmManager.INTERVAL_FIFTEEN_MINUTES,
                DateUtils.SECOND_IN_MILLIS * 30,
                DateUtils.SECOND_IN_MILLIS * 30, pendingIntent);

    }


}
