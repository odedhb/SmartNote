package snooze.ninja.SpeechDating;

import android.util.Log;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;

import snooze.ninja.model.Item;

/**
 * Created by oded on 10/19/14.
 */
public class TimeHypotheses {

    private Long timeInMillis;
    private String speech;

    public Long getTimeInMillis() {

        if (timeInMillis == null) return null;

        Log.d("returnedTime", Item.fullTimeForDisplay(timeInMillis));


        Long returnedTime = new Long(timeInMillis);
        if (timeInMillis < System.currentTimeMillis()) {
            returnedTime = backToTheFuture();
        }


        return returnedTime;
    }

    private Long backToTheFuture() {
        Long returnedTime;
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(timeInMillis);


        LinkedHashMap<Integer, Integer> timeJumps = new LinkedHashMap<Integer, Integer>();
        timeJumps.put(Calendar.HOUR, 12);
        timeJumps.put(Calendar.HOUR, 24);
        timeJumps.put(Calendar.WEEK_OF_YEAR, 1);
        timeJumps.put(Calendar.MONTH, 1);
        timeJumps.put(Calendar.YEAR, 1);


        for (Map.Entry<Integer, Integer> jump : timeJumps.entrySet()) {

            if (calendar.getTimeInMillis() > System.currentTimeMillis()) {
                break;
            }

            calendar.setTimeInMillis(timeInMillis);
            calendar.add(jump.getKey(), jump.getValue());
        }


        returnedTime = calendar.getTimeInMillis();
        return returnedTime;
    }

    public TimeHypotheses setTimeInMillis(Long timeInMillis) {
        this.timeInMillis = timeInMillis;
        return this;
    }

    public String getSpeech() {
        return speech;
    }

    public TimeHypotheses setSpeech(String speech) {
        this.speech = speech;
        return this;

    }

    @Override
    public String toString() {
        return getSpeech() + " : " + Item.fullTimeForDisplay(timeInMillis);
    }
}
