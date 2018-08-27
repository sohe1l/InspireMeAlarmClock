package com.github.sohe1l.inspiremealarmclock.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.github.sohe1l.inspiremealarmclock.database.Converter;

import java.time.DayOfWeek;
import java.util.ArrayList;

@Entity(tableName = "alarm")
public class Alarm implements Parcelable{

    @PrimaryKey(autoGenerate = true)
    private int id;
    boolean active;
    String label;
    int hour; // always will be in 24hour format
    int minute;
    Uri ringtone;
    boolean vibrate;
    ArrayList<Integer> repeat;  // from 1 (Monday) to 7 (Sunday).
    String challenge;

    @Ignore
    public static final String INTENT_KEY = "ALARM_INTENT_KEY";

    public Alarm(int id, boolean active, String label, int hour, int minute, Uri ringtone, boolean vibrate, ArrayList<Integer> repeat, String challenge) {
        this.id = id;
        this.active = active;
        this.label = label;
        this.hour = hour;
        this.minute = minute;
        this.ringtone = ringtone;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challenge = challenge;
    }

    @Ignore
    public Alarm(boolean active, String label, int hour, int minute, Uri ringtone, boolean vibrate, ArrayList<Integer> repeat, String challenge) {
        this.active = active;
        this.label = label;
        this.hour = hour;
        this.minute = minute;
        this.ringtone = ringtone;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challenge = challenge;
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

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
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
        challenge = in.readString();
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
        dest.writeString(challenge);
    }
}
