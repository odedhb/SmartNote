package snooze.ninja.SpeechDating;

import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by oded on 8/12/14.
 */
public class SpeechRecognitionListener implements RecognitionListener {
    private final AudioManager audioManagerService;
    private ListeningDialog listeningDialog;

    public SpeechRecognitionListener(ListeningDialog listeningDialog) {
        this.listeningDialog = listeningDialog;
        audioManagerService = (AudioManager) listeningDialog.getContext().getSystemService(listeningDialog.getContext().AUDIO_SERVICE);
    }

    @Override
    public void onReadyForSpeech(Bundle bundle) {
        audioManagerService.setStreamMute(AudioManager.STREAM_MUSIC, false);
    }

    @Override
    public void onBeginningOfSpeech() {
        listeningDialog.onBeginningOfSpeech();

    }

    @Override
    public void onRmsChanged(float v) {
        listeningDialog.onVoiceLevelChanged(v);
    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {
        listeningDialog.onEndOfSpeech();
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = new ConstantIdentifier(SpeechRecognizer.class).name(errorCode);
        Log.e("SpeechApi", errorMessage);

        if (errorCode == SpeechRecognizer.ERROR_RECOGNIZER_BUSY) return;

        listeningDialog.startListening();
    }

    @Override
    public void onResults(Bundle bundle) {
        if (bundle != null) {
            float[] confidences = bundle.getFloatArray(SpeechRecognizer.CONFIDENCE_SCORES);
//            Toast.makeText(listeningDialog.getContext(), Arrays.toString(confidences), Toast.LENGTH_SHORT).show();

            ArrayList<String> list = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (list != null && list.size() > 0) {
                listeningDialog.onSpeechResults(list);
            }
        }
        listeningDialog.startListening();
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        if (bundle != null) {
            ArrayList<String> list = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (list != null && list.size() > 0) {
                listeningDialog.onPartialSpeechResults(list);
            }
        }
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }
}