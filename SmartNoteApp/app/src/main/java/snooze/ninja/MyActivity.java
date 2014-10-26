package snooze.ninja;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import snooze.ninja.SpeechDating.SpeechDate;
import snooze.ninja.SpeechDating.TimeHypotheses;
import snooze.ninja.controller.ItemAdapter;
import snooze.ninja.model.Item;


public class MyActivity extends ActionBarActivity {


    private ListView listView;
    private EditText createItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        App.periodicCheckForOverdueItems();

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

            SpeechDate speechDate = new SpeechDate(text);
            TimeHypotheses selectedHypotheses = speechDate.getSelectedHypotheses();
            Long time;
            if (selectedHypotheses == null) {
                new Item(text).save();
            } else {
                time = selectedHypotheses.getTimeInMillis();
                new Item(text, time).save();
            }


            InputMethodManager imm = (InputMethodManager) getSystemService(
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
            textView.setText("");
            return true;
        }
    }
}
