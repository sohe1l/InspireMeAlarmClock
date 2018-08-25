package com.github.sohe1l.inspiremealarmclock.ui.debug;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;

public class QuotesActivity extends AppCompatActivity {

    AppDatabase mdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotes);

        mdb = AppDatabase.getInstance(this);

//        int count = mdb.quoteDao().getNumberOfQuotes();
//        Log.d("TAG", "COUNT OF QUOTES: " + String.valueOf(count));
////
//        String randQ = mdb.quoteDao().getRandomQuote();
//        Log.d("TAG", "RANDOM: " + randQ);

    }
}
