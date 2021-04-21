package com.harvest.core_base.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.harvest.core_base.coroutine.launch
import com.harvest.core_base.interfaces.IFragmentWrapper

abstract class CommonFragment : Fragment(), IFragmentWrapper {

    protected var onDataLoaded = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            globalBroadcast(intent)
        }
    }

    override fun obtainLayoutId(): Int = 0

    override fun obtainLayoutRootView(
        layoutInflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view: View? = null
        val layoutId = obtainLayoutId()
        var layoutResLoaded = false
        if (layoutId != 0) {
            view = inflater.inflate(layoutId, container, false)
            layoutResLoaded = true
        }
        if (!layoutResLoaded) {
            view = obtainLayoutRootView(inflater, container, savedInstanceState)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        onApplyTheme()

        val intentFilter = IntentFilter()
        initIntentFilter(intentFilter)

        activity?.let {
            LocalBroadcastManager.getInstance(it).registerReceiver(broadcastReceiver, intentFilter)
        }

        launch {
            initViews()
            initViewModel()
            if (!onDataLoaded) {
                loadData()
                onDataLoaded = true
            }
        }
    }

    override fun onApplyTheme() {}

    protected open fun initIntentFilter(filter: IntentFilter) {}

    protected open fun globalBroadcast(intent: Intent?) {}

    @CallSuper
    override suspend fun initViews() {
    }

    @CallSuper
    override suspend fun initViewModel() {
    }

    @CallSuper
    override suspend fun loadData() {
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(broadcastReceiver)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onDataLoaded = false
    }
}