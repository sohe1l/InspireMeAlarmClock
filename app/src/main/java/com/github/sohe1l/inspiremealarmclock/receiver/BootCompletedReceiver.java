package com.github.sohe1l.inspiremealarmclock.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);


        Intent receiverIntent = new Intent(context, AlarmReceiver.class);


        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, receiverIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + 15000,
                15000, alarmIntent);

    }
}
