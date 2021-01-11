package com.harvest.core_network.impl;

import com.harvest.core_base.database.bean.CookieCache;
import com.harvest.core_base.database.instance.DBInstance;
import com.harvest.core_network.factory.ConverterFactory;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.TimeUnit;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.Dispatchers;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HRetrofit {

    private static HRetrofit mInstance = null;

    public static HRetrofit getInstance() {
        if (mInstance == null) {
            synchronized (HRetrofit.class) {
                if (mInstance == null) {
                    mInstance = new HRetrofit();
                }
            }
        }
        return mInstance;
    }

    private final Retrofit.Builder retrofitBuilder;
    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;

    HRetrofit() {
        okHttpClient = configClient();
        retrofitBuilder = configBuilder();
        retrofit = retrofitBuilder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private Retrofit.Builder configBuilder() {
        return new Retrofit.Builder().baseUrl(Configurations.domainUrl)
                .addConverterFactory(ConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);
    }

    private OkHttpClient configClient() {
        return new OkHttpClient.Builder()
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .cookieJar(new HCookieJar())
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .build();
    }
}

interface Configurations {
    String domainUrl = "";
    String appKey = "";
    String appId = "";
}

class HCookieJar implements CookieJar {

    @Override
    public void saveFromResponse(@NotNull HttpUrl url, @NotNull List<Cookie> cookies) {
        CookieCache[] cookieCaches = CookieCache.toCookieCaches(url, cookies);
        DBInstance.getAppDatabase().getCookieDao().saveCookies(cookieCaches);
    }

    @NotNull
    @Override
    public List<Cookie> loadForRequest(@NotNull HttpUrl url) {
        String targetUrl = url.toString();
        List<CookieCache> cookieCaches = DBInstance.getAppDatabase().getCookieDao().getCookies(targetUrl);
        return CookieCache.toCookies(cookieCaches);
    }
}