package com.harvest.core_network.impl;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.harvest.core_network.interfaces.ConverterAdapter;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import org.json.JSONObject;

import java.lang.reflect.Type;

public class MoshiConverterAdapter implements ConverterAdapter {

    private static MoshiConverterAdapter mInstance = null;

    @NonNull
    public static MoshiConverterAdapter getInstance() {
        if (mInstance == null) {
            synchronized (MoshiConverterAdapter.class) {
                if (mInstance == null) {
                    mInstance = new MoshiConverterAdapter();
                }
            }
        }
        return mInstance;
    }


    private final Moshi moshi = new Moshi.Builder()
            .add(new KotlinJsonAdapterFactory()).build();

    @Nullable
    public <T> T parseJson(JSONObject jsonObject, Class<T> clazz) {
        try {
            if(jsonObject != null && !jsonObject.equals(JSONObject.NULL)){
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
            if(jsonObject != null && !jsonObject.equals(JSONObject.NULL)){
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
            JsonAdapter<T> jsonAdapter = moshi.adapter(clazz);
            return jsonAdapter.fromJson(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Nullable
    public <T> T parseString(String str, Type type) {
        try {
            JsonAdapter<T> jsonAdapter = moshi.adapter(type);
            return jsonAdapter.fromJson(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
