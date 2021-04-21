package com.harvest.core_base.activity

import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding

abstract class CommonBindingActivity<T : ViewDataBinding> : CommonActivity() {

    private var binding: T? = null

    abstract fun initialBinding(): T

    protected fun requireBinding(): T = binding!!

    override fun obtainLayoutRootView(): View? {
        if (binding == null) {
            binding = initialBinding()
            binding?.lifecycleOwner = this
        }
        binding?.root?.let {
            if (it.parent is ViewGroup) {
                (it.parent as ViewGroup).removeView(it)
            }
        }
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        binding?.unbind()
    }
}