package com.github.sohe1l.inspiremealarmclock.database;

import android.arch.persistence.room.TypeConverter;
import android.net.Uri;

import java.util.ArrayList;

public class Converter {

    @TypeConverter
    public static ArrayList<Integer> stringToIntegerArrayList(String days){
        if(days == null || days.equals("")) return null;

        ArrayList<Integer> res = new ArrayList<>();
        String[] daysStringArray = days.split(",");
        for (String day : daysStringArray) {
            res.add(Integer.valueOf(day));
        }
        return res;
    }

    @TypeConverter
    public static String integerArrayListToString(ArrayList<Integer> ints){
        if(ints == null) return "";
        return android.text.TextUtils.join(",", ints);
    }

    @TypeConverter
    public static int booleanToInt(boolean b){
        return b?1:0;
    }

    @TypeConverter
    public static boolean intToBoolean(int i){
        return i==1;
    }


    @TypeConverter
    public static String uriToString(Uri uri){
        return uri.toString();
    }


    @TypeConverter
    public static Uri stringToUri(String uriString){
        return Uri.parse(uriString);
    }


}
