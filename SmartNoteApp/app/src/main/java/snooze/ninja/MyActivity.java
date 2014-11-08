package snooze.ninja;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import snooze.ninja.SpeechDating.SpeechDate;
import snooze.ninja.SpeechDating.TimeHypotheses;
import snooze.ninja.controller.ItemAdapter;
import snooze.ninja.model.Item;


public class MyActivity extends ActionBarActivity {


    private RecyclerView mRecyclerView;
    private EditText createItem;
    private LinearLayoutManager mLayoutManager;
    private ItemAdapter mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        App.periodicCheckForOverdueItems();

        mRecyclerView = (RecyclerView) findViewById(R.id.item_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mItemAdapter = new ItemAdapter(this);
        mRecyclerView.setAdapter(mItemAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());


        createItem = (EditText) findViewById(R.id.create_edit_text);
        createItem.setOnEditorActionListener(new EditorActionDoneListener());

    }

    @Override
    protected void onResume() {
        super.onResume();
        App.instance.showCreateNotification();
        mItemAdapter.notifyDataSetChanged();
    }

    public void addButton(View view) {

        EditText createBox = (EditText) findViewById(R.id.create_edit_text);

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(createBox.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);

        addItem(createBox);

    }

    void addItem(TextView textView) {

        String text = textView.getText().toString();
        if (text == null || text.length() < 1) {
            return;
        }

        SpeechDate speechDate = new SpeechDate(text);
        TimeHypotheses selectedHypotheses = speechDate.getSelectedHypotheses();
        Long time;
        Item item;
        if (selectedHypotheses == null) {
            item = new Item(text);
        } else {
            time = selectedHypotheses.getTimeInMillis();
            item = new Item(text, time);
        }

        item.save();
        int position = item.getPositionInList();

        if (position == 0) {
            mItemAdapter.notifyDataSetChanged();
        } else {
            mItemAdapter.notifyItemInserted(position);
        }

        textView.setText("");

    }


    class EditorActionDoneListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId != EditorInfo.IME_ACTION_DONE) return false;

            addItem(textView);

            return true;
        }
    }
}
