package com.open.core_base.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope

abstract class CommonActivity : AppCompatActivity() {

    private var layoutInflated: Boolean = false

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

        lifecycleScope.launchWhenCreated {
            initViews()
            loadData()
        }
    }

    protected open fun configUIThemes() {
        val decorView = window.decorView
        decorView.systemUiVisibility = decorView.systemUiVisibility
            .or(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            .or(View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            .or(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION)
    }

    protected open suspend fun initViews() {}
    protected open suspend fun loadData() {}
}