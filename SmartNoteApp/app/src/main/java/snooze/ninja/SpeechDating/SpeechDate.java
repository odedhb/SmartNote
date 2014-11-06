package snooze.ninja.SpeechDating;

import android.util.Log;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import snooze.ninja.model.Item;

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
                TimeHypotheses timeHypotheses = new TimeHypotheses().setSpeech(when).setTimeInMillis(date.getTime());

                timeHypothesis.add(timeHypotheses);
                Log.d("time_debug : set: ", "voiceInput: " + voiceInput +
                        " | when: " + when + " | time: " + Item.fullTimeForDisplay(date.getTime()));
            }
        }
    }


    private static String normalize(String voiceInput) {
        return voiceInput
                .replace("doors", "2 hours")
                .replace("a_m", "a.m.")
                .replace("p_m", "p.m.")
                .replace("maroon", "tomorrow noon")
                .replace("nowhere and a half", "90 minutes")
                .replace("nowhere and a half", "90 minutes")
                .replace("an hour and a half", "90 minutes")
                .replace("free horse", "3 hours")
                .replace("o clock", "o'clock")
                .replace("o-clock", "o'clock")
                .replace("today's", "2 days")
                .replace("elephant", "11")
                .replace("the clock", "10 o'clock")
                ;
    }

    public TimeHypotheses getSelectedHypotheses() {
        Log.d("timeHypo", " count:"+timeHypothesis.size());

        if (timeHypothesis.size() < 1) {
            return null;
        }

        for (TimeHypotheses timeHypotheses : timeHypothesis) {
            Log.d("timeHypo", timeHypotheses.toString());
        }

        return timeHypothesis.get(0);
    }
}
