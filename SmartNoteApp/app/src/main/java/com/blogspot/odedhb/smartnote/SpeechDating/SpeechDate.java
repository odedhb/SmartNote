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
            parseTimeFromText(hypo);
        }
    }


    public void parseTimeFromText(String voiceInput) {

        String when = normalize(voiceInput);


        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(when);
        for (DateGroup group : groups) {

            Log.d("time_debug : group", group.getText());
//            Log.d("time_debug : group tree", group.getSyntaxTree().getText());

            List<Date> dates = group.getDates();

            for (Date date : dates) {
                if (date.getTime() == 0) continue;
                TimeHypotheses timeHypotheses = new TimeHypotheses().setSpeech(voiceInput).setTimeInMillis(date.getTime());
                timeHypothesis.add(timeHypotheses);
                Log.d("time_debug : hypo: ", timeHypotheses.toString());
            }
        }
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
                .replace("(?i)today's", "2 days")
                .replace("(?i)elephant", "eleven")
                ;
    }

    public TimeHypotheses getSelectedHypotheses() {

        if (timeHypothesis.size() < 1) {
            return null;
        }

        return timeHypothesis.get(0);
    }
}
