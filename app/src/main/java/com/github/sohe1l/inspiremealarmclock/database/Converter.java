package com.github.sohe1l.inspiremealarmclock.database;

import android.arch.persistence.room.TypeConverter;
import android.util.Log;

import java.time.DayOfWeek;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class Converter {

    @TypeConverter
    public static int[] stringToIntArray(String days){
        String[] daysStringArray = days.split(",");

        int[] res = new int[daysStringArray.length];

        return res;

//        for(int i = 0; i < daysStringArray.length; i++){
//
//            Log.d("Converter", "i " + i  + " " +  daysStringArray[i]);
//            res[i] = Integer.valueOf(daysStringArray[i]);
//        }
//
//        return res;
    }

    @TypeConverter
    public static String intArrayToString(int[] intArr){
        return intArr.toString();
    }

    @TypeConverter
    public static int booleanToInt(boolean b){
        return b?1:0;
    }

    public static boolean intToBoolean(int i){
        return i==1;
    }
}
