package com.harvest.harvestwork.base

import android.app.Application
import com.harvest.harvestwork.utils.AppStartUtils

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AppStartUtils.initWithOutPermission()
    }
}