package snooze.ninja;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by oded on 8/5/14.
 */
public class ContextualSpeech {

    static boolean enableContextualSpeech = true;

    public void run(SpeechRecognition speechRecognition) {

        if (!enableContextualSpeech) return;

        try {
            runSafely(speechRecognition);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void runSafely(SpeechRecognition speechRecognition) throws JSONException, UnsupportedEncodingException {

        if (speechRecognition.hypothesis.get(0).context_type.equals(ContextType.TASK)) {
            return;
        }
        JSONObject jsonObject = speechRecognition.toJson();

        new PostJSON("http://contextualspeech.appspot.com/api", jsonObject) {
            @Override
            public void onResponse(HttpResponse httpResponse, JSONObject jsonResponse) {
                Log.d("post response", httpResponse.getStatusLine().getReasonPhrase());
                Log.d("post json object response", jsonResponse.toString());
            }
        };

    }


    public class SpeechRecognition {

        private List<SpeechHypo> hypothesis;

        JSONObject toJson() throws JSONException {

            JSONObject wrapper = new JSONObject();
            JSONArray hypothesisJsonArray = new JSONArray();

            for (SpeechHypo hypotheses : hypothesis) {

                JSONObject hypo = new JSONObject();
                hypo.put("speech_recognition_id", hypotheses.speech_recognition_id);
                hypo.put("speech_hypotheses", hypotheses.speech_hypotheses);
                hypo.put("context_type", hypotheses.context_type);
                hypo.put("matches_context", hypotheses.matches_context);

                hypothesisJsonArray.put(hypo);
            }

            wrapper.put("hypothesis", hypothesisJsonArray);


            Log.d("json object", wrapper.toString());
            return wrapper;
        }

        public void add(SpeechHypo speechHypo) {
            if (hypothesis == null) {
                hypothesis = new ArrayList<SpeechHypo>();
            }

            hypothesis.add(speechHypo);
        }


    }


    public class SpeechHypo {
        public String speech_recognition_id;
        public String speech_hypotheses;
        public String context_type;
        public String matches_context;
    }

    public static class ContextType {
        public static String TIME = "TIME";
        public static String TASK = "TASK";
    }
}
