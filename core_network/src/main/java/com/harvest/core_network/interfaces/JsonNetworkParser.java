package com.harvest.core_network.interfaces;

import org.json.JSONObject;

public interface JsonNetworkParser<T> {
    T convert(JSONObject jsonObject);
}
