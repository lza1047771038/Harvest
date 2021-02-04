package com.harvest.core_base.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope

abstract class CommonActivity : AppCompatActivity() {

    private var layoutInflated: Boolean = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            globalBroadcast(intent)
        }
    }

    /**
     * @return layout resource id, 0 for no layout display
     */
    protected open fun getLayoutId(): Int = 0

    protected open fun getRootView(): View? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        configUIThemes()

        val layoutId = getLayoutId()
        if (layoutId != 0) {
            layoutInflated = true
            setContentView(layoutId)
        }

        if (!layoutInflated) {
            val rootView = getRootView()
            if (rootView != null) {
                layoutInflated = true
                setContentView(rootView)
            }
        }

        val intentFilter = IntentFilter()
        initIntentFilter(intentFilter)
        registerReceiver(broadcastReceiver, intentFilter)

        lifecycleScope.launchWhenCreated {
            initViews()
            loadData()
        }
    }

    protected open fun configUIThemes() {
        val decorView = window.decorView
        val uiVisibility = decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        decorView.systemUiVisibility = uiVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            decorView.systemUiVisibility =
                decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
        }
    }

    protected open fun initIntentFilter(filter: IntentFilter) {}

    protected open fun globalBroadcast(intent: Intent?) {}

    protected open suspend fun initViews() {}

    protected open suspend fun loadData() {}

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(broadcastReceiver)
    }
}