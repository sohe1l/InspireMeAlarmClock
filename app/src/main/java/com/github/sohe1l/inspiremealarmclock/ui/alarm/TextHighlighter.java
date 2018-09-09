package com.github.sohe1l.inspiremealarmclock.ui.alarm;

import android.os.AsyncTask;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

class TextHighlighter extends AsyncTask<Void, Spannable, Void> {
    private final static String TAG = "TextHighlighter";
    private static final int targetPercent = 85;
    private final Set<Integer> lettersSaid = new HashSet<>();

    private final TextHighlighterCallback callback;

    private final String textLC;
    private final WeakReference<TextView> textView;

    private final ArrayList<String> wordsChecked = new ArrayList<>();
    private final Queue<String> wordsToCheck = new LinkedList<>();

    private final Spannable spannable;

    private final int highlightColor;

    TextHighlighter(TextView tv, String s, int highlightColor, TextHighlighterCallback callback) {
        Log.wtf(TAG, "Started - q : " + s);
        this.textView = new WeakReference<>(tv);
        spannable = new SpannableString(s);
        textLC = s.toLowerCase();
        this.callback = callback;
        this.highlightColor = highlightColor;

        Log.wtf(TAG, "@@@@ END OF CONST ");

    }

    public void checkWords(String words){
        Log.wtf("WORDS", words);
        if(words.equals("")) return;
        String[] wordsArr = words.split("\\s");
        wordsToCheck.addAll(Arrays.asList(wordsArr));
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ DONE @@@@@@@");
        callback.onHighlightingDone();
        super.onPostExecute(aVoid);
    }

    @Override
    protected Void doInBackground(Void... voids) {

        Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ INSIDE DO IN BACKGROUND");

        while(true){
            Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ INSIDE W1");

            while(wordsToCheck.isEmpty()){
                if (isCancelled()) break;

                try {
                    Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ INSIDE W2");
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String s = wordsToCheck.poll().toLowerCase();


            Log.wtf("WORDS", "@@@@@@@@@@@ CHECKED COUNT: " + wordsChecked.size());


            // make sure to check each word only once
            if(wordsChecked.contains(s)){
                Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ ALREADY CHECKED: " + s);

                continue;
            }else{
                wordsChecked.add(s);
            }

            int index = textLC.indexOf(s);
            while(index != -1){
                Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ WHILE @@@@@@@");
                for(int j = index; j<=index+s.length(); j++){
                    lettersSaid.add(j);
                }
                spannable.setSpan(new ForegroundColorSpan(highlightColor), index, index+s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ PUBLISHING @@@@@@@");
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
        Log.wtf("WORDS", "@@@@@@@@@@@@@@@@@@@@@@ UPDATING @@@@@@@");
        super.onProgressUpdate(values);
        textView.get().setText(values[0]);
    }

    public interface TextHighlighterCallback{
        void onHighlightingDone();
    }
}

