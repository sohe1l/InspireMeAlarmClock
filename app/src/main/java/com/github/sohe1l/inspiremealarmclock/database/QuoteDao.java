package com.github.sohe1l.inspiremealarmclock.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.github.sohe1l.inspiremealarmclock.model.Quote;

@Dao
public interface QuoteDao {

    @Query("SELECT COUNT(*) FROM quote")
    int getNumberOfQuotes();


    @Query("SELECT * FROM quote ORDER BY RANDOM() LIMIT 1")
    Quote getRandomQuote();


    @Delete
    void delete(Quote quote);


    @Insert
    void insert(Quote quote);

}
