package com.github.sohe1l.inspiremealarmclock.network;

import com.github.sohe1l.inspiremealarmclock.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;

public interface GetQuotesService {

    @GET("quote")
    Call<Quote> getQuote();

    @GET("quote")
    Call<List<Quote>> getQuotes();

}
