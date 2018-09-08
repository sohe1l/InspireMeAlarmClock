package com.github.sohe1l.inspiremealarmclock;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.CreateAlarmActivity;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.SwitchClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.dashboard.AlarmAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashboardActivityFragment extends Fragment
        implements RecyclerItemClickListener, SwitchClickListener {

    private static final String TAG = DashboardActivityFragment.class.getSimpleName();

    @BindView(R.id.dashboard_rv_alarms)
    RecyclerView rvAlarms;
    List<Alarm> alarms;

    public DashboardActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        DashboardViewModel viewModel = ViewModelProviders.of(getActivity()).get(DashboardViewModel.class);

        viewModel.getAlarms().observe(this, new Observer<List<Alarm>>() {
            @Override
            public void onChanged(@Nullable List<Alarm> newAlarms) {
                alarms = newAlarms;
                loadAlarms();
            }
        });

        loadAlarms();
//
//        ItemTouchHelper.SimpleCallback itemTouchHelper = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
//
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
//                Toast.makeText(getContext(), "on Move", Toast.LENGTH_SHORT).show();
//
//            }
//        };
//
//        new ItemTouchHelper(itemTouchHelper).attachToRecyclerView(rvAlarms);
//
//

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void loadAlarms(){
        if(alarms == null) return;
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

        // set up the alarms again
        Alarm.setALlAlarms(getContext());
    }
}
