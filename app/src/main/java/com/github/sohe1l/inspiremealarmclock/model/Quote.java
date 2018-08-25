package com.github.sohe1l.inspiremealarmclock.model;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "quote")
public class Quote {

    @PrimaryKey(autoGenerate = true)
    private int id;
    String quote;
    String author;
    String category;

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
}
