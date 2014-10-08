package com.blogspot.odedhb.smartnote;

import android.app.Activity;
import android.os.Bundle;
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


}
