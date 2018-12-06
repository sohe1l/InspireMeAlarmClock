package com.github.sohe1l.inspiremealarmclock.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.model.Alarm;
import com.github.sohe1l.inspiremealarmclock.ui.alarm.AlarmActivity;
import com.github.sohe1l.inspiremealarmclock.utilities.ParcelableUtil;

public class AlarmReceiver extends BroadcastReceiver {

    private final int  CHECK_INTERVAL = 30000; // 30 seconds

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.wtf("AlarmReceiver", "onReceive called");

        Alarm alarm;
        if(intent.hasExtra(Alarm.INTENT_KEY)){

            Bundle extras = intent.getExtras();
            byte[] byteArray = extras.getByteArray(Alarm.INTENT_KEY);
            alarm = ParcelableUtil.unmarshall(byteArray, Alarm.CREATOR);

            //alarm = intent.getParcelableExtra(Alarm.INTENT_KEY);
        }else{
            return;
        }

        // we must query the db to get the current alarm status
        AppDatabase mDb = AppDatabase.getInstance(context);
        alarm = mDb.alarmDao().getAlarm(alarm.getId());

        if(alarm == null || !alarm.isActive()){
            return;
        }

        if(alarm.isChallengeDone()){

            alarm.scheduleAlarmNextOccurrence(context);
            // set challenge done to false to make sure user has to do the challenge
            alarm.setChallengeDone(false);
            mDb.alarmDao().update(alarm);

        }else{ // make the alarm ring again if not already ringing

            Intent alarmIntent = new Intent(context, AlarmActivity.class);


            //byte[] bytes = ParcelableUtil.marshall(alarm);
            alarmIntent.putExtra(Alarm.INTENT_KEY, alarm);

            alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(alarmIntent);


            //intent.setAction(Intent.ACTION_MAIN);
            //intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);




            // schedule to check again if the user has closed the alarm
            alarm.scheduleAlarm(CHECK_INTERVAL, context);
        }

    }
}
