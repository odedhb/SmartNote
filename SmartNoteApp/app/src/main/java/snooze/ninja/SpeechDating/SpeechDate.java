package snooze.ninja.SpeechDating;

import android.util.Log;

import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import snooze.ninja.ContextualSpeech;
import snooze.ninja.model.Item;


/**
 * Created by oded on 10/2/14.
 */
public class SpeechDate {

    private List<TimeHypotheses> timeHypothesis;
    private ContextualSpeech.SpeechRecognition speechRecognition;
    private ContextualSpeech contextualSpeech;

    void initialize(List<String> hypothesis, String contextType) {
        contextualSpeech = new ContextualSpeech();
        setText(hypothesis, contextType);
    }

    public SpeechDate(CharSequence hypotheses, String contextType) {
        List<String> hypothesis = new ArrayList<String>();
        hypothesis.add(hypotheses.toString());
        initialize(hypothesis, contextType);
    }

    public SpeechDate(List<String> hypothesis, String contextType) {
        initialize(hypothesis, contextType);
    }

    private void setText(List<String> hypothesis, String contextType) {
        timeHypothesis = new ArrayList<TimeHypotheses>();

        Log.d("time_debug : hypothesis", hypothesis.toString());

        speechRecognition = contextualSpeech.new SpeechRecognition();

        for (String hypoString : hypothesis) {

            Boolean isTime = parseTimeFromText(hypoString);

            ContextualSpeech.SpeechHypo hypo = contextualSpeech.new SpeechHypo();

            hypo.context_type = contextType;
            hypo.matches_context = isTime.toString();
            hypo.speech_hypotheses = hypoString;
            hypo.speech_recognition_id = hypothesis.get(0).hashCode() + "_" + hypothesis.get(0);

            speechRecognition.add(hypo);
        }
    }


    public Boolean parseTimeFromText(String voiceInput) {

        String when = voiceInput;

        List<TimeHypotheses> tempTimeHypothesis = new ArrayList<TimeHypotheses>();

        Parser parser = new Parser();
        List<DateGroup> groups = parser.parse(when);
        for (DateGroup group : groups) {

            Log.d("time_debug : group", group.getText());
//            Log.d("time_debug : group tree", group.getSyntaxTree().getText());

            List<Date> dates = group.getDates();

            for (Date date : dates) {
                if (date.getTime() == 0) continue;
                TimeHypotheses timeHypotheses = new TimeHypotheses().setSpeech(when).setTimeInMillis(date.getTime());

                tempTimeHypothesis.add(timeHypotheses);
                Log.d("time_debug : set: ", "voiceInput: " + voiceInput +
                        " | when: " + when + " | lastUsed: " + Item.fullTimeForDisplay(date.getTime()));
            }
        }

        timeHypothesis.addAll(tempTimeHypothesis);

        return tempTimeHypothesis.size() > 0;
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
                .replace("the cock", "4 o'clock")
                ;
    }

    public TimeHypotheses getSelectedHypotheses() {
        Log.d("timeHypo", " count:" + timeHypothesis.size());

        contextualSpeech.run(speechRecognition);

        if (timeHypothesis.size() < 1) {
            return null;
        }

        for (TimeHypotheses timeHypotheses : timeHypothesis) {
            Log.d("timeHypo", timeHypotheses.toString());
        }

        return timeHypothesis.get(0);
    }


}
