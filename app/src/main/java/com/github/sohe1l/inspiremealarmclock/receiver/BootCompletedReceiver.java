package com.github.sohe1l.inspiremealarmclock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;

import java.util.Calendar;
import java.util.List;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmActivity.class.toString();

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm.setALlAlarms(context);
    }
}
