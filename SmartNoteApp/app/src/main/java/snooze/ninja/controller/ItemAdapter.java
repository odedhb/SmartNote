package snooze.ninja.controller;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.TextView;

import snooze.ninja.MyActivity;
import snooze.ninja.R;
import snooze.ninja.model.Item;

/**
 * Created by oded on 10/11/14.
 */
public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolderItem> {
    private MyActivity myActivity;

    public ItemAdapter(MyActivity myActivity) {
        this.myActivity = myActivity;
    }

    @Override
    public ViewHolderItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new ViewHolderItem(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolderItem viewHolderItem, int i) {

        final Item item = Item.getAll().get(i);

        viewHolderItem.itemDescription.setText(item.desc);
        viewHolderItem.itemDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(myActivity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.setContentView(LayoutInflater.from(viewHolderItem.itemDescription.getContext())
                        .inflate(R.layout.preview_layout, null));
                ((TextView) dialog.findViewById(R.id.item_description_full)).setText(item.desc);
                dialog.show();
            }
        });

        viewHolderItem.itemTime.setText(item.timeForDisplay());
        viewHolderItem.itemTime.setTextColor(viewHolderItem.itemTime.getContext().getResources().getColor(item.dueDateColor()));
        Log.d("task_color", "" + item.dueDateColor());
        SetTime setTimeClickListener = new SetTime(item, this);
        viewHolderItem.dateButtonSet.setOnClickListener(setTimeClickListener);
        viewHolderItem.dateButtonNew.setOnClickListener(setTimeClickListener);


        if (item.isNew()) {
            viewHolderItem.dateButtonSet.setVisibility(View.GONE);
            viewHolderItem.dateButtonNew.setVisibility(View.VISIBLE);
        } else {
            viewHolderItem.dateButtonSet.setVisibility(View.VISIBLE);
            viewHolderItem.dateButtonNew.setVisibility(View.GONE);
        }

        viewHolderItem.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemRemoved(item.getPositionInList());
                Item.delete(item.desc);
            }
        });

        viewHolderItem.itemView.setTag(item);
    }


    @Override
    public long getItemId(int i) {
        return Item.getAll().get(i).desc.hashCode();
    }

    @Override
    public int getItemCount() {
        return Item.getAll().size();
    }


    public static class ViewHolderItem extends RecyclerView.ViewHolder {
        public TextView itemDescription;
        public TextView itemTime;
        public ImageButton checkBox;
        public View dateButtonSet;
        public View dateButtonNew;

        public ViewHolderItem(View itemView) {
            super(itemView);
            itemDescription = (TextView) itemView.findViewById(R.id.item_description);
            itemTime = (TextView) itemView.findViewById(R.id.item_time);
            checkBox = (ImageButton) itemView.findViewById(R.id.item_done);
            dateButtonSet = itemView.findViewById(R.id.date_button_set);
            dateButtonNew = itemView.findViewById(R.id.date_button_new);
        }
    }
}
