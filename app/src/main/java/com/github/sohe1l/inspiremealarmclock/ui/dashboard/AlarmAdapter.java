package com.github.sohe1l.inspiremealarmclock.ui.dashboard;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.SwitchClickListener;

import java.util.List;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmViewHolder>  {

    final private List<Alarm> alarms;
    final private RecyclerItemClickListener recyclerClickListener;
    final private SwitchClickListener switchClickListener;


    public AlarmAdapter(List<Alarm> alarms, RecyclerItemClickListener listener, SwitchClickListener switchListener ) {
        this.alarms = alarms;
        recyclerClickListener = listener;
        switchClickListener = switchListener;
    }

    @NonNull
    @Override
    public AlarmViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dashboard_alarm_item, viewGroup, false);
        return new AlarmViewHolder(view, recyclerClickListener, switchClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AlarmViewHolder alarmViewHolder, int i) {
        if (alarms.size() > i) {
            alarmViewHolder.bind(alarms.get(i));
        }
    }

    @Override
    public int getItemCount() {
        if (alarms == null) {
            return 0;
        } else {
            return alarms.size();
        }
    }
}
