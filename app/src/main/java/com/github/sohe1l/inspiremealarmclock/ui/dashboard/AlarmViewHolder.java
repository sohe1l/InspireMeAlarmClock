package com.github.sohe1l.inspiremealarmclock.ui.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AlarmViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    final private RecyclerItemClickListener clickListener;

    @BindView(R.id.alarm_title)
    TextView alarmTitle;

    @BindView(R.id.alarm_time)
    TextView alarmTime;

    @BindView(R.id.alarm_repeat)
    TextView alarmRepeat;

    @BindView(R.id.sw_active)
    Switch swActive;


    public AlarmViewHolder(@NonNull View itemView, RecyclerItemClickListener clickListener) {
        super(itemView);
        this.clickListener = clickListener;
        itemView.setOnClickListener(this);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onClick(View v) {
        int clickedPos = getAdapterPosition();
        clickListener.onRecyclerItemClicked(clickedPos);
    }

    void bind(Alarm alarm) {
        alarmTitle.setText(alarm.getLabel());
        alarmTime.setText(alarm.getTime12hformat());
        swActive.setChecked(alarm.isActive());

        if(alarm.getRepeat() != null) {
            alarmRepeat.setText( android.text.TextUtils.join(",", alarm.getRepeat()));
        }
    }
}
