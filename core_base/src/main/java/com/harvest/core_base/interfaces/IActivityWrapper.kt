package com.door.core_base.interfaces

import android.view.View

interface IActivityWrapper {

    fun obtainLayoutId(): Int

    fun obtainLayoutRootView(): View?

    fun onApplyTheme()

    suspend fun initViews()

    suspend fun initViewModel()

    suspend fun loadData()
}