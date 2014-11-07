package snooze.ninja.SpeechDating;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import snooze.ninja.App;

/**
 * Created by oded on 11/6/14.
 */
public class SnoozeHistory {
    public static String getPastSnoozesText(int i) {
        Map<String, Long> map = (Map<String, Long>) prefs().getAll();

        List<PastSnooze> pastSnoozeList = new ArrayList<PastSnooze>();

        for (Map.Entry item : map.entrySet()) {

            String k = (String) item.getKey();
            long v = (Long) item.getValue();

            pastSnoozeList.add(new PastSnooze(k, v));
        }

        Collections.sort(pastSnoozeList, new PastSnoozeComparator());

        if (i > pastSnoozeList.size() - 1) {
            return null;
        }
        return pastSnoozeList.get(i).timeString;
    }

    static void add(String timeString) {
        prefs().edit().putLong(
                timeString, System.currentTimeMillis()).commit();
    }

    static private SharedPreferences prefs() {
        return App.getContext().getSharedPreferences(
                "past_snoozes", Context.MODE_PRIVATE);
    }

    private static class PastSnoozeComparator implements java.util.Comparator<PastSnooze> {
        @Override
        public int compare(PastSnooze lhs, PastSnooze rhs) {
            return rhs.lastUsed.compareTo(lhs.lastUsed);
        }
    }

    static class PastSnooze {

        PastSnooze(String timeString, Long lastUsed) {
            this.lastUsed = lastUsed;
            this.timeString = timeString;
        }

        Long lastUsed;
        String timeString;
    }
}
