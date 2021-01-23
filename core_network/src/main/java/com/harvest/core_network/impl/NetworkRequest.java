package com.harvest.core_network.impl;

import com.open.core_network.utils.NetworkStatusUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class NetworkRequest {

    private String url = null;
    private Map<Object, Object> map = null;
    private ErrorNotifier errorNotifier = null;
    private boolean forceLocalCache = false;
    private boolean doGet = false;
    private boolean doPost = false;

    NetworkRequest(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public NetworkRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public Map<Object, Object> getMap() {
        return map;
    }

    public NetworkRequest setParams(Map<Object, Object> map) {
        this.map = map;
        return this;
    }

    public NetworkRequest doGet() {
        doGet = true;
        doPost = false;
        return this;
    }

    public NetworkRequest doPost() {
        doPost = true;
        doGet = false;
        return this;
    }

    public NetworkRequest setParams(Object... params) {
        if (map == null) {
            map = new HashMap<>();
        }

        for (int index = 0; index < params.length; index += 2) {
            map.put(params[index], params[index + 1]);
        }
        return this;
    }

    public boolean isForceLocalCache() {
        return forceLocalCache;
    }

    public NetworkRequest setForceLocalCache(boolean forceLocalCache) {
        this.forceLocalCache = forceLocalCache;
        return this;
    }

    public ErrorNotifier getErrorNotifier() {
        return errorNotifier;
    }

    public NetworkRequest setErrorNotifier(ErrorNotifier errorNotifier) {
        this.errorNotifier = errorNotifier;
        return this;
    }

    public <T> T executeApi(JsonNetworkParser<T> callback) {
        if (!doPost && !doGet) {
            doGet();
        }

        final FormBody.Builder formBody = new FormBody.Builder();

        final boolean isNetworkEnable = NetworkStatusUtils.getInstance().isNetworkConnected();

        final String domain = Configurations.domainUrl;
        Request request;
        final Map<Object, Object> map = getMap();
        if (doGet) {
            final StringBuilder stringBuilder = new StringBuilder();
            if (!map.isEmpty()) {
                stringBuilder.append("?");
                for (Object key : map.keySet()) {
                    final Object value = map.get(key);
                    if (value != null) {
                        stringBuilder.append(key.toString()).append("=").append(value.toString()).append("&");
                    }
                }
            }
            request = new Request.Builder()
                    .url(domain + getUrl() + stringBuilder.toString())
                    .get()
                    .cacheControl((isForceLocalCache() || !isNetworkEnable) ? CacheControl.FORCE_CACHE : CacheControl.FORCE_NETWORK)
                    .build();
        } else {
            for (Object key : map.keySet()) {
                final Object value = map.get(key);
                if (value != null) {
                    formBody.add(key.toString(), value.toString());
                }
            }
            request = new Request.Builder()
                    .url(domain + getUrl())
                    .post(formBody.build())
                    .cacheControl((isForceLocalCache() || !isNetworkEnable) ? CacheControl.FORCE_CACHE : CacheControl.FORCE_NETWORK)
                    .build();
        }
        try {
            final Response response = HRetrofit.getInstance().getOkHttpClient().newCall(request).execute();
            final ResponseBody responseBody = response.body();
            if (responseBody != null) {
                final String responseString = responseBody.string();
                final JSONObject jsonObject = new JSONObject(responseString);
                return callback.convert(jsonObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
            final ErrorNotifier errorNotifier = getErrorNotifier();
            if (errorNotifier != null) {
                HRetrofit.getInstance().getMainHandler().post(() -> errorNotifier.notifyError(e));
            }
        }
        return null;
    }
}

