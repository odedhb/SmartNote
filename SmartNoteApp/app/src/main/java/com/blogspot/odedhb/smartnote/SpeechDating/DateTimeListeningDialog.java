package com.blogspot.odedhb.smartnote.SpeechDating;

import android.content.Context;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blogspot.odedhb.smartnote.App;
import com.blogspot.odedhb.smartnote.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by oded on 10/11/14.
 */
public class DateTimeListeningDialog extends ListeningDialog {
    private final LinearLayout confirmationView;
    private OnSubmitListener submitListener;

    public DateTimeListeningDialog(Context context, OnSubmitListener submitListener) {
        super(context);
        this.submitListener = submitListener;
        confirmationView = (LinearLayout) ((LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.snooze_layout, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(confirmationView);
        ((RelativeLayout) confirmationView.findViewById(R.id.animation_holder)).addView(speechAnimation);
    }

    @Override
    public void onSpeechResults(List<String> speechGuesses) {
        setTitle(speechGuesses.get(0));
        parse(speechGuesses);
    }

    @Override
    public void onPartialSpeechResults(List<String> speechGuesses) {
        setTitle(speechGuesses.get(0));
//        parse(speechGuesses);
    }

    void parse(List<String> speechGuesses) {

        SpeechDate speechDate = new SpeechDate(speechGuesses);
        TimeHypotheses selectedHypotheses = speechDate.getSelectedHypotheses();

        if (selectedHypotheses == null) return;

        setTitle(selectedHypotheses.getSpeech());

        final Long time = selectedHypotheses.getTimeInMillis();


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        SimpleDateFormat monthDate = new SimpleDateFormat("MMM");
        String monthName = monthDate.format(time).toUpperCase();

        SimpleDateFormat monthDay = new SimpleDateFormat("dd");
        String dayInMonth = monthDay.format(time);

        ((TextView) confirmationView.findViewById(R.id.day_in_month)).setText(dayInMonth);
        ((TextView) confirmationView.findViewById(R.id.month_short)).setText(monthName);
        ((TextView) confirmationView.findViewById(R.id.relative_date)).setText(DateUtils.formatDateTime(App.getContext(), time,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));

        confirmationView.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitListener.onSubmit(time);
                dismiss();
            }
        });

//        setContentView(confirmationView);
    }

    public interface OnSubmitListener {
        void onSubmit(long time);
    }
}
