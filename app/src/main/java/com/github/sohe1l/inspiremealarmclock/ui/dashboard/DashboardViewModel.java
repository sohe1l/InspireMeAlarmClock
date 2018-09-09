package com.github.sohe1l.inspiremealarmclock.ui.dashboard;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;

import java.util.List;

class DashboardViewModel extends AndroidViewModel {

    private static final String TAG = DashboardViewModel.class.getSimpleName();

    private final LiveData<List<Alarm>> alarms;

    public DashboardViewModel(@NonNull Application application) {
        super(application);


        Log.d(TAG, "Getting alarms from database.");
        AppDatabase mDb = AppDatabase.getInstance(getApplication());
        alarms = mDb.alarmDao().getAll();


    }

    public LiveData<List<Alarm>> getAlarms() {
        return alarms;
    }
}
