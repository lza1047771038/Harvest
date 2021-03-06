package com.harvest.core_image.impl

import com.harvest.core_image_interface.interfaces.DrawableLoadListener

abstract class DefaultIconListener : DrawableLoadListener {
    override fun onLoadFailed(e: Exception?) {}
}