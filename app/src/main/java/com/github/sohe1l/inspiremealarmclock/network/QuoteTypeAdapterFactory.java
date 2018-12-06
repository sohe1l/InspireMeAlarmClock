package com.github.sohe1l.inspiremealarmclock.network;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class QuoteTypeAdapterFactory implements TypeAdapterFactory {

    @Override
    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);

        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);
                if (jsonElement.isJsonObject()) {
                    JsonObject jsonObject = jsonElement.getAsJsonObject();

                    if (jsonObject.has("B") && jsonObject.get("B").isJsonObject()) {
                        jsonElement = jsonObject.get("B");
                        JsonObject jsonObjectUser = jsonElement.getAsJsonObject();

                        if(jsonObjectUser.has("B.C") && jsonObjectUser.get("B.C").isJsonObject()) {
                            jsonElement = jsonObject.get("B.C");
                        }
                        if(jsonObjectUser.has("B.D") && jsonObjectUser.get("B.D").isJsonObject()) {
                            jsonElement = jsonObject.get("B.D");
                        }
                    }

                }

                return delegate.fromJsonTree(jsonElement);
            }
        }.nullSafe();


    }
}
