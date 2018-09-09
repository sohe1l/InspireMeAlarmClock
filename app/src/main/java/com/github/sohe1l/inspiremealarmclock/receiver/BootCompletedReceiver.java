package com.github.sohe1l.inspiremealarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;

public class BootCompletedReceiver extends BroadcastReceiver {

    private static final String TAG = AlarmActivity.class.toString();

    @Override
    public void onReceive(Context context, Intent intent) {
        Alarm.setALlAlarms(context);
    }
}
