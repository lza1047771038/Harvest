package com.harvest.core_base.activity

import android.view.View
import androidx.databinding.ViewDataBinding

abstract class CommonBindingActivity<T : ViewDataBinding> : CommonActivity() {

    private var binding: T? = null

    protected abstract fun initialBinding(): T

    protected fun requireBinding(): T = binding!!

    override fun obtainLayoutRootView(): View? {
        binding = initialBinding()
        binding?.lifecycleOwner = this
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }
}