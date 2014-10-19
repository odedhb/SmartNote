package com.blogspot.odedhb.smartnote.SpeechDating;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
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
//        parse(speechGuesses);
    }

    void parse(List<String> speechGuesses) {

        SpeechDate speechDate = new SpeechDate(speechGuesses);
        TimeHypotheses selectedHypotheses = speechDate.getSelectedHypotheses();

        if (selectedHypotheses == null) return;

        setTitle(selectedHypotheses.getSpeech());

        final Long time = selectedHypotheses.getTimeInMillis();


        //temp
        LayoutInflater inflater = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.snooze_layout, null);


        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(time);
        SimpleDateFormat monthDate = new SimpleDateFormat("MMM");
        String monthName = monthDate.format(time).toUpperCase();

        SimpleDateFormat monthDay = new SimpleDateFormat("dd");
        String dayInMonth = monthDay.format(time);

        ((TextView) view.findViewById(R.id.day_in_month)).setText(dayInMonth);
        ((TextView) view.findViewById(R.id.month_short)).setText(monthName);
        ((TextView) view.findViewById(R.id.relative_date)).setText(DateUtils.formatDateTime(App.getContext(), time,
                DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_ALL));

        view.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitListener.onSubmit(time);
                dismiss();
            }
        });

        setContentView(view);
    }

    public interface OnSubmitListener {
        void onSubmit(long time);
    }
}
