package com.blogspot.odedhb.smartnote.SpeechDating;

/**
 * Created by oded on 10/19/14.
 */
public class TimeHypotheses {

    private Long timeInMillis;
    private String speech;

    public Long getTimeInMillis() {
        return timeInMillis;
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
}
