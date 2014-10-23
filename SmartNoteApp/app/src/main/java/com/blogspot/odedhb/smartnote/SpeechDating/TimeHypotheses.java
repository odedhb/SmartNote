package com.blogspot.odedhb.smartnote.SpeechDating;

import android.util.Log;

import com.blogspot.odedhb.smartnote.model.Item;

import java.util.Calendar;

/**
 * Created by oded on 10/19/14.
 */
public class TimeHypotheses {

    private Long timeInMillis;
    private String speech;

    public Long getTimeInMillis() {

        Long returnedTime = new Long(timeInMillis);

        Log.d("returnedTime", Item.fullTimeForDisplay(timeInMillis));

        while (returnedTime < System.currentTimeMillis()) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(returnedTime);
            calendar.add(Calendar.HOUR, 12);
            returnedTime = calendar.getTimeInMillis();
        }


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
