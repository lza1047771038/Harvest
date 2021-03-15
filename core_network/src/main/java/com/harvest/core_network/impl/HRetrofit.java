package com.harvest.core_network.impl;

import android.os.Environment;
import android.os.Handler;
import android.os.Looper;

import com.harvest.core_base.database.bean.CookieCache;
import com.harvest.core_base.database.instance.DBInstance;
import com.harvest.core_network.factory.ConverterFactory;
import com.open.core_network.utils.NetworkStatusUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HRetrofit {

    private static HRetrofit mInstance = null;
    private static final File cacheDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "cache");

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
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    HRetrofit() {
        okHttpClient = configClient();
        retrofitBuilder = configBuilder();
        retrofit = retrofitBuilder.build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    public Handler getMainHandler() {
        return mHandler;
    }

    private Retrofit.Builder configBuilder() {
        return new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Configurations.domainUrl)
                .addConverterFactory(ConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create());
    }

    private OkHttpClient configClient() {
        return new OkHttpClient.Builder()
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .connectTimeout(1000, TimeUnit.MILLISECONDS)
                .cache(new Cache(cacheDir, 50 * 1024 * 1024))
                .readTimeout(2000, TimeUnit.MILLISECONDS)
                .addNetworkInterceptor(new CacheInterceptor())
                .addInterceptor(new CacheInterceptor())
                .build();
    }
}

interface Configurations {
    String domainUrl = "https://api.weibo.com/";
}

class CacheInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();
        Request.Builder newBuilder = request.newBuilder();
        boolean isNetworkConnected = NetworkStatusUtils.getInstance().isNetworkConnected();
        if (!isNetworkConnected) {
            newBuilder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(newBuilder.build());
    }
}