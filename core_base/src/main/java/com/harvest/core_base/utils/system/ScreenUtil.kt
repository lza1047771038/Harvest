package com.open.core_base.utils.system

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

object ScreenUtil {

    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }

    fun dp2px(context: Context, dp: Int): Int {
        val displayScale = context.resources.displayMetrics.density
        return (dp * displayScale + 0.5).toInt()
    }

    fun dp2px(context: Context, dp: Double): Int {
        val displayScale = context.resources.displayMetrics.density
        return (dp * displayScale + 0.5).toInt()
    }

    /**
     * 获取屏幕的宽
     */
    fun getScreenWidth(context: Context): Int {
        val dm = DisplayMetrics()
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        display.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取屏幕的高
     */
    fun getScreenHeight(context: Context): Int {
        return context.resources.displayMetrics.heightPixels
    }

}