package com.blogspot.odedhb.smartnote.SpeechDating;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.model.Item;

import java.util.List;

/**
 * Created by Oded on 8/12/14.
 */
public class ListeningDialog extends Dialog {

    private final Context context;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognitionIntent;
    private AudioManager audioManagerService;
    boolean isBeepMuted = true;

    public ListeningDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context, BestSpeechRecognition.getComponent(context));
        speechRecognitionIntent = BestSpeechRecognition.getIntent(context);
        speechRecognizer.setRecognitionListener(new SpeechRecognitionListener(this));
        audioManagerService = (AudioManager) context.getSystemService(context.AUDIO_SERVICE);
        startListening();

        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                stopListening();
                speechRecognizer.destroy();
            }
        });
    }
/*
    @Override
    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }*/

    private void stopListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        speechRecognizer.stopListening();
    }


    public void onSpeechResults(List<String> speechGuesses) {
        String speech = speechGuesses.get(0);
        setTitle(speech);

        Long time = new SpeechDate(speech).getTimeInMillis();

        TextView content = new TextView(context);
        content.setText(Item.timeForDisplay(time));
        setContentView(content);
    }

    void startListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        speechRecognizer.startListening(speechRecognitionIntent);
    }

    public void onPartialSpeechResults(List<String> speechResults) {
        setTitle(speechResults.get(0));
    }
}
