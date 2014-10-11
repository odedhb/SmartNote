package com.blogspot.odedhb.smartnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.controller.ItemAdapter;
import com.blogspot.odedhb.smartnote.model.Item;


public class MyActivity extends Activity {


    private ListView listView;
    private EditText createItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        App.periodicCheckForOverdueItems();

        getActionBar().hide();

        listView = (ListView) findViewById(R.id.item_list);
        listView.setAdapter(new ItemAdapter(this));

        createItem = (EditText) findViewById(R.id.create_edit_text);
        createItem.setOnEditorActionListener(new EditorActionDoneListener());

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void editItem(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(getLayoutInflater().inflate(R.layout.task_options, null));

        builder.show();

    }

    class EditorActionDoneListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {

            if (actionId != EditorInfo.IME_ACTION_DONE) return false;

            String text = textView.getText().toString();
            if (text == null || text.length() < 1) {
                return false;
            }
            new Item(text, System.currentTimeMillis()).save();

            return true;
        }
    }
}
