package com.open.core_base.utils.system

import android.R
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.annotation.IntDef
import androidx.appcompat.app.AppCompatActivity
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

object StatusBarUtil {
    const val TYPE_MIUI = 0
    const val TYPE_FLYME = 1
    const val TYPE_M = 3 //6.0

    /**
     * 修改状态栏颜色，支持4.4以上版本
     *
     * @param colorId 颜色
     */
    fun setStatusBarColor(activity: AppCompatActivity, colorId: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val window = activity.window
            window.statusBarColor = colorId
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //使用SystemBarTintManager,需要先将状态栏设置为透明
            setTranslucentStatus(activity)
            val systemBarTintManager = SystemBarTintManager(activity)
            systemBarTintManager.isStatusBarTintEnabled = true //显示状态栏
            systemBarTintManager.setStatusBarTintColor(colorId) //设置状态栏颜色
        }
    }

    /**
     * 设置状态栏透明
     */
    @TargetApi(19)
    fun setTranslucentStatus(activity: AppCompatActivity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            val window = activity.window
            val decorView = window.decorView
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            val option = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //导航栏颜色也可以正常设置
            //window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val window = activity.window
            val attributes = window.attributes
            val flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            attributes.flags = attributes.flags or flagTranslucentStatus
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.attributes = attributes
        }
    }

    /**
     * 代码实现android:fitsSystemWindows
     *
     * @param activity
     */
    fun setRootViewFitsSystemWindows(activity: AppCompatActivity, fitSystemWindows: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val winContent = activity.findViewById<View>(R.id.content) as ViewGroup
            if (winContent.childCount > 0) {
                val rootView = winContent.getChildAt(0) as ViewGroup
                rootView.fitsSystemWindows = fitSystemWindows
            }
        }
    }

    /**
     * 设置状态栏深色浅色切换
     */
    fun setStatusBarDarkTheme(activity: AppCompatActivity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                setStatusBarFontIconDark(activity, TYPE_M, dark)
            } else if (OSUtils.isMiui) {
                setStatusBarFontIconDark(activity, TYPE_MIUI, dark)
            } else if (OSUtils.isFlyme) {
                setStatusBarFontIconDark(activity, TYPE_FLYME, dark)
            } else { //其他情况
                return false
            }
            return true
        }
        return false
    }

    /**
     * 设置 状态栏深色浅色切换
     */
    fun setStatusBarFontIconDark(activity: AppCompatActivity, @ViewType type: Int, dark: Boolean): Boolean {
        return when (type) {
            TYPE_MIUI -> setMiuiUI(activity, dark)
            TYPE_FLYME -> setFlymeUI(activity, dark)
            TYPE_M -> setCommonUI(activity, dark)
            else -> setCommonUI(activity, dark)
        }
    }

    //设置6.0 状态栏深色浅色切换
    fun setCommonUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = activity.window.decorView
            if (decorView != null) {
                var vis = decorView.systemUiVisibility
                vis = if (dark) {
                    vis or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                } else {
                    vis and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
                }
                if (decorView.systemUiVisibility != vis) {
                    decorView.systemUiVisibility = vis
                }
                return true
            }
        }
        return false
    }

    //设置Flyme 状态栏深色浅色切换
    fun setFlymeUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val lp = window.attributes
            val darkFlag = WindowManager.LayoutParams::class.java.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON")
            val meizuFlags = WindowManager.LayoutParams::class.java.getDeclaredField("meizuFlags")
            darkFlag.isAccessible = true
            meizuFlags.isAccessible = true
            val bit = darkFlag.getInt(null)
            var value = meizuFlags.getInt(lp)
            value = if (dark) {
                value or bit
            } else {
                value and bit.inv()
            }
            meizuFlags.setInt(lp, value)
            window.attributes = lp
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    //设置MIUI 状态栏深色浅色切换
    fun setMiuiUI(activity: AppCompatActivity, dark: Boolean): Boolean {
        return try {
            val window = activity.window
            val clazz: Class<*> = activity.window.javaClass
            @SuppressLint("PrivateApi") val layoutParams = Class.forName("android.view.MiuiWindowManager\$LayoutParams")
            val field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE")
            val darkModeFlag = field.getInt(layoutParams)
            val extraFlagField = clazz.getDeclaredMethod("setExtraFlags", Int::class.javaPrimitiveType, Int::class.javaPrimitiveType)
            extraFlagField.isAccessible = true
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag)
            } else {
                extraFlagField.invoke(window, 0, darkModeFlag)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    fun getDarkModeStatus(context: Context): Boolean {
        val mode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return mode == Configuration.UI_MODE_NIGHT_YES
    }

    //获取底部导航的高度
    fun getBottomStatusHeight(context: Context): Int {
        val totalHeight = getDpi(context)
        val contentHeight = getScreenHeight(context)
        return (totalHeight - contentHeight) / 2
    }

    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    fun getDpi(context: Context): Int {
        var dpi = 0
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val displayMetrics = DisplayMetrics()
        val c: Class<*>
        try {
            c = Class.forName("android.view.Display")
            val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
            method.invoke(display, displayMetrics)
            dpi = displayMetrics.heightPixels
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return dpi
    }

    //获取屏幕高度 不包含虚拟按键=
    fun getScreenHeight(context: Context): Int {
        val dm = context.resources.displayMetrics
        return dm.heightPixels
    }

    fun isShowNavBar(context: Context?, windowManager: WindowManager, window: Window): Boolean {
        if (null == context) {
            return false
        }
        /**
         * 获取应用区域高度
         */
        val outRect1 = Rect()
        try {
            window.decorView.getWindowVisibleDisplayFrame(outRect1)
        } catch (e: ClassCastException) {
            e.printStackTrace()
            return false
        }
        val activityHeight = outRect1.height()

        /**
         * 获取状态栏高度
         */
        val statuBarHeight = getStatusBarHeight(context)

        /**
         * 屏幕物理高度 减去 状态栏高度
         */
        val remainHeight = getRealHeight(windowManager) - statuBarHeight
        /**
         * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
         */
        return if (activityHeight == remainHeight) {
            false
        } else {
            true
        }
    }

    /**
     * 获取真实屏幕高度
     */
    private fun getRealHeight(windowManager: WindowManager): Int {
        val point = Point()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            windowManager.defaultDisplay.getRealSize(point)
        } else {
            windowManager.defaultDisplay.getSize(point)
        }
        return point.y
    }

    /**
     * 获取状态栏高度
     */
    fun getStatusBarHeight(context: Context?): Int {
        var result = 0
        val resourceId = context!!.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    fun getDaoHangHeight(context: Context?): Int {
        var resourceId = 0
        val rid = context!!.resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return if (rid != 0) {
            resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            context.resources.getDimensionPixelSize(resourceId)
        } else 130
    }

    @IntDef(TYPE_MIUI, TYPE_FLYME, TYPE_M)
    @Retention(RetentionPolicy.SOURCE)
    internal annotation class ViewType
}