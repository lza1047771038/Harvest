package com.harvest.core_base.service;

import java.util.concurrent.ConcurrentHashMap;


/**
 * created by liuzhongao on 2021-01-08
 * 作为服务总线，可以在module中注册，然后通过interface获取其实现类
 */
public class ServiceFacade {

    private static ServiceFacade mInstance = null;

    public static ServiceFacade getInstance() {
        if (mInstance == null) {
            throw new NullPointerException("please call init() before calling getInstance() method ");
        }
        return mInstance;
    }

    public static void init() {
        if (mInstance == null) {
            synchronized (ServiceFacade.class) {
                if (mInstance == null) {
                    mInstance = new ServiceFacade();
                }
            }
        }
    }


    private final ConcurrentHashMap<Class<?>, Object> serviceFacade = new ConcurrentHashMap<>();

    public final void put(Class<?> tClass, Object impl) {
        serviceFacade.putIfAbsent(tClass, impl);
    }

    public final <T> T get(Class<T> tClass) {
        return (T) serviceFacade.get(tClass);
    }
}
