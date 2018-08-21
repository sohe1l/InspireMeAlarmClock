package com.github.sohe1l.inspiremealarmclock.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import java.time.DayOfWeek;

@Entity(tableName = "alarm")
public class Alarm implements Parcelable {


    @PrimaryKey(autoGenerate = true)
    private int id;
    String label;
    String time;
    String sound;
    boolean vibrate;
    int[] repeat;
    String challenge;


    protected Alarm(Parcel in) {
        id = in.readInt();
        label = in.readString();
        time = in.readString();
        sound = in.readString();
        vibrate = in.readByte() != 0;
        repeat = in.createIntArray();
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

    public Alarm(int id, String label, String time, String sound, boolean vibrate, int[] repeat, String challenge) {
        this.id = id;
        this.label = label;
        this.time = time;
        this.sound = sound;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challenge = challenge;
    }

    @Ignore
    public Alarm(String label, String time, String sound, boolean vibrate, int[] repeat, String challenge) {
        this.label = label;
        this.time = time;
        this.sound = sound;
        this.vibrate = vibrate;
        this.repeat = repeat;
        this.challenge = challenge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(label);
        dest.writeString(time);
        dest.writeString(sound);
        dest.writeByte((byte)(vibrate?1:0));
        dest.writeIntArray(repeat);
        dest.writeString(challenge);
    }




    // getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public int[] getRepeat() {
        return repeat;
    }

    public void setRepeat(int[] repeat) {
        this.repeat = repeat;
    }

    public String getChallenge() {
        return challenge;
    }

    public void setChallenge(String challenge) {
        this.challenge = challenge;
    }



}
