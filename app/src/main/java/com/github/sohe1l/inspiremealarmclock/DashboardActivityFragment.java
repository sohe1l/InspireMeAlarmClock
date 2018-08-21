package com.github.sohe1l.inspiremealarmclock;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.RecyclerItemClickListener;
import com.github.sohe1l.inspiremealarmclock.ui.dashboard.AlarmAdapter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A placeholder fragment containing a simple view.
 */
public class DashboardActivityFragment extends Fragment implements RecyclerItemClickListener {

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

        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    private void loadAlarms(){
        if(alarms == null) return;
        AlarmAdapter alarmAdapter = new AlarmAdapter(alarms, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvAlarms.setLayoutManager(layoutManager);
        rvAlarms.setHasFixedSize(true);
        rvAlarms.setAdapter(alarmAdapter);
    }

    @Override
    public void onRecyclerItemClicked(int index) {

    }
}
