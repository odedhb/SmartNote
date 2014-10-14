package com.blogspot.odedhb.smartnote.model;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.format.DateUtils;

import com.blogspot.odedhb.smartnote.App;
import com.blogspot.odedhb.smartnote.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by oded on 9/21/14.
 * A to-do item
 */
public class Item {

    public String desc;
    public long time;

    static SharedPreferences prefs() {
        return App.getContext().getSharedPreferences(
                "items", Context.MODE_PRIVATE);
    }


    public Item(String desc, long time) {
        this.desc = desc;
        this.time = time;
    }

    public Item(CharSequence desc, long time) {
        this.desc = desc.toString();
        this.time = time;
    }

    public void save() {
        prefs().edit().putLong(desc, time).commit();
    }

    public static List<Item> getAll() {
        List<Item> items = new ArrayList<Item>();

        //suppressed check, we want the code to except if this is not a <String,Long> map.
        @SuppressWarnings("unchecked")
        Map<String, Long> itemMap = (Map<String, Long>) prefs().getAll();

        for (Map.Entry item : itemMap.entrySet()) {

            String k = (String) item.getKey();
            long v = (Long) item.getValue();

            items.add(new Item(k, v));
        }

        Collections.sort(items, new ItemComparator());

        return items;
    }

    public static List<Item> getAllOverDue() {
        List<Item> items = new ArrayList<Item>();
        for (Item item : getAll()) {
            if (item.isOverDue()) {
                items.add(item);
            }
        }
        return items;
    }

    private boolean isOverDue() {
        return time < System.currentTimeMillis();
    }

    public static void popAllOverDue() {
        //get all items
        for (Item item : Item.getAllOverDue()) {
            new SnoozeNotification(item.desc).show();
        }
    }

    public static void delete(String originalItemDesc) {
        prefs().edit().remove(originalItemDesc).commit();

        NotificationManager notificationManager =
                (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(originalItemDesc.hashCode());
    }

    public String timeForDisplay() {
        return timeForDisplay(time);
    }

    public static String timeForDisplay(long timeInMillis) {
        String relative = DateUtils.getRelativeTimeSpanString(timeInMillis, System.currentTimeMillis(), 0L, DateUtils.FORMAT_ABBREV_ALL).toString();
        String exact = DateUtils.formatDateTime(App.getContext(), timeInMillis,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL);
        return exact + "\n" + relative;
    }

    public static void popAll() {
        //get all items
        for (Item item : Item.getAll()) {
            new SnoozeNotification(item.desc).show();
        }
    }

    private static class ItemComparator implements java.util.Comparator<Item> {
        @Override
        public int compare(Item i, Item i2) {
            return ((Long) i.time).compareTo(i2.time);
        }
    }

    public int dueDateColor() {
        long delta = time - System.currentTimeMillis();

        if (delta > DateUtils.DAY_IN_MILLIS) return R.color.due_tomorrow_text;

        if (delta > 0l) return R.color.due_today_text;

        return R.color.overdue_text;
    }
}
