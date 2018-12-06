package com.github.sohe1l.inspiremealarmclock.ui.dashboard;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.create.CreateAlarmActivity;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.SwitchClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashboardActivityFragment extends Fragment
        implements RecyclerItemClickListener, SwitchClickListener {

    private static final String TAG = "DashboardActivityFragment";

    @BindView(R.id.no_alarm_title)
    TextView tvNoAlarm;

    @BindView(R.id.no_alarm_subtitle)
    TextView tvNoAlarmSubTitle;

    @BindView(R.id.dashboard_rv_alarms)
    RecyclerView rvAlarms;
    private List<Alarm> alarms;

    public DashboardActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DashboardViewModel viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);


        viewModel.getAlarms().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(@Nullable List<Alarm> newAlarms) {
                alarms = newAlarms;
                loadAlarms();
                Alarm.setALlAlarms(getContext());
            }
        });

        loadAlarms();

        return view;
    }

    private void showNoAlarmText(){
        tvNoAlarmSubTitle.setVisibility(View.VISIBLE);
        tvNoAlarm.setVisibility(View.VISIBLE);
    }

    private void hideNoAlarmText(){
        tvNoAlarmSubTitle.setVisibility(View.GONE);
        tvNoAlarm.setVisibility(View.GONE);
    }

    private void loadAlarms(){
        if(alarms == null){
            showNoAlarmText();
            return;
        }
        if(alarms.size() == 0) showNoAlarmText();
        else hideNoAlarmText();

        AlarmAdapter alarmAdapter = new AlarmAdapter(alarms, this, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvAlarms.setLayoutManager(layoutManager);
        rvAlarms.setHasFixedSize(true);
        rvAlarms.setAdapter(alarmAdapter);

    }

    @Override
    public void onRecyclerItemClicked(int index) {
        Intent editAlarmIntent = new Intent(getContext(), CreateAlarmActivity.class);
        editAlarmIntent.putExtra(Alarm.INTENT_KEY, alarms.get(index));
        startActivity(editAlarmIntent);

    }

    @Override
    public void onSwitchClicked(int id, boolean isOn) {

        AppDatabase mDb = AppDatabase.getInstance(getContext());
        Alarm alarm = mDb.alarmDao().getAlarm(id);
        alarm.setActive(isOn);
        mDb.alarmDao().update(alarm);
    }
}
