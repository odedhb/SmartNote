package snooze.ninja.controller;

import android.app.Dialog;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import snooze.ninja.App;
import snooze.ninja.MyActivity;
import snooze.ninja.R;
import snooze.ninja.SpeechDating.DateTimeListeningDialog;
import snooze.ninja.model.Item;

/**
 * Created by oded on 10/11/14.
 */
public class ItemAdapter extends BaseAdapter {
    private MyActivity myActivity;
    private LayoutInflater inflater;

    public ItemAdapter(MyActivity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public int getCount() {
        return Item.getAll().size();
    }

    @Override
    public Item getItem(int i) {
        return Item.getAll().get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final Item item = getItem(position);

        ViewHolderItem viewHolder;

        if (convertView == null) {

            // inflate the layout
            inflater = (myActivity).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.itemDescription = (TextView) convertView.findViewById(R.id.item_description);
            viewHolder.itemTime = (TextView) convertView.findViewById(R.id.item_time);
            viewHolder.checkBox = (ImageButton) convertView.findViewById(R.id.item_done);
            viewHolder.snoozeButton = convertView.findViewById(R.id.snooze_item);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        } else {
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // assign values if the object is not null
        if (item != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.itemDescription.setText(item.desc);
            viewHolder.itemDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Dialog dialog = new Dialog(myActivity);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                    dialog.setCanceledOnTouchOutside(true);
                    dialog.setContentView(inflater.inflate(R.layout.preview_layout, null));
                    ((TextView) dialog.findViewById(R.id.item_description_full)).setText(item.desc);
                    dialog.show();
                }
            });

            viewHolder.itemTime.setText(item.timeForDisplay());
            viewHolder.itemTime.setTextColor(convertView.getContext().getResources().getColor(item.dueDateColor()));
            Log.d("task_color", "" + item.dueDateColor());
            viewHolder.snoozeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new DateTimeListeningDialog(view.getContext(), new DateTimeListeningDialog.OnSubmitListener() {
                        @Override
                        public void onSubmit(long time) {
                            new Item(item.desc, time).save();
                            notifyDataSetChanged();
                            NotificationManager notificationManager =
                                    (NotificationManager) App.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                            notificationManager.cancel(item.desc.hashCode());
                        }
                    }).show();
                }
            });
//            viewHolder.itemDescription.setTag(item.);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item.delete(item.desc);
                    notifyDataSetChanged();
                }
            });

        }

        return convertView;

    }


    static class ViewHolderItem {
        TextView itemDescription;
        TextView itemTime;
        ImageButton checkBox;
        View snoozeButton;
    }
}
