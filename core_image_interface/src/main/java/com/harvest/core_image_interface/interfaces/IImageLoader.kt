package com.harvest.core_image_interface.interfaces

import android.content.Context
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.request.FutureTarget
import com.facebook.drawee.drawable.ScalingUtils

interface IImageLoader {
    fun load(url: String?, view: ImageView)

    fun loadRadius(url: String?, view: ImageView, radius: Float)

    fun load(url: String?, view: ImageView, scaleType: ScalingUtils.ScaleType?)

    fun get(url: String?, context: Context, radius: Float): FutureTarget<Drawable>
    fun get(id: Int?, context: Context, radius: Float): FutureTarget<Drawable>

    fun loadAsync(url:String?, context: Context, listener: DrawableLoadListener)
}