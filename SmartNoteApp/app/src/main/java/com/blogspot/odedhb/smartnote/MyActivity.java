package com.blogspot.odedhb.smartnote;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.model.Item;


public class MyActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        App.periodicCheckForOverdueItems();


    }

    @Override
    protected void onResume() {
        super.onResume();

        StringBuffer stringBuffer = new StringBuffer();

        for (Item item : Item.getAll()) {
            stringBuffer.append(item.desc).append("\n").append(item.timeForDisplay()).append("\n\n");
        }

        ((TextView) findViewById(R.id.main_message)).setText(stringBuffer);
    }


    public void createItem(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

        // Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String text = input.getText().toString();
                if (text == null || text.length() < 1) {
                    return;
                }
                new Item(text, System.currentTimeMillis()).save();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}
