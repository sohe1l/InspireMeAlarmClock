package com.github.sohe1l.inspiremealarmclock.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.github.sohe1l.inspiremealarmclock.model.Quote;

import java.util.List;

@Dao
public interface QuoteDao {

    @Query("SELECT COUNT(*) FROM quote")
    int getNumberOfQuotes();


    @Query("SELECT * FROM quote ORDER BY RANDOM() LIMIT 1")
    Quote getRandomQuote();





    @Insert
    void insert(Quote quote);

}
