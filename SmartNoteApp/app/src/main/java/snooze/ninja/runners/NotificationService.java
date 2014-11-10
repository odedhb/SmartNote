package snooze.ninja.runners;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.RemoteInput;
import android.util.Log;

import snooze.ninja.App;
import snooze.ninja.ContextualSpeech;
import snooze.ninja.SpeechDating.SpeechDate;
import snooze.ninja.SpeechDating.TimeHypotheses;
import snooze.ninja.model.CreateNotification;
import snooze.ninja.model.Dictification;
import snooze.ninja.model.Item;
import snooze.ninja.model.SnoozeNotification;

/**
 * Created by oded on 9/26/14.
 * Receiver for voice remote input, both for creation and snoozing
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        processIntent(intent);
    }


    private void processIntent(Intent intent) {

        Log.d("testing-" + this, "intent captured");

        Bundle remoteInputResults = RemoteInput.getResultsFromIntent(intent);

        Log.d("testing-" + this, "remote input captured");

        if (remoteInputResults == null) return;

        Log.d("testing-" + this, "remote input results are not null");

        CharSequence speech = remoteInputResults.getCharSequence(Dictification.EXTRA_VOICE_REPLY);

        String originalItemDesc = intent.getStringExtra(SnoozeNotification.ORIGINAL_ITEM_DESC);

        if (originalItemDesc.contains(CreateNotification.CREATE_TITLE)) {
            Log.d("testing-" + this, "creating");
            create(speech);
            Log.d("testing-" + this, "created");
        } else {
            Log.d("testing-" + this, "snoozing");
            snooze(speech, originalItemDesc);
            Log.d("testing-" + this, "snoozed");
        }

        App.instance.showCreateNotification();
        Log.d("testing-" + this, "refreshing CreateNotification");
    }

    void create(CharSequence speech) {

        Log.d("testing-" + this, "create function called");

        TimeHypotheses timeHypotheses = new SpeechDate(speech, ContextualSpeech.ContextType.TASK).getSelectedHypotheses();

        if (timeHypotheses == null) {
            Log.d("testing-" + this, "time is null");
            new Item(speech).save();
            Log.d("testing-" + this, "new timeless Item finished");
            return;
        }

        Long time = timeHypotheses.getTimeInMillis();

        Log.d("testing-" + this, "speechDate result is :" + time.toString());

        if (time != null) {
            Log.d("testing-" + this, "time is not null");
            new Item(speech, time).save();
            Log.d("testing-" + this, "new Item finished");
        } else {
            new Item(speech).save();
        }
    }

    void snooze(CharSequence speech, String originalItemDesc) {
        Long time = new SpeechDate(speech, ContextualSpeech.ContextType.TIME).getSelectedHypotheses().getTimeInMillis();
        if (time == null) return;
        if (time == 0l) return;
        new Item(originalItemDesc, time).save();

        NotificationManager notificationManager =
                (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(originalItemDesc.hashCode());
    }

}
