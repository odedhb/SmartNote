package com.blogspot.odedhb.smartnote.controller;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.MyActivity;
import com.blogspot.odedhb.smartnote.R;
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
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;

        if(convertView==null){

            // inflate the layout
            LayoutInflater inflater = (myActivity).getLayoutInflater();
            convertView = inflater.inflate(R.layout.item, parent, false);

            // well set up the ViewHolder
            viewHolder = new ViewHolderItem();
            viewHolder.itemDescription = (TextView) convertView.findViewById(R.id.item_description);
            viewHolder.itemTime = (TextView) convertView.findViewById(R.id.item_time);

            // store the holder with the view.
            convertView.setTag(viewHolder);

        }else{
            // we've just avoided calling findViewById() on resource every time
            // just use the viewHolder
            viewHolder = (ViewHolderItem) convertView.getTag();
        }

        // object item based on the position
        Item item = Item.getAll().get(position);

        // assign values if the object is not null
        if(item != null) {
            // get the TextView from the ViewHolder and then set the text (item name) and tag (item ID) values
            viewHolder.itemDescription.setText(item.desc);
            viewHolder.itemTime.setText(item.timeForDisplay());
//            viewHolder.itemDescription.setTag(item.);
        }

        return convertView;

    }


    static class ViewHolderItem {
        TextView itemDescription;
        TextView itemTime;
    }
}
