package com.harvest.core_network.impl;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.harvest.core_network.interfaces.ConverterAdapter;
import com.squareup.moshi.JsonAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class GsonConverterAdapter implements ConverterAdapter {

    private static GsonConverterAdapter mInstance = null;

    public static GsonConverterAdapter getInstance() {
        if (mInstance == null) {
            synchronized (GsonConverterAdapter.class) {
                if (mInstance == null) {
                    mInstance = new GsonConverterAdapter();
                }
            }
        }
        return mInstance;
    }

    private final Gson gson = new Gson();

    @Nullable
    public <T> T parseJson(JSONObject jsonObject, Class<T> clazz) {
        try {
            if (jsonObject != null && !jsonObject.equals(JSONObject.NULL)) {
                return parseString(jsonObject.toString(), clazz);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public <T> T parseJson(JSONObject jsonObject, Type type) {
        try {
            if (jsonObject != null && !jsonObject.equals(JSONObject.NULL)) {
                return parseString(jsonObject.toString(), type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public <T> T parseString(String str, Class<T> clazz) {
        try {
            return gson.fromJson(str, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public <T> T parseString(String str, Type type) {
        try {
            return gson.fromJson(str, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
