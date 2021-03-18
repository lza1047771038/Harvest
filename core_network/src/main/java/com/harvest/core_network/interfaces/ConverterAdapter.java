package com.harvest.core_network.interfaces;

import org.json.JSONObject;

import java.lang.reflect.Type;

public interface ConverterAdapter {

    <T> T parseJson(JSONObject jsonObject, Class<T> clazz);

    <T> T parseJson(JSONObject jsonObject, Type type);

    <T> T parseString(String source, Class<T> clazz);

    <T> T parseString(String source, Type type);
}
