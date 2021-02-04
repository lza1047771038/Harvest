package com.harvest.core_image_interface.interfaces;

import android.graphics.drawable.Drawable;

import androidx.annotation.WorkerThread;

import com.facebook.drawee.view.SimpleDraweeView;

public class ImageRequest {

    String url = null;
    CacheType cacheType = null;
    ImageListener imageListener = null;

    ImageRequest setRequest(ImageRequest imageRequest) {
        return this;
    }

    ImageRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    ImageRequest setCacheType(CacheType cacheType) {
        this.cacheType = cacheType;
        return this;
    }

    ImageRequest setListener(ImageListener listener) {
        this.imageListener = listener;
        return this;
    }

    void loadInto(SimpleDraweeView simpleDraweeView) {

    }

    @WorkerThread
    Drawable fetchDrawable() {
        return null;
    }

    public static enum CacheType {
        DEFAULT,
        DISK_ONLY,
        NETWORK_ONLY
    }

    public static interface ImageListener {
        void onSuccess(Drawable drawable);

        void onFail(Throwable throwable);
    }
}


