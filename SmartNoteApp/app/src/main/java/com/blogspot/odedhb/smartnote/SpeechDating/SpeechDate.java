package com.blogspot.odedhb.smartnote.SpeechDating;

import android.util.Log;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oded on 10/2/14.
 */
public class SpeechDate {

    private List<TimeHypotheses> timeHypothesis;

    public SpeechDate(CharSequence hypothesis) {
        List<String> hypotheses = new ArrayList<String>();
        hypotheses.add(hypothesis.toString());
        setText(hypotheses);
    }

    public SpeechDate(List<String> hypothesis) {
        setText(hypothesis);
    }

    private void setText(List<String> hypothesis) {
        timeHypothesis = new ArrayList<TimeHypotheses>();

        Log.d("time_debug : hypothesis", hypothesis.toString());

        for (String hypo : hypothesis) {
            long timeInMillis = parseTimeFromText(hypo);

            if (timeInMillis == 0) continue;

            timeHypothesis.add(new TimeHypotheses().setSpeech(hypo).setTimeInMillis(timeInMillis));
        }
    }


    public static long parseTimeFromText(String voiceInput) {

        String when = normalize(voiceInput);

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(when);
        long target = 0;
        for (DateGroup group : groups) {

            Log.d("time_debug : group", group.toString());

            List<Date> dates = group.getDates();

            Log.d("time_debug : dates", dates.toString());

            target = dates.get(0).getTime();
        }

        return target;
    }

    private static String normalize(String voiceInput) {
        return voiceInput
                .replace("(?i)doors", "2 hours")
                .replace("(?i)a_m", "a.m.")
                .replace("(?i)noon", "tomorrow noon")
                .replace("(?i)maroon", "tomorrow noon")
                .replace("(?i)nowhere and a half", "90 minutes")
                .replace("(?i)nowhere and a half", "90 minutes")
                .replace("(?i)an hour and a half", "90 minutes")
                .replace("(?i)free horse", "3 hours")
                .replace("(?i)o clock", "o'clock")
                .replace("(?i)o-clock", "o'clock")
                ;
    }

    public TimeHypotheses getSelectedHypotheses() {
        return timeHypothesis.get(0);
    }
}
