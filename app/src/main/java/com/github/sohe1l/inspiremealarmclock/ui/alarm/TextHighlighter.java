package com.github.sohe1l.inspiremealarmclock.ui.alarm;

import android.graphics.Color;
import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import com.github.sohe1l.inspiremealarmclock.R;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class TextHighlighter extends AsyncTask<Void, Spannable, Void> {
    private final static String TAG = "TextHighlighter";
    private static final int targetPercent = 85;
    private Set<Integer> lettersSaid = new HashSet<>();

    private TextHighlighterCallback callback;

    private final String textLC;
    private final WeakReference<TextView> textView;

    private ArrayList<String> wordsChecked = new ArrayList<String>();
    private Queue<String> wordsToCheck = new LinkedList<>();

    private Spannable spannable;

    private int highlightColor;

    TextHighlighter(TextView tv, String s, int highlightColor, TextHighlighterCallback callback) {
        Log.d(TAG, "Started - q : " + s);
        this.textView = new WeakReference<>(tv);
        spannable = new SpannableString(s);
        textLC = s.toLowerCase();
        this.callback = callback;
        this.highlightColor = highlightColor;
    }

    public void checkWords(String words){
        if(words.equals("")) return;
        String[] wordsArr = words.split("\\s");
        wordsToCheck.addAll(Arrays.asList(wordsArr));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        callback.onHighlightingDone();
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        while(true){
            while(wordsToCheck.isEmpty()){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String s = wordsToCheck.poll().toLowerCase();

            // make sure to check each word only once
            if(wordsChecked.contains(s)){
                continue;
            }else{
                wordsChecked.add(s);
            }

            int index = textLC.indexOf(s);
            while(index != -1){
                for(int j = index; j<=index+s.length(); j++){
                    lettersSaid.add(j);
                }
                spannable.setSpan(new ForegroundColorSpan(highlightColor), index, index+s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                publishProgress(spannable);
                int start = index+s.length() ;
                index = textLC.indexOf(s, start);
            }

            if(  (lettersSaid.size()  * 100) / textLC.length() > targetPercent   ){
                break;
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Spannable... values) {
        super.onProgressUpdate(values);
        textView.get().setText(values[0]);
    }

    public interface TextHighlighterCallback{
        void onHighlightingDone();
    }
}

