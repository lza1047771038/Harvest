package com.harvest.core_image_interface.interfaces

import android.graphics.drawable.Drawable

interface DrawableLoadListener {
    fun onLoadSuccess(drawable:Drawable)

    fun onLoadFailed(e:Exception?)
}