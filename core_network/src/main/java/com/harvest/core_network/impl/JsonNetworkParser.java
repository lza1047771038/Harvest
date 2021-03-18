package com.harvest.core_network.impl;

import androidx.annotation.NonNull;

import com.harvest.core_network.interfaces.StringNetworkParser;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class JsonNetworkParser<T> implements StringNetworkParser<T> {
    @Override
    public T parseString(String string) throws JSONException {
        JSONObject jsonObject = new JSONObject(string);
        return convert(jsonObject);
    }

    public abstract T convert(@NonNull JSONObject jsonObject);
}
