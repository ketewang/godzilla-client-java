package com.jurassic.godzilla.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JSONUtil {
    private static final Gson gson = new GsonBuilder().create();

    public static <T> String toJson(T t) {
        return gson.toJson(t);
    }

    public static <T> T fromJson(String jsonStr, Class<T> clazz) {
        return gson.fromJson(jsonStr, clazz);
    }
}
