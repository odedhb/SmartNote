package com.blogspot.odedhb.smartnote.SpeechDating;

import android.content.Context;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.model.Item;

import java.util.List;

/**
 * Created by oded on 10/11/14.
 */
public class DateTimeListeningDialog extends ListeningDialog {
    public DateTimeListeningDialog(Context context) {
        super(context);
    }

    @Override
    public void onSpeechResults(List<String> speechGuesses) {
        String speech = speechGuesses.get(0);
        setTitle(speech);
        Long time = new SpeechDate(speech).getTimeInMillis();

        if (time < System.currentTimeMillis()) return;

        TextView content = new TextView(context);
        content.setText(Item.timeForDisplay(time));
        setContentView(content);
    }
}
