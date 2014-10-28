package snooze.ninja;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.format.DateUtils;

import snooze.ninja.runners.AlarmIntentService;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by oded on 9/21/14.
 * App context and holder of misc
 */
public class App extends Application {


    public static App instance;

    public App() {
        instance = this;
        showingNotifications = new HashSet<String>();
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
                DateUtils.SECOND_IN_MILLIS * 3,
                DateUtils.SECOND_IN_MILLIS * 3, pendingIntent);
    }

    public Set<String> showingNotifications;

    public PendingIntent openAppPendingIntent() {
        return PendingIntent.getActivity(this, 704554, new Intent(this, MyActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
    }

}
