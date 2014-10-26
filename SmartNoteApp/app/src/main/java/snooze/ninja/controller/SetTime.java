package snooze.ninja.controller;

import android.app.NotificationManager;
import android.content.Context;
import android.view.View;

import snooze.ninja.App;
import snooze.ninja.SpeechDating.DateTimeListeningDialog;
import snooze.ninja.model.Item;

/**
 * Created by oded on 10/26/14.
 */
public class SetTime implements View.OnClickListener {

    private Item item;
    private ItemAdapter itemAdapter;

    SetTime(Item item, ItemAdapter itemAdapter) {

        this.item = item;
        this.itemAdapter = itemAdapter;
    }


    @Override
    public void onClick(View view) {
        new DateTimeListeningDialog(view.getContext(), new DateTimeListeningDialog.OnSubmitListener() {
            @Override
            public void onSubmit(long time) {
                new Item(item.desc, time).save();
                itemAdapter.notifyDataSetChanged();
                NotificationManager notificationManager =
                        (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.cancel(item.desc.hashCode());
            }
        }).show();

    }

}
