package com.harvest.core_network.impl;

import org.json.JSONObject;

public interface JsonNetworkParser<T> {
    T convert(JSONObject jsonObject);
}
