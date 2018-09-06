package com.github.sohe1l.inspiremealarmclock.receiver;

import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("AlarmReceiver", "onReceive called");


        Intent alarmIntent = new Intent(context, AlarmActivity.class);

        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


//        intent.setAction(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

        context.startActivity(alarmIntent);

    }
}
