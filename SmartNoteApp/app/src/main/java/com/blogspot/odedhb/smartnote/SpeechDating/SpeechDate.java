package com.blogspot.odedhb.smartnote.SpeechDating;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by oded on 10/2/14.
 */
public class SpeechDate {

    private Long timeInMillis;

    public SpeechDate(CharSequence hypothesis) {
        List<String> hypotheses = new ArrayList<String>();
        hypotheses.add(hypothesis.toString());
        setText(hypotheses);
    }

    public SpeechDate(List<String> hypotheses) {
        setText(hypotheses);
    }

    private void setText(List<String> hypotheses) {
        for (String hypothesis : hypotheses) {
            timeInMillis = parseTimeFromText(hypothesis);
            if (timeInMillis != 0) break;
        }
    }


    public static long parseTimeFromText(String voiceInput) {

        String when = normalize(voiceInput);

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(when);
        long target = 0;
        for (DateGroup group : groups) {
            List<Date> dates = group.getDates();
            target = dates.get(0).getTime();
        }

        return target;
    }

    private static String normalize(String voiceInput) {
        return voiceInput
                .replace("(?i)doors", "2 hours")
                .replace("(?i)a_m", "a.m.");
    }

    public Long getTimeInMillis() {
        return timeInMillis;
    }
}
