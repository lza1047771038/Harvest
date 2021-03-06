package com.harvest.core_base.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.door.core_base.interfaces.IActivityWrapper

abstract class CommonActivity : AppCompatActivity(), IActivityWrapper {

    private var layoutInflated: Boolean = false

    private val broadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            globalBroadcast(intent)
        }
    }

    /**
     * @return layout resource id, 0 for no layout display
     */
    override fun obtainLayoutId(): Int = 0

    override fun obtainLayoutRootView(): View? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        onApplyTheme()

        super.onCreate(savedInstanceState)

        val layoutId = obtainLayoutId()
        if (layoutId != 0) {
            layoutInflated = true
            setContentView(layoutId)
        }

        if (!layoutInflated) {
            val rootView = obtainLayoutRootView()
            if (rootView != null) {
                layoutInflated = true
                setContentView(rootView)
            }
        }

        val intentFilter = IntentFilter()
        initIntentFilter(intentFilter)
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, intentFilter)

        lifecycleScope.launchWhenCreated {
            initViews()
            initViewModel()
            loadData()
        }
    }

    override fun onApplyTheme() {
        val decorView = window.decorView

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.setDecorFitsSystemWindows(false)
        } else {
            val uiVisibility = decorView.systemUiVisibility or
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            decorView.systemUiVisibility = uiVisibility

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                decorView.systemUiVisibility =
                    decorView.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            }
        }
    }

    protected open fun initIntentFilter(filter: IntentFilter) {}

    protected open fun globalBroadcast(intent: Intent?) {}

    override suspend fun initViews() {}

    override suspend fun initViewModel() {}

    override suspend fun loadData() {}

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}