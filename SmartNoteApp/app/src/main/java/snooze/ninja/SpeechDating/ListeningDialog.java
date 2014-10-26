package snooze.ninja.SpeechDating;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.List;

import snooze.ninja.R;

/**
 * Created by Oded on 8/12/14.
 */
public class ListeningDialog extends Dialog {

    private final Context context;
    SpeechRecognizer speechRecognizer;
    Intent speechRecognitionIntent;
    private AudioManager audioManagerService;
    boolean isBeepMuted = true;
    private TextView speechTextView;
    private View soundBarView;
    private View boxView;
    private View progressBarView;
    private float progress = 0;
    public RelativeLayout speechAnimation;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setCanceledOnTouchOutside(true);
        setContentView(getMainView());
    }

    private RelativeLayout getMainView() {
        speechAnimation = new RelativeLayout(context);
        speechAnimation.setLayoutParams(new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT));


        boxView = new View(context);
        boxView.setBackground(boxBg());
        RelativeLayout.LayoutParams boxViewLayoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 128);
        speechAnimation.addView(boxView, boxViewLayoutParams);


        progressBarView = new View(context);
        progressBarView.setBackgroundColor(context.getResources().getColor(R.color.progress_color));
        RelativeLayout.LayoutParams progressBarLayoutParams = new RelativeLayout.LayoutParams(
                16, 128);
        speechAnimation.addView(progressBarView, progressBarLayoutParams);


        soundBarView = new View(context);
        soundBarView.setBackgroundColor(context.getResources().getColor(R.color.speech_volume_color));
        RelativeLayout.LayoutParams soundBarLayoutParams = new RelativeLayout.LayoutParams(
                16, 128);
        speechAnimation.addView(soundBarView, soundBarLayoutParams);


        speechTextView = new TextView(context);
        speechTextView.setText(R.string.speak);
        speechTextView.setTextSize(24);
        speechTextView.setTextColor(Color.parseColor("#ffffff"));
        RelativeLayout.LayoutParams speechTextViewParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        speechTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        speechTextViewParams.setMargins(30, 20, 30, 0);
        speechAnimation.addView(speechTextView, speechTextViewParams);
        return speechAnimation;
    }

    private Drawable boxBg() {
        GradientDrawable shape = new GradientDrawable();
        shape.setCornerRadius(8);

        shape.setColor(Color.parseColor("#99000000"));

        return shape;
    }

/*
    @Override
    public void setOnShowListener(OnShowListener listener) {
        super.setOnShowListener(listener);
    }*/

    public void stopListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, false);
        }
        speechRecognizer.stopListening();
    }


    public void onSpeechResults(List<String> speechGuesses) {
        speechTextView.setText(speechGuesses.get(0));
    }

    void startListening() {
        if (isBeepMuted) {
            audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, true);
        }
        speechRecognizer.startListening(speechRecognitionIntent);
    }

    public void onPartialSpeechResults(List<String> speechResults) {
        speechTextView.setText(speechResults.get(0));
    }

    public void onVoiceLevelChanged(float v) {

        soundBarView.setScaleX(v * 3);

        progress = progress * 1.1f;

        /*if (progress > 100) {
            progress = 1;
        }*/

        Log.d("ProgressBar", ":" + progress);

        progressBarView.setScaleX(progress);

//        soundBarView.setBackgroundColor(calculateColor(v));
        Log.d("Soundbar", "sound level:" + v);

    }


    public static int calculateColor(Float value) {
        String opacity = "#99"; //opacity between 00-ff
        String hexColor = String.format(
                opacity + "%06X", (0xeeeeee & value.hashCode()));

        return Color.parseColor(hexColor);
    }

    public void onEndOfSpeech() {
        progress = 1;
        soundBarView.setVisibility(View.GONE);
    }

    public void onBeginningOfSpeech() {
        progress = 0;
        soundBarView.setVisibility(View.VISIBLE);
    }
}
