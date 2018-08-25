package com.github.sohe1l.inspiremealarmclock.network;

import com.github.sohe1l.inspiremealarmclock.model.Quote;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface GetQuotesService {

    @GET("quotes")
    Call<List<Quote>> getQuotes();

}
