package com.blogspot.odedhb.smartnote.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.odedhb.smartnote.MyActivity;
import com.blogspot.odedhb.smartnote.R;
import com.blogspot.odedhb.smartnote.SpeechDating.ListeningDialog;
import com.blogspot.odedhb.smartnote.model.Item;

/**
 * Created by oded on 10/11/14.
 */
public class ItemAdapter extends BaseAdapter {
    private MyActivity myActivity;

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

        ViewHolderItem viewHolder;

        if (convertView == null) {

            // inflate the layout
            LayoutInflater inflater = (myActivity).getLayoutInflater();
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
        if (getItem(position) != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.itemDescription.setText(getItem(position).desc);
            viewHolder.itemTime.setText(getItem(position).timeForDisplay());
            viewHolder.snoozeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(), "Speak!", Toast.LENGTH_SHORT).show();
                    new ListeningDialog(view.getContext()).show();
                }
            });
//            viewHolder.itemDescription.setTag(item.);
            viewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Item.delete(getItem(position).desc);
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
