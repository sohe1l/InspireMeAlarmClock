package com.github.sohe1l.inspiremealarmclock.network;

import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {


    private static Retrofit retrofit;
    // private static final String BASE_URL = "http://talaikis.com/api/";
    private static final String BASE_URL = "http://30perks.com/api/v1/";





    public static Retrofit getRetrofitInstance() {

        Log.wtf("JSON", "GETING RETRO FIT");

        if (retrofit == null) {


            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Quote.class, new QuoteDeserializer());
            Gson quoteGson = gsonBuilder.create();

            GsonConverterFactory converterFactory = GsonConverterFactory.create(quoteGson);

            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(converterFactory)
                    .build();
        }
        return retrofit;
    }

    //                    .addConverterFactory(buildGsonConverter())
    // .addConverterFactory(GsonConverterFactory.create())



}
