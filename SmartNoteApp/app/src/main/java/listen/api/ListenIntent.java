package listen.api;

import android.content.Intent;

import java.util.Arrays;

/**
 * Created by oded on 10/6/14.
 */
public class ListenIntent {

    String LISTEN_INTENT_KEYWORD = "LISTEN_INTENT_KEYWORD";

    private final Intent intent;


    ListenIntent(Intent intent) {
        this.intent = intent;
    }

    ListenIntent addKeywords(String... keyword) {
        intent.putStringArrayListExtra(LISTEN_INTENT_KEYWORD, (java.util.ArrayList<String>) Arrays.asList(keyword));
        return this;
    }

}
