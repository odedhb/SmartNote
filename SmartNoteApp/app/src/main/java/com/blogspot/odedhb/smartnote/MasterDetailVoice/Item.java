package com.blogspot.odedhb.smartnote.MasterDetailVoice;

import android.os.Bundle;

import java.util.List;

/**
 * Created by oded on 10/2/14.
 */
public class Item {

    public Bundle bundle;

    List<VoiceCommand> voiceCommands;


    Item setTitle(String title) {
        bundle.putString("title", title);
        return this;
    }

    Item setTime(Long time) {
        bundle.putLong("time", time);
        return this;
    }

    Item setText(String text) {
        bundle.putString("text", text);
        return this;
    }

    Item addVoiceCommand(VoiceCommand voiceCommand) {
        voiceCommands.add(voiceCommand);
        return this;
    }

}
