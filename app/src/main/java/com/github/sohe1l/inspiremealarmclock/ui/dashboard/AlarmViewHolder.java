package com.github.sohe1l.inspiremealarmclock.ui.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.SwitchClickListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final private RecyclerItemClickListener clickListener;
    final private SwitchClickListener switchClickListener;

    @BindView(R.id.alarm_title)
    TextView alarmTitle;

    @BindView(R.id.alarm_time)
    TextView alarmTime;

    @BindView(R.id.alarm_repeat)
    TextView alarmRepeat;

    @BindView(R.id.sw_active)
    Switch swActive;

    private String[] days;

    public AlarmViewHolder(@NonNull View itemView, RecyclerItemClickListener clickListener, SwitchClickListener switchListener) {
        super(itemView);
        this.clickListener = clickListener;
        switchClickListener = switchListener;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
        days = itemView.getResources().getStringArray(R.array.day_short);
    }

    @Override
    public void onClick(View v) {
        int clickedPos = getAdapterPosition();
        clickListener.onRecyclerItemClicked(clickedPos);
    }

    void bind(final Alarm alarm) {
        if(alarm.getLabel().equals(""))
            alarmTitle.setVisibility(View.GONE);
        else
            alarmTitle.setText(alarm.getLabel());

        alarmTime.setText(alarm.getTime12hformat());

        if(alarm.getRepeat() != null) {
            ArrayList<Integer> repeat = alarm.getRepeat();
            Collections.sort(repeat);

            for (int day: repeat) {
                alarmRepeat.append(days[day] + " ");

            }
        }

        swActive.setChecked(alarm.isActive());
        swActive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchClickListener.onSwitchClicked(alarm.getId(), isChecked);
            }
        });
    }
}
