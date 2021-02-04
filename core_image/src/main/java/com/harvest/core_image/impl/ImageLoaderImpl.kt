package com.harvest.core_image.impl

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.FutureTarget
import com.facebook.drawee.drawable.ScalingUtils
import com.harvest.core_image.R
import com.harvest.core_image.utils.GlideRoundTransform
import com.harvest.core_image_interface.interfaces.IImage
import com.open.core_image_interface.interfaces.IImageLoader
import java.util.concurrent.FutureTask

class ImageLoaderImpl : IImageLoader {

    override fun load(url: String?, view: ImageView) {
        val builder = build(view).load(url)
            .centerCrop()
        builder.into(view)
    }

    override fun load(url: String?, view: ImageView, scaleType: ScalingUtils.ScaleType?) {
        val builder = build(view).load(url).let {
            when (scaleType) {
                ScalingUtils.ScaleType.FIT_CENTER -> {
                    it.optionalFitCenter()
                }
                ScalingUtils.ScaleType.CENTER -> {
                    it.optionalCenterInside()
                }
                else -> {
                    it.optionalCenterCrop()
                }
            }
            it
        }
        builder.into(view)
    }

    override fun loadRadius(url: String?, view: ImageView, radius: Float) {
        val builder = build(view)
            .load(url)
            .transform(GlideRoundTransform(radius.toInt()))
        builder.into(view)
    }

    override fun get(url: String?, context: Context, radius: Float): FutureTarget<Drawable> {
        return build(context)
            .load(url)
            .transform(GlideRoundTransform(radius.toInt()))
            .submit()
    }

    override fun get(id: Int?, context: Context, radius: Float): FutureTarget<Drawable> {
        return build(context)
            .load(id)
            .transform(GlideRoundTransform(radius.toInt()))
            .submit()
    }

    private fun build(context: Context): RequestBuilder<Drawable> {
        return configGlide(context).defaultConfig()
    }

    private fun build(view: ImageView): RequestBuilder<Drawable> {
        return configGlide(view).defaultConfig()
    }

    private fun configGlide(context: Context): RequestManager {
        return Glide.with(context)

    }

    private fun configGlide(view: View): RequestManager {
        return Glide.with(view)
    }
}

fun RequestManager.defaultConfig(): RequestBuilder<Drawable> {
    return asDrawable()
        .error(R.drawable.ic_pic_fail)
        .placeholder(R.color.gray_light)
        .thumbnail(0.25f)
        .transition(DrawableTransitionOptions.withCrossFade(IImage.CROSSFADE_DURATION))
}