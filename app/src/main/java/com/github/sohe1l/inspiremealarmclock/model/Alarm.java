package com.github.sohe1l.inspiremealarmclock.model;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.database.Converter;
import com.github.sohe1l.inspiremealarmclock.receiver.AlarmReceiver;
import com.github.sohe1l.inspiremealarmclock.utilities.ParcelableUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Entity(tableName = "alarm")
public class Alarm implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    private boolean active;
    private String label;
    private int hour; // always will be in 24hour format
    private int minute;
    private Uri ringtone;
    private boolean vibrate;
    private ArrayList<Integer> repeat;  // based on Calendar.MONDAY, ...
    private boolean challengeDone; // if its true means the user has solved the challenge and alarm is off

    @Ignore
    public static final String INTENT_KEY = "ALARM_INTENT_KEY";

    @Ignore
    public static final String INTENT_KEY_TESTING = "ALARM_INTENT_TEST_KEY";


    public Alarm(int id, boolean active, String label, int hour, int minute, Uri ringtone, boolean vibrate, ArrayList<Integer> repeat, boolean challengeDone) {
        this.id = id;
        this.active = active;
        this.label = label;
        this.hour = hour;
        this.minute = minute;
        this.ringtone = ringtone;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challengeDone = challengeDone;
    }

    @Ignore
    public Alarm(boolean active, String label, int hour, int minute, Uri ringtone, boolean vibrate, ArrayList<Integer> repeat, boolean challengeDone) {
        this.active = active;
        this.label = label;
        this.hour = hour;
        this.minute = minute;
        this.ringtone = ringtone;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challengeDone = challengeDone;
    }


    // getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public Uri getRingtone() {
        return ringtone;
    }

    public void setRingtone(Uri ringtone) {
        this.ringtone = ringtone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public ArrayList<Integer> getRepeat() {
        return repeat;
    }

    public void setRepeat(ArrayList<Integer> repeat) {
        this.repeat = repeat;
    }

    public boolean isChallengeDone() {
        return challengeDone;
    }

    public void setChallengeDone(boolean challengeDone) {
        this.challengeDone = challengeDone;
    }


    public String getTime12hformat(){
        if(hour == 0)
            return "12:" + minute + " AM";
        else if(hour == 12)
            return "12:" + minute + " PM";
        else if(hour > 12)
            return (hour-12) + ":" + minute + " PM";
        else
            return hour + ":" + minute + " AM";
    }




    /* PARCELABLE */

    protected Alarm(Parcel in) {
        id = in.readInt();
        active = in.readByte() != 0;
        label = in.readString();
        hour = in.readInt();
        minute = in.readInt();
        ringtone = in.readParcelable(Uri.class.getClassLoader());
        vibrate = in.readByte() != 0;
        repeat = Converter.stringToIntegerArrayList(in.readString());
        challengeDone = in.readByte() != 0;
    }

    public static final Creator<Alarm> CREATOR = new Creator<Alarm>() {
        @Override
        public Alarm createFromParcel(Parcel in) {
            return new Alarm(in);
        }

        @Override
        public Alarm[] newArray(int size) {
            return new Alarm[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(label);
        dest.writeInt(hour);
        dest.writeInt(minute);
        dest.writeParcelable(ringtone, flags);
        dest.writeByte((byte) (vibrate ? 1 : 0));
        dest.writeString(Converter.integerArrayListToString(repeat));
        dest.writeByte((byte) (challengeDone ? 1 : 0));
    }


    private int getAsMilliseconds(){
        return hour*60*60*1000 + minute*60*1000;
    }

    private int getNextOccurrence(){

        Calendar calendar = Calendar.getInstance();
        final int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        final int hour = calendar.get(Calendar.HOUR_OF_DAY);
        final int minute = calendar.get(Calendar.MINUTE);
        final int currentMills = hour*60*60*1000 + minute*60*1000;
        int mills = 0;

        if(repeat == null){

            mills = getAsMilliseconds() - currentMills;

            if(getAsMilliseconds() < currentMills){
                mills = 24*60*60*1000 + mills;
            }

        }else{
            // here we want to find the next day of repeat closest to the current day
            // repeat must contain at least one day between 1 to 7 so
            // we are going to check every day starting from current day with a for loop
            int currentLoopDay = currentDay;
            for(int i = 0; i<7; i++){
                if(repeat.contains(currentLoopDay)){

                    // if alarm is same day && not already passed the alarm time must skip day
                    if(currentLoopDay != currentDay ||  getAsMilliseconds() > currentMills){
                        mills = getMillsForRepeating(currentLoopDay);
                        break;
                    }
                }
                if(currentLoopDay == 7) currentLoopDay = 0;
                currentLoopDay++;
            }
        }
        return mills;
    }

    private int getNextMillsForNonRepeating(){
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int currentMills = hour*60*60*1000 + minute*60*1000;

        int mills = getAsMilliseconds() - currentMills;

        if(getAsMilliseconds() < currentMills){
            mills = 24*60*60*1000 + mills;
        }

        return mills;
    }

    private int getMillsForRepeating(int repeatDay){

        final int oneDayInMills = 24*60*60*1000;
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int currentMills = hour*60*60*1000 + minute*60*1000;
        final int currentDay = calendar.get(Calendar.DAY_OF_WEEK);
        int mills;

        if(currentDay == repeatDay ){
            mills = getNextMillsForNonRepeating();

            if(getAsMilliseconds() < currentMills){
                mills -= oneDayInMills;
                mills += 6 * oneDayInMills;
            }


        }else if(repeatDay > currentDay){
            mills = (repeatDay-currentDay)*oneDayInMills
                    +  getNextMillsForNonRepeating();

            // remove one day if alarm is before current time
            if(getAsMilliseconds() < currentMills){
                mills -= oneDayInMills;
            }
        }else{ // repeatDay < currentDay

            mills = (7+repeatDay-currentDay)*oneDayInMills
                    +  getNextMillsForNonRepeating();

            // remove one day if alarm is before current time
            if(getAsMilliseconds() < currentMills){
                mills -= oneDayInMills;
            }
        }
        return mills;
    }

    public void scheduleAlarmNextOccurrence(Context context){
        scheduleAlarm(getNextOccurrence(), context);
    }


    public void scheduleAlarm(int afterMills, Context context){

        if(!active) return;

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent alarmReceiverIntent = new Intent(context, AlarmReceiver.class);
        //alarmReceiverIntent.putExtra(Alarm.INTENT_KEY, this);

        byte[] bytes = ParcelableUtil.marshall(this);
        alarmReceiverIntent.putExtra(Alarm.INTENT_KEY, bytes);


        int flag = 0;
//        if(!active){
//            flag = PendingIntent.FLAG_CANCEL_CURRENT;
//        }

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, getId(), alarmReceiverIntent, flag);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + afterMills,
                    pendingIntent);
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + afterMills,
                    pendingIntent);
        }


//        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
//                SystemClock.elapsedRealtime() + afterMills,
//                pendingIntent);
    }

    public static void setALlAlarms(Context context){

        Log.wtf("AL", "Scheduling alarms");

        // here we must not update the alarms otherwise will create infinite loop

        AppDatabase mDb = AppDatabase.getInstance(context);
        List<Alarm> activeAlarms = mDb.alarmDao().getActiveAlarmsAsList();
        for (Alarm alarm : activeAlarms) {
            alarm.scheduleAlarmNextOccurrence(context);
            // set challenge done to false to make sure user has to do the challenge
//            alarm.setChallengeDone(false);
//            mDb.alarmDao().update(alarm);
        }

    }
}
