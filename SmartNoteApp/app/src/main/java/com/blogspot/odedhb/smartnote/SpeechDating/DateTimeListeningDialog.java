package com.blogspot.odedhb.smartnote.SpeechDating;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.model.Item;

import java.util.List;

/**
 * Created by oded on 10/11/14.
 */
public class DateTimeListeningDialog extends ListeningDialog {
    private OnSubmitListener submitListener;

    public DateTimeListeningDialog(Context context, OnSubmitListener submitListener) {
        super(context);
        this.submitListener = submitListener;
    }

    @Override
    public void onSpeechResults(List<String> speechGuesses) {
        setTitle(speechGuesses.get(0));
        parse(speechGuesses);
    }

    @Override
    public void onPartialSpeechResults(List<String> speechGuesses) {
        setTitle(speechGuesses.get(0));
        parse(speechGuesses);
    }

    void parse(List<String> speechGuesses) {

        final Long time = new SpeechDate(speechGuesses).getTimeInMillis();

        if (time < System.currentTimeMillis()) return;

        TextView content = new TextView(context);
        content.setText(Item.fullTimeForDisplay(time));

        LinearLayout ll = new LinearLayout(context);
        ll.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        layoutParams.setMargins(30, 20, 30, 0);

        Button okButton = new Button(context);
        okButton.setText(android.R.string.ok);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitListener.onSubmit(time);
                dismiss();
            }
        });
        ll.addView(content, layoutParams);
        ll.addView(okButton, layoutParams);

        setContentView(ll);
    }

    public interface OnSubmitListener {
        void onSubmit(long time);
    }
}
