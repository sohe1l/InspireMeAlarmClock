package com.github.sohe1l.inspiremealarmclock.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.content.Context;

import com.github.sohe1l.inspiremealarmclock.R;
import com.github.sohe1l.inspiremealarmclock.database.AppDatabase;
import com.github.sohe1l.inspiremealarmclock.job.QuotesJobService;

@Entity(tableName = "quote")
public class Quote {

    @Ignore
    public static final String JOB_TAG = "QUOTE_JOB_TAG";
    public static final String QUOTE_TEXT_INTENT_KEY = "QUOTE_TEXT_INTENT_KEY";


    @PrimaryKey(autoGenerate = true)
    private int id;
    private String quote;
    private String author;
    private String category;

    public Quote(int id, String quote, String author, String category) {
        this.id = id;
        this.quote = quote;
        this.author = author;
        this.category = category;
    }

    @Ignore
    public Quote(String quote, String author, String category) {
        this.quote = quote;
        this.author = author;
        this.category = category;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }





    @SuppressLint("DefaultLocale") // used for logs only
    @Override
    public String toString() {
        return String.format("Id: %d Quote: %s Author: %s Category: %S", id, quote, author, category);
    }



    public static Quote getRandomQuote(Context context){
        Quote quote;
        AppDatabase mDB = AppDatabase.getInstance(context);
        int count = mDB.quoteDao().getNumberOfQuotes();
        if(count == 0){
            // the database is empty, load new quotes
            QuotesJobService.scheduleQuoteJob(context);

            // use the default quote
            quote = new Quote(
                    context.getString(R.string.default_quote),
                    context.getString(R.string.default_quote_author),
                    context.getString(R.string.default_quote_category));
        }else{
            quote = mDB.quoteDao().getRandomQuote();
        }
        return quote;
    }


}
