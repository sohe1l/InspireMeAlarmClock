package com.github.sohe1l.inspiremealarmclock.network;

import android.util.Log;

import com.github.sohe1l.inspiremealarmclock.model.Quote;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class QuoteDeserializer implements JsonDeserializer<Quote> {

    @Override
    public Quote deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {

        try{
            final JsonObject quoteJson = json.getAsJsonObject();
            final String quoteText = quoteJson.get("quote").getAsString();
            //final String quoteAuthor = quoteJson.get("author").getAsString();

            return new Quote(quoteText,"","");

        }catch(Exception e){

            e.printStackTrace();

            Log.wtf("JSON", "HITTING EXCEPTION " + e.getMessage());



            return null;
        }
    }
}
