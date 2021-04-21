package com.harvest.core_base.interfaces

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface IFragmentWrapper {


    fun obtainLayoutId(): Int

    fun obtainLayoutRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View?

    fun onApplyTheme()

    suspend fun initViews()

    suspend fun initViewModel()

    suspend fun loadData()
}